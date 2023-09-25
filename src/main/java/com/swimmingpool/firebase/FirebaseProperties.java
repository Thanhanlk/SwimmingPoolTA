package com.swimmingpool.firebase;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "firebase")
@Setter
@Getter
public class FirebaseProperties {
    private String location;
    private String bucket;
}
