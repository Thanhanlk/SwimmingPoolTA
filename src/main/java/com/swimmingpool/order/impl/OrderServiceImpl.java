package com.swimmingpool.order.impl;

import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.order.IOrderService;
import com.swimmingpool.order.request.OrderSearchRequest;
import com.swimmingpool.order.response.OrderSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final CustomOrderRepository customOrderRepository;

    @Override
    public PageResponse<OrderSearchResponse> searchOrder(OrderSearchRequest orderSearchRequest) {
        return this.customOrderRepository.searchOrder(orderSearchRequest);
    }
}
