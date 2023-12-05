package com.swimmingpool.chatgpt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "chatgpt")
@Setter
@Getter
public class ChatGptProperties {
    private String url;
    private int readTimeout;
    private int connectionTimeout;
    private String apiKey;
    private String model;
}
