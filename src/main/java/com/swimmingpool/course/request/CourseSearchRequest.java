package com.swimmingpool.course.request;

import com.swimmingpool.common.dto.PageRequest;
import lombok.Data;

@Data
public class CourseSearchRequest extends PageRequest {
    private String codeName;
}
