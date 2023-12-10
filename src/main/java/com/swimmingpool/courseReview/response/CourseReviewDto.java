package com.swimmingpool.courseReview.response;

import lombok.Data;

import java.util.Date;

@Data
public class CourseReviewDto {
    private Long id;
    private String username;
    private Date createdDate;
    private String content;
    private Integer star;
    private String createdName;
    private Date modifiedDate;
    private String courseId;
    private boolean updated;
}
