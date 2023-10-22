package com.swimmingpool.validation.annotation;

import com.swimmingpool.validation.handler.RepeatPasswordHandler;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = RepeatPasswordHandler.class
)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatPassword {
    String[] fields() default {"password", "repeatPassword"};
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
