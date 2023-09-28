package com.swimmingpool.course;

import com.swimmingpool.common.dto.Result;
import com.swimmingpool.course.request.CourseCreationRequest;
import com.swimmingpool.course.response.CourseCreationResponse;

public interface ICourseService {
    Result<CourseCreationResponse> saveCourse(CourseCreationRequest courseCreationRequest);
}
