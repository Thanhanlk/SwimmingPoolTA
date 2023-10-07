package com.swimmingpool.course.request;

import com.swimmingpool.common.dto.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.DayOfWeek;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CourseSearchRequest extends PageRequest {
    private String codeName;
    private Boolean active;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private DayOfWeek dayOfWeek;
}
