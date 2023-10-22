package com.swimmingpool.assignment;

import com.swimmingpool.assignment.request.AssignmentCreationRequest;
import com.swimmingpool.assignment.request.AssignmentField;
import com.swimmingpool.assignment.request.AssignmentSearchRequest;
import com.swimmingpool.assignment.response.AssignmentSearchResponse;
import com.swimmingpool.assignment.response.AvailableAssignmentResponse;
import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.exception.ValidationException;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

public interface IAssignmentService {

    void save(Assignment assignment);

    List<Assignment> findByCourseId(String courseId);

    List<AvailableAssignmentResponse> findAvailableAssignmentByCourseId(String courseId);

    List<Assignment> findByPoolId(String poolId);

    List<Assignment> findByIds(List<String> ids);

    Assignment findByIdThrowIfNotPresent(String id) throws ValidationException;

    List<Assignment> findByUserId(String userId);

    default AssignmentCreationRequest convertToAssignmentCreationRequest(String userId) {
        List<AssignmentField> fields = this.findByUserId(userId).stream()
                .map(a -> {
                    AssignmentField assignmentField = new AssignmentField();
                    assignmentField.setId(a.getId());
                    assignmentField.setCourseId(a.getCourseId());
                    assignmentField.setEndTime(a.getEndTime().toString());
                    assignmentField.setStartTime(a.getStartTime().toString());
                    assignmentField.setPoolId(a.getPoolId());
                    assignmentField.setDayOfWeek(DayOfWeek.of(a.getDayOfWeek()));
                    assignmentField.setStartDate(a.getStartDate());
                    return assignmentField;
                })
                .toList();
        AssignmentCreationRequest assignmentCreationRequest = new AssignmentCreationRequest();
        assignmentCreationRequest.setUserId(userId);
        assignmentCreationRequest.setAssignmentFields(fields);
        return assignmentCreationRequest;
    }

    PageResponse<AssignmentSearchResponse> searchAssignment(AssignmentSearchRequest request);

    void saveAssignment(AssignmentCreationRequest creationRequest);

    void updateAssignment(AssignmentCreationRequest creationRequest);
}
