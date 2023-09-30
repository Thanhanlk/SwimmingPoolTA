package com.swimmingpool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class SwimmingPoolApplication {

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC+7"));
    }

    public static void main(String[] args) {
        SpringApplication.run(SwimmingPoolApplication.class, args);
    }

}
