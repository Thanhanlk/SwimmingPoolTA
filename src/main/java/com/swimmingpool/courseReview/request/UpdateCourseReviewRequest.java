package com.swimmingpool.courseReview.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCourseReviewRequest {

    @NotBlank
    private String content;

    @NotNull
    private Long courseReviewId;

    @NotNull
    private Integer star;
}
