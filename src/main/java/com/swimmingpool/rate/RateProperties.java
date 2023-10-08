package com.swimmingpool.rate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rate")
@Setter
@Getter
public class RateProperties {
    private String currency;
    private String accessToken;
    private String url;
}
