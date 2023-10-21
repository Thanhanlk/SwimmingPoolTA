package com.swimmingpool.ticket.model;

import lombok.Data;

import java.sql.Time;
import java.util.Date;

@Data
public class CloseCourseAdditionalInfo extends AdditionalInfo {
    private String courseCode;
    private String courseName;
    private int numberLesson;
    private Time startTime;
    private Time endTime;
    private Date startDate;
}
