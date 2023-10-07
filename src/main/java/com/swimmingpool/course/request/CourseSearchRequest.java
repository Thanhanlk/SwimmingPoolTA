package com.swimmingpool.course.request;

import com.swimmingpool.common.dto.PageRequest;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseSearchRequest extends PageRequest {
    private String codeName;
    private Boolean active;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
