package com.swimmingpool.payment.service;

import com.swimmingpool.order.request.CreateOrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("offPaymentService")
@Slf4j
public class OFFPaymentService extends AbstractPaymentService {

    @Override
    public String payment(CreateOrderRequest createOrderRequest) {
        log.info("payment COD: {}", createOrderRequest);
        this.createOrder(createOrderRequest);
        return "redirect:/order";
    }

}
