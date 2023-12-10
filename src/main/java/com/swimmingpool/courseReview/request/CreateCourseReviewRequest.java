package com.swimmingpool.courseReview.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCourseReviewRequest {

    @NotBlank
    private String courseId;

    @NotBlank
    private String content;

    @NotNull
    private Integer star;

}
