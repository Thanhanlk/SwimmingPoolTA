package com.swimmingpool.course.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CourseResponse {
    private String id;
    private String code;
    private String name;
    private String image;
    private String shortDescription;
    private String description;
    private Boolean active;
    private Boolean isShowHome;
    private BigDecimal price;
    private String slug;
    private Integer avgStar;
    private int numberOfReview;
}
