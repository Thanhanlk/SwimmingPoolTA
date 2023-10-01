package com.swimmingpool.assignment.response;

import lombok.Data;

import java.util.List;

@Data
public class AssignmentSearchResponse {
    private String userName;
    private String fullName;
    private List<AssignmentCreationResponse> assignments;
}
