package com.swimmingpool.course;

import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.exception.ValidationException;
import com.swimmingpool.course.request.CourseCreationRequest;
import com.swimmingpool.course.request.CourseSearchRequest;
import com.swimmingpool.course.response.*;

import java.io.IOException;
import java.util.List;

public interface ICourseService {

    void save(Course course);

    List<Course> findActiveCourse();

    Course findByIdThrowIfNotPresent(String id) throws ValidationException;

    CourseCreationResponse saveCourse(CourseCreationRequest courseCreationRequest) throws IOException;

    PageResponse<CourseSearchResponse> searchCourse(CourseSearchRequest courseSearchRequest);

    void changeStatus(String id);

    void deleteCourse(String id);

    List<CourseImageDTO> getCourseImage(int limit);

    List<CourseResponse> getBestSeller(int limit);

    List<CourseResponse> getNewest(int limit);

    List<CourseResponse> getHotSales(int limit);

    CourseDetailResponse getDetailProduct(String courseCode, int limitRelatedCourse);
}
