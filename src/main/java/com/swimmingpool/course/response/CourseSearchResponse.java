package com.swimmingpool.course.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CourseSearchResponse {
    private String id;
    private String code;
    private String name;
    private String avatar;
    private String slug;
    private BigDecimal price;
    private int numberOfLesson;
    private int discount;
    private Date createdDate;
    private Date modifiedDate;
    private Boolean active;
}
