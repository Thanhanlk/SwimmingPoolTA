package com.swimmingpool.order.response;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.DayOfWeek;
import java.util.Date;

@Data
public class OrderSearchResponse {
    private String id;
    private String poolName;
    private String courseName;
    private DayOfWeek day;
    private Time startTime;
    private Time endTime;
    private String fullName;
    private String phone;
    private Date createdDate;
    private String methodPayment;
    private String status;
    private BigDecimal total;
    private Date startDate;
    private Integer discount;
}
