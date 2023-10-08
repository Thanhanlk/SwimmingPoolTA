package com.swimmingpool.cart.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.DayOfWeek;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private String cartId;
    private String courseId;
    private String courseCode;
    private String courseName;
    private BigDecimal price;
    private Integer discount;
    private String courseImage;
    private DayOfWeek dayOfWeek;
    private Time startTime;
    private Time endTime;
    private Date startDate;
    private String slug;
    private String assignmentId;
}