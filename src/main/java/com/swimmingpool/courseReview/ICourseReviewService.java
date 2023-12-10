package com.swimmingpool.courseReview;

import com.swimmingpool.courseReview.request.CreateCourseReviewRequest;
import com.swimmingpool.courseReview.request.UpdateCourseReviewRequest;
import com.swimmingpool.courseReview.response.CourseReviewDto;
import com.swimmingpool.courseReview.response.CreateCourseReviewResponse;

import java.util.List;

public interface ICourseReviewService {

    List<CourseReviewDto> findByCourseCode(String courseCode);
    CreateCourseReviewResponse save(CreateCourseReviewRequest request);
    CreateCourseReviewResponse update(UpdateCourseReviewRequest request);
}
