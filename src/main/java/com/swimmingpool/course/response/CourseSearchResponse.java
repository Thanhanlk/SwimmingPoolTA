package com.swimmingpool.course.response;

import lombok.Data;

import java.util.Date;

@Data
public class CourseSearchResponse extends CourseResponse {
    private String avatar;
    private String slug;
    private Integer numberOfLesson;
    private Integer discount;
    private Date createdDate;
    private Date modifiedDate;

    public String getImage() {
        return this.avatar;
    }
}
