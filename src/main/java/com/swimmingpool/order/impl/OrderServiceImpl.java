package com.swimmingpool.order.impl;

import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.order.IOrderService;
import com.swimmingpool.order.Order;
import com.swimmingpool.order.OrderRepository;
import com.swimmingpool.order.request.OrderSearchRequest;
import com.swimmingpool.order.response.OrderSearchResponse;
import com.swimmingpool.timetable.response.TimetableResponse;
import com.swimmingpool.user.IUserService;
import com.swimmingpool.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final CustomOrderRepository customOrderRepository;
    private final IUserService userService;

    @Override
    public PageResponse<OrderSearchResponse> searchOrder(OrderSearchRequest orderSearchRequest) {
        return this.customOrderRepository.searchOrder(orderSearchRequest);
    }

    @Override
    public List<Order> findByAssignmentIds(List<String> assignmentIds) {
        return this.orderRepository.findByAssignmentId(assignmentIds);
    }

    @Override
    public Order findByAssignmentIdAndCurrentUser(String assignmentId) {
        return this.orderRepository.findByAssignmentIdAndCurrentUser(assignmentId).orElse(null);
    }
}
