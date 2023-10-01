package com.swimmingpool.assignment.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AssignmentCreationRequest {

    @NotBlank(message = "user.id.validate.empty")
    private String userId;

    @Valid
    private List<AssignmentField> assignmentFields;

    public AssignmentCreationRequest() {
        this.userId = null;
        this.assignmentFields = new ArrayList<>();
        this.assignmentFields.add(new AssignmentField());
    }
}
