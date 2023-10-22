package com.swimmingpool.order;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderConstant {

    public enum Status {
        PAID, UNPAID
    }

    public enum MethodPayment {
        VNPAY, PAYPAL, OFF
    }
}
