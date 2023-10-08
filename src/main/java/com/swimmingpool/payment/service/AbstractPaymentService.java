package com.swimmingpool.payment.service;

import com.swimmingpool.assignment.Assignment;
import com.swimmingpool.assignment.IAssignmentService;
import com.swimmingpool.cart.CartRepository;
import com.swimmingpool.cart.ICartService;
import com.swimmingpool.cart.response.CartResponse;
import com.swimmingpool.common.exception.ValidationException;
import com.swimmingpool.common.util.I18nUtil;
import com.swimmingpool.course.Course;
import com.swimmingpool.course.ICourseService;
import com.swimmingpool.order.IOrderService;
import com.swimmingpool.order.Order;
import com.swimmingpool.order.OrderConstant;
import com.swimmingpool.order.OrderRepository;
import com.swimmingpool.order.request.CreateOrderRequest;
import com.swimmingpool.pool.IPoolService;
import com.swimmingpool.pool.Pool;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Setter(onMethod_ = @Autowired)
public  abstract class AbstractPaymentService implements IPaymentService {

    protected OrderRepository orderRepository;
    protected CartRepository cartRepository;

    protected ICartService cartService;
    protected IAssignmentService assignmentService;
    protected IOrderService orderService;
    protected ICourseService courseService;
    protected IPoolService poolService;

    @Override
    public void createOrder(CreateOrderRequest createOrderRequest) {
        List<CartResponse> carts = this.getCartByIds(createOrderRequest.getCartId());
        for (CartResponse cart : carts) {
            Assignment assignment = this.assignmentService.findByIdThrowIfNotPresent(cart.getAssignmentId());
            this.validateAssignment(assignment);
            Order order = new Order();
            order.setStatus(OrderConstant.Status.PAID);
            order.setCreatedBy(createOrderRequest.getCreatedBy());
            order.setPhone(createOrderRequest.getPhone());
            order.setFullName(createOrderRequest.getFullName());
            order.setMethodPayment(createOrderRequest.getMethod());
            order.setPrice(cart.getPrice());
            order.setPaymentOnlineID(createOrderRequest.getPaymentId());
            order.setDiscount(cart.getDiscount());
            order.setAssignmentId(cart.getAssignmentId());
            this.orderRepository.save(order);
            this.cartRepository.deleteById(cart.getCartId());
        }
    }

    protected List<CartResponse> getCartByIds(List<String> ids) {
        return this.cartService.getMyCart(ids);
    }

    protected void validateAssignment(Assignment assignment) {
        DayOfWeek dayOfWeek = DayOfWeek.of(assignment.getDayOfWeek());
        String i18nDayOfWeek = I18nUtil.getMessage("day-of-week." + dayOfWeek.name());
        if (!assignment.getActive()) {
            throw new ValidationException("assignment.active.validate.false", i18nDayOfWeek, assignment.getStartTime().toString(), assignment.getEndTime().toString());
        }

        if (assignment.getStartDate().before(new Date())) {
            throw new ValidationException("assignment.start-date.validate.started", i18nDayOfWeek, assignment.getStartTime().toString(), assignment.getEndTime().toString());
        }
        Order order = this.orderService.findByAssignmentIdAndCurrentUser(assignment.getId());
        if (Objects.nonNull(order)) {
            Course course = courseService.findByIdThrowIfNotPresent(assignment.getCourseId());
            throw new ValidationException("order.validate.register", course.getName(), i18nDayOfWeek, assignment.getStartTime().toString(), assignment.getEndTime().toString());
        }
        List<Order> byAssignmentIds = this.orderService.findByAssignmentIds(List.of(assignment.getId()));
        Pool pool = this.poolService.findByIdThrowIfNotPresent(assignment.getPoolId());
        if (pool.getNumberOfStudent() <= byAssignmentIds.size()) {
            throw new ValidationException("assignment.number-of-student.validate.max", i18nDayOfWeek, assignment.getStartTime().toString(), assignment.getEndTime().toString());
        }
    }
}
