package com.swimmingpool.assignment.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssignmentCreationResponse {
    private String id;
    private String username;
    private String fullName;
    private String day;
    private Time startTime;
    private Time endTime;
    private String courseName;
    private String poolName;
    private Date createdDate;
    private Date modifiedDate;
    private String userId;
    private Date startDate;
}
