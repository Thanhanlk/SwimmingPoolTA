package com.swimmingpool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;

@SpringBootApplication(exclude = CacheAutoConfiguration.class)
public class SwimmingPoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwimmingPoolApplication.class, args);
    }
}
