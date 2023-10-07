package com.swimmingpool.course.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseImageDTO {
    private String id;
    private String name;
    private String code;
    private String imageUrl;
    private String slug;
}
