package com.swimmingpool.assignment;

import com.swimmingpool.assignment.request.AssignmentCreationRequest;
import com.swimmingpool.assignment.request.AssignmentSearchRequest;
import com.swimmingpool.assignment.response.AssignmentSearchResponse;
import com.swimmingpool.common.dto.PageResponse;

public interface IAssignmentService {

    PageResponse<AssignmentSearchResponse> searchAssignment(AssignmentSearchRequest request);

    void saveAssignment(AssignmentCreationRequest creationRequest);
}
