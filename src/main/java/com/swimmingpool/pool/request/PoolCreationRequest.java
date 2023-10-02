package com.swimmingpool.pool.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PoolCreationRequest {

    private String id;

    @NotBlank(message = "pool.code.validate.empty")
    private String code;

    @NotBlank(message = "pool.name.validate.empty")
    private String name;

    @NotNull(message = "pool.number-of-student.validate.empty")
    @Min(value = 1, message = "pool.number-of-student.validate.min")
    private Integer numberOfStudent;

    private Boolean active;
}
