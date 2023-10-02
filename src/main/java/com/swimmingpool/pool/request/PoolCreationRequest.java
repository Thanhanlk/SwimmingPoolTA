package com.swimmingpool.pool.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PoolCreationRequest {

    private String id;

    @NotBlank(message = "pool.code.validate.empty")
    private String code;

    @NotBlank(message = "pool.name.validate.empty")
    private String name;

    private Boolean active;
}
