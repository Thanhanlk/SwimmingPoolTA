package com.swimmingpool.cart.impl;

import com.swimmingpool.assignment.Assignment;
import com.swimmingpool.assignment.IAssignmentService;
import com.swimmingpool.cart.Cart;
import com.swimmingpool.cart.CartRepository;
import com.swimmingpool.cart.ICartService;
import com.swimmingpool.cart.response.CartResponse;
import com.swimmingpool.common.exception.ValidationException;
import com.swimmingpool.common.util.I18nUtil;
import com.swimmingpool.order.IOrderService;
import com.swimmingpool.order.Order;
import com.swimmingpool.pool.IPoolService;
import com.swimmingpool.pool.Pool;
import com.swimmingpool.user.IUserService;
import com.swimmingpool.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements ICartService {

    private final CartRepository cartRepository;
    private final CustomCartRepository customCartRepository;

    @Setter(onMethod_ = @Autowired, onParam_ = @Lazy)
    private IPoolService poolService;

    @Setter(onMethod_ = @Autowired, onParam_ = @Lazy)
    private IAssignmentService assignmentService;

    @Setter(onMethod_ = @Autowired, onParam_ = @Lazy)
    private IUserService userService;

    @Setter(onMethod_ = @Autowired, onParam_ = @Lazy)
    private IOrderService orderService;

    @Override
    public List<CartResponse> getMyCart(List<String> cartIds) {
        log.info("get my cart");
        UserResponse userResponse = this.userService.getCurrentUserThrowIfNot();
        return this.customCartRepository.getMyCart(userResponse.getUsername(), cartIds);
    }

    @Override
    @Transactional
    @CacheEvict(
            cacheManager = "redisCacheManager",
            cacheNames = "CART",
            key="T(org.springframework.security.core.context.SecurityContextHolder).getContext()?.authentication?.name",
            beforeInvocation = true
    )
    public void addToCart(List<String> assignmentIds) {
        log.info("add to cart: {}", assignmentIds);
        Map<String, Assignment> assignmentMap = this.assignmentService.findByIds(assignmentIds)
                .stream()
                .collect(Collectors.toMap(Assignment::getId, x -> x));
        StringBuilder errorMess = new StringBuilder();
        UserResponse userResponse = this.userService.getCurrentUserThrowIfNot();
        Map<String, List<Order>> orderMap = this.orderService.findByAssignmentIds(assignmentIds)
                .stream()
                .collect(Collectors.groupingBy(Order::getAssignmentId));
        assignmentIds.forEach(assignmentId -> {
            Assignment assignment = assignmentMap.get(assignmentId);
            String messageErrorAssignment = this.getMessageErrorAssignment(assignment, assignmentId, orderMap);
            if (Objects.nonNull(messageErrorAssignment)) {
                errorMess.append(messageErrorAssignment).append("</br>");
                return;
            }
            if (this.cartRepository.findByUsernameAndAssignmentId(userResponse.getUsername(), assignmentId).isPresent()) {
                return;
            }
            Cart cart = new Cart();
            cart.setAssignmentId(assignmentId);
            cart.setUsername(userResponse.getUsername());
            this.cartRepository.save(cart);
        });
        if (!errorMess.isEmpty()) {
            ValidationException validationException = new ValidationException(errorMess.toString());
            validationException.set18N(false);
            throw validationException;
        }
    }

    @Override
    @Transactional
    @CacheEvict(
            cacheManager = "redisCacheManager",
            cacheNames = "CART",
            key="T(org.springframework.security.core.context.SecurityContextHolder).getContext()?.authentication?.name",
            beforeInvocation = true
    )
    public void deleteById(String cId) {
        Cart cart = this.cartRepository.findById(cId)
                .orElseThrow(() -> new ValidationException("cart.id.validate.not-exist", cId));
        UserResponse userResponse = this.userService.getCurrentUserThrowIfNot();
        if (!cart.getUsername().equals(userResponse.getUsername())) {
            throw new ValidationException("common.delete.not-auth");
        }
        this.cartRepository.deleteById(cId);
    }

    @Override
    @Transactional
    @CacheEvict(
            cacheManager = "redisCacheManager",
            cacheNames = "CART",
            key="T(org.springframework.security.core.context.SecurityContextHolder).getContext()?.authentication?.name",
            beforeInvocation = true
    )
    public void deleteAllCart() {
        this.cartRepository.deleteByUsername();
    }

    private String getMessageErrorAssignment(Assignment assignment, String id, Map<String, List<Order>> orderMap) {
        if (Objects.isNull(assignment)) {
            return I18nUtil.getMessage("assignment.id.validate.not-exist", id);
        }

        DayOfWeek dayOfWeek = DayOfWeek.of(assignment.getDayOfWeek());
        String i18nDayOfWeek = I18nUtil.getMessage("day-of-week." + dayOfWeek.name());

        if (!assignment.getActive()) {
           return I18nUtil.getMessage("assignment.active.validate.false", i18nDayOfWeek, assignment.getStartTime().toString(), assignment.getEndTime().toString());
        }

        if (assignment.getStartDate().before(new Date())) {
            return I18nUtil.getMessage("assignment.start-date.validate.started", i18nDayOfWeek, assignment.getStartTime().toString(), assignment.getEndTime().toString());
        }
        Pool pool = this.poolService.findByIdThrowIfNotPresent(assignment.getPoolId());
        List<Order> orders = orderMap.getOrDefault(id, Collections.emptyList());
        if (pool.getNumberOfStudent().equals(orders.size())) {
            return I18nUtil.getMessage("assignment.number-of-student.validate.max", i18nDayOfWeek, assignment.getStartTime().toString(), assignment.getEndTime().toString());
        }

        return null;
    }
}
