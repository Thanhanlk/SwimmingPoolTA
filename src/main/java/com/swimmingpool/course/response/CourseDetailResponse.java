package com.swimmingpool.course.response;

import com.swimmingpool.assignment.response.AvailableAssignmentResponse;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class CourseDetailResponse extends CourseResponse {
    private Integer discount;
    private List<CourseSearchResponse> relatedProducts;
    private List<AvailableAssignmentResponse> assignments;
}
