package com.swimmingpool.paypal;

import com.paypal.base.rest.APIContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(PaypalProperties.class)
public class PaypalConfig {

    @Bean
    public APIContext apiContext(PaypalProperties paypalProperties) {
        return new APIContext(paypalProperties.getClientId(), paypalProperties.getClientSecret(), paypalProperties.getMode().name());
    }
}
