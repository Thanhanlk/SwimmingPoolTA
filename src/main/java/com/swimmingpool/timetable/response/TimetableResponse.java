package com.swimmingpool.timetable.response;

import lombok.Builder;
import lombok.Getter;

import java.sql.Time;

@Builder
@Getter
public class TimetableResponse {
    private String assignmentId;
    private int dayOfWeek;
    private Time startTime;
    private Time endTime;
    private String poolCode;
    private String poolName;
    private String courseCode;
    private String courseName;
    private String teacher;
    private String teacherUsername;
}
