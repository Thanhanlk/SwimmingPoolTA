package com.swimmingpool.payment;

import com.swimmingpool.order.OrderConstant;
import com.swimmingpool.payment.service.IPaymentService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentFactory {

    @Qualifier("offPaymentService")
    private final IPaymentService offPaymentService;

    @Qualifier("paypalPaymentService")
    private final IPaymentService paypalPaymentService;

    @NonNull
    public IPaymentService getInstance(OrderConstant.MethodPayment method) {
        assert method != null;
        switch (method) {
            case PAYPAL:
                return this.paypalPaymentService;
            case OFF:
                return this.offPaymentService;
            default:
                throw new UnsupportedOperationException("");
        }
    }
}
