package com.swimmingpool.common.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class BusinessException extends RuntimeException {

    private String redirectUrl;
    private Map<String, Object> data;

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
