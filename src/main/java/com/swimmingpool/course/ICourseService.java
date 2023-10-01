package com.swimmingpool.course;

import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.dto.Result;
import com.swimmingpool.common.exception.BusinessException;
import com.swimmingpool.common.exception.ValidationException;
import com.swimmingpool.course.request.CourseCreationRequest;
import com.swimmingpool.course.request.CourseSearchRequest;
import com.swimmingpool.course.response.CourseCreationResponse;
import com.swimmingpool.course.response.CourseSearchResponse;

import java.io.IOException;
import java.util.List;

public interface ICourseService {

    List<Course> findActiveCourse();

    Course findByIdThrowIfNotPresent(String id) throws ValidationException;

    CourseCreationResponse saveCourse(CourseCreationRequest courseCreationRequest) throws IOException;

    PageResponse<CourseSearchResponse> searchCourse(CourseSearchRequest courseSearchRequest);

    void changeStatus(String id);
}
