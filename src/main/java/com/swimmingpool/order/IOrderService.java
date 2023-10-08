package com.swimmingpool.order;

import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.order.request.OrderSearchRequest;
import com.swimmingpool.order.response.OrderSearchResponse;

import java.util.List;

public interface IOrderService {
    PageResponse<OrderSearchResponse> searchOrder(OrderSearchRequest orderSearchRequest);

    List<Order> findByAssignmentIds(List<String> assignmentIds);

    Order findByAssignmentIdAndCurrentUser(String assignmentId);
}
