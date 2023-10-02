package com.swimmingpool.order;

import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.order.request.OrderSearchRequest;
import com.swimmingpool.order.response.OrderSearchResponse;

public interface IOrderService {
    PageResponse<OrderSearchResponse> searchOrder(OrderSearchRequest orderSearchRequest);
}
