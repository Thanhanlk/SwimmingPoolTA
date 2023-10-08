package com.swimmingpool.payment.service;

import com.swimmingpool.order.request.CreateOrderRequest;

public interface IPaymentService {
    String payment(CreateOrderRequest createOrderRequest);

    void createOrder(CreateOrderRequest createOrderRequest);
}
