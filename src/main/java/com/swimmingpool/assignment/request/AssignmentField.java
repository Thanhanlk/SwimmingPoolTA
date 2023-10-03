package com.swimmingpool.assignment.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.DayOfWeek;
import java.util.Date;

@Data
public class AssignmentField {

    private String id;

    @NotNull(message = "assignment.day-of-week.validate.empty")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "assignment.start-time.validate.empty")
    private String startTime;

    @NotNull(message = "assignment.end-time.validate.empty")
    private String endTime;

    @NotBlank(message = "pool.id.validate.empty")
    private String poolId;

    @NotBlank(message = "course.id.validate.empty")
    private String courseId;

    @NotNull(message = "assignment.start-date.validate.empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
}
