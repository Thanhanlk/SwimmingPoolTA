package com.swimmingpool.paypal;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "paypal")
@Setter
@Getter
public class PaypalProperties {

    private String clientId;
    private String clientSecret;
    private PaypalConstant.Mode mode;
    private String cancelUrl;
    private String successUrl;
    private String currency;
}
