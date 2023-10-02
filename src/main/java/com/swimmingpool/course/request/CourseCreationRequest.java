package com.swimmingpool.course.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class CourseCreationRequest {

    private String id;

    @NotBlank(message = "course.code.validate.empty")
    private String code;

    @NotBlank(message = "course.name.validate.empty")
    private String name;

    @NotNull(message = "course.price.validate.empty")
    @Min(value = 1, message = "course.price.validate.min")
    private BigDecimal price;

    @Max(value = 100, message = "course.discount.validate.max")
    private Integer discount;

    @NotNull(message = "course.number-of-lesson.validate.empty")
    @Min(value = 1, message = "course.number-of-lesson.validate.min")
    private Integer numberOfLesson;

    @NotBlank(message = "course.short-description.validate.empty")
    @Size(max = 200, message = "course.short-description.validate.max")
    private String shortDescription;

    @NotBlank(message = "course.description.validate.empty")
    private String description;

    @NotBlank(message = "course.slug.validate.empty")
    @Size(max = 255, message = "course.slug.validate.max")
    private String slug;

    private Boolean isShowHome = false;

    @NotNull(message = "course.avatar.validate.empty")
    private MultipartFile avatar;
}