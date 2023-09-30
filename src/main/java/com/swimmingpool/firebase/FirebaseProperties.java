package com.swimmingpool.firebase;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "firebase")
@ConditionalOnProperty(prefix = "firebase", name = "enable")
@Setter
@Getter
public class FirebaseProperties {
    private Boolean enable;
    private String location;
    private String bucket;
    private String urlImage;
}
