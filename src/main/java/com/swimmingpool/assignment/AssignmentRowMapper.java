package com.swimmingpool.assignment;

import com.swimmingpool.assignment.response.AssignmentCreationResponse;
import jakarta.persistence.Tuple;
import lombok.experimental.UtilityClass;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.Date;
import java.util.function.Function;

@UtilityClass
public class AssignmentRowMapper {

    public Function<Tuple, AssignmentCreationResponse> assignmentSearchRowMapper() {
        return tuple -> {
            Integer dayOfWeek = tuple.get("dayOfWeek", Integer.class);
            AssignmentCreationResponse assignmentCreationResponse = new AssignmentCreationResponse();
            assignmentCreationResponse.setId(tuple.get("id", String.class));
            assignmentCreationResponse.setUsername(tuple.get("username", String.class));
            assignmentCreationResponse.setFullName(tuple.get("fullName", String.class));
            assignmentCreationResponse.setDay(DayOfWeek.of(dayOfWeek).name());
            assignmentCreationResponse.setStartTime(tuple.get("startTime", Time.class));
            assignmentCreationResponse.setEndTime(tuple.get("endTime", Time.class));
            assignmentCreationResponse.setCourseName(tuple.get("courseName", String.class));
            assignmentCreationResponse.setPoolName(tuple.get("poolName", String.class));
            assignmentCreationResponse.setCreatedDate(tuple.get("createdDate", Date.class));
            assignmentCreationResponse.setModifiedDate(tuple.get("modifiedDate", Date.class));
            assignmentCreationResponse.setUserId(tuple.get("userId", String.class));
            assignmentCreationResponse.setStartDate(tuple.get("startDate", Date.class));
            return assignmentCreationResponse;
        };
    }
}
