package com.swimmingpool.assignment.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.DayOfWeek;

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
}
