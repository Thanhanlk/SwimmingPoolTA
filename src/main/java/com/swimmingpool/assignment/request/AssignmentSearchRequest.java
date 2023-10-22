package com.swimmingpool.assignment.request;

import com.swimmingpool.common.dto.PageRequest;
import lombok.Data;

import java.sql.Time;
import java.time.DayOfWeek;

@Data
public class AssignmentSearchRequest extends PageRequest {
    private String codeNameUser;
    private DayOfWeek day;
    private String startTime;
    private String endTime;
    private String poolId;
    private String courseId;
}
