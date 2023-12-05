package com.swimmingpool.assignment.response;

import lombok.Data;

import java.sql.Time;
import java.time.DayOfWeek;

@Data
public class AvailableAssignmentResponse {
    private String id;
    private Time startTime;
    private Time endTime;
    private DayOfWeek dayOfWeek;
    private Long registerStudent;
    private Integer maxStudent;
    private String teacherName;
    private String poolName;
}
