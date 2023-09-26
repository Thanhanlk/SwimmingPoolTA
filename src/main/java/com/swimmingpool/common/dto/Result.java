package com.swimmingpool.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Result<T> {

    private T data;
    private String message;
    private boolean isSuccess;

    public static <T> Result<T> success(T data, String message) {
        return Result.<T>builder()
                .isSuccess(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> Result<T> fail(T data, String message) {
        return Result.<T>builder()
                .isSuccess(false)
                .data(data)
                .message(message)
                .build();
    }
}
