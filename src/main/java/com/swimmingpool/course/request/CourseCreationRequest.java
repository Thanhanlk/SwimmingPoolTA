package com.swimmingpool.course.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CourseCreationRequest {

    @NotBlank(message = "{course.code.validate.empty}")
    private String code;

    @NotBlank(message = "{course.name.validate.empty}")
    private String name;

    @NotNull(message = "{course.price.validate.empty}")
    @Min(value = 1, message = "{course.price.validate.min}")
    private BigDecimal price;

    @NotNull(message = "{course.numberOfLesson.validate.empty}")
    @Min(value = 1, message = "{course.numberOfLesson.validate.min}")
    private Integer numberOfLesson;

    @NotNull(message = "{course.numberOfStudent.validate.empty}")
    @Min(value = 1, message = "{course.numberOfStudent.validate.min}")
    private Integer numberOfStudent;

    @NotBlank(message = "{course.short-description.validate.empty}")
    @Size(max = 200, message = "{course.short-description.validate.max}")
    private String shortDescription;

    @NotBlank(message = "{course.description.validate.empty}")
    private String description;

    @NotBlank(message = "{pool.id.validate.empty}")
    private String poolId;

    @NotNull(message = "{course.avatar.validate.empty}")
    private MultipartFile avatar;

    private List<MultipartFile> images;
}
