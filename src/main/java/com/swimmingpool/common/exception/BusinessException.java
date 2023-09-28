package com.swimmingpool.common.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
public class BusinessException extends RuntimeException {

    private final String redirectUrl;
    private final Map<String, Object> data;

    public BusinessException(String message, String redirectUrl) {
        super(message);
        this.redirectUrl = redirectUrl;
        this.data = null;
    }

    public BusinessException(String message, String redirectUrl, Map<String, Object> data) {
        super(message);
        this.redirectUrl = redirectUrl;
        this.data = data;
    }
}
