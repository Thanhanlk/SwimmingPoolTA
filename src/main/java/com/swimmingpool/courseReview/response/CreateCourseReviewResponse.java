package com.swimmingpool.courseReview.response;

import com.swimmingpool.courseReview.CourseReview;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCourseReviewResponse {
    private CourseReview courseReview;
}
