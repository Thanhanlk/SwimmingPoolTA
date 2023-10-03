package com.swimmingpool.assignment.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public void addAssignmentField(AssignmentField assignmentField) {
        if (Objects.isNull(assignmentField)) return;
        if (Objects.isNull(this.assignmentFields)) this.assignmentFields = new ArrayList<>();
        this.assignmentFields.add(assignmentField);
    }

    public void deleteAssignmentField(int index) {
        if (Objects.isNull(this.assignmentFields)) return;
        this.assignmentFields.remove(index);
    }

    public AssignmentField getAssignmentField(int index) {
        if (Objects.isNull(this.assignmentFields)) return null;
        return this.assignmentFields.get(index);
    }
}
