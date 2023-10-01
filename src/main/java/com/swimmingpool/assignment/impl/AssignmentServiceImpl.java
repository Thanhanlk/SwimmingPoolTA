package com.swimmingpool.assignment.impl;

import com.swimmingpool.assignment.Assignment;
import com.swimmingpool.assignment.AssignmentRepository;
import com.swimmingpool.assignment.IAssignmentService;
import com.swimmingpool.assignment.request.AssignmentCreationRequest;
import com.swimmingpool.assignment.request.AssignmentField;
import com.swimmingpool.assignment.request.AssignmentSearchRequest;
import com.swimmingpool.assignment.response.AssignmentCreationResponse;
import com.swimmingpool.assignment.response.AssignmentSearchResponse;
import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.exception.ValidationException;
import com.swimmingpool.common.util.DateUtil;
import com.swimmingpool.common.util.I18nUtil;
import com.swimmingpool.course.ICourseService;
import com.swimmingpool.pool.IPoolService;
import com.swimmingpool.pool.Pool;
import com.swimmingpool.user.IUserService;
import com.swimmingpool.user.User;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssignmentServiceImpl implements IAssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final CustomAssignmentRepository customAssignmentRepository;

    @Setter(onMethod_ = @Autowired)
    private IUserService userService;

    @Setter(onMethod_ = @Autowired)
    private ICourseService courseService;

    @Setter(onMethod_ = @Autowired)
    private IPoolService poolService;

    @Override
    public PageResponse<AssignmentSearchResponse> searchAssignment(AssignmentSearchRequest request) {
        PageResponse<AssignmentCreationResponse> pageResponse = this.customAssignmentRepository.searchAssignment(request);
        List<AssignmentCreationResponse> items = pageResponse.getItems();
        Map<String, List<AssignmentCreationResponse>> assignmentGroup = items.stream()
                .collect(Collectors.groupingBy(a -> a.getUsername() + "-" + a.getFullName(), LinkedHashMap::new, Collectors.toList()));
        List<AssignmentSearchResponse> newItems = assignmentGroup.keySet().stream()
                .map(key -> {
                    List<AssignmentCreationResponse> assignmentCreationResponses = assignmentGroup.get(key);
                    String[] name = key.split("-");
                    AssignmentSearchResponse assignmentSearchResponse = new AssignmentSearchResponse();
                    assignmentSearchResponse.setUserName(name[0]);
                    assignmentSearchResponse.setFullName(name[1]);
                    assignmentSearchResponse.setAssignments(assignmentCreationResponses);
                    return assignmentSearchResponse;
                }).toList();
        return new PageResponse<>(pageResponse, newItems);
    }

    @Transactional
    @Override
    public void saveAssignment(AssignmentCreationRequest creationRequest) {
        log.info("save assignment: {}", creationRequest);
        User user = this.userService.findByIdThrowIfNotPresent(creationRequest.getUserId());

        for (AssignmentField form : creationRequest.getAssignmentFields()) {
            this.courseService.findByIdThrowIfNotPresent(form.getCourseId());
            Pool pool = this.poolService.findByIdThrowIfNotPresent(form.getPoolId());

            Time startTime = DateUtil.strToTime(form.getStartTime());
            Time endTime = DateUtil.strToTime(form.getEndTime());

            Optional<Assignment> assignmentByPoolId = this.assignmentRepository.findByPoolIdAndStartTimeBetweenAndDayOfWeek(
                    form.getPoolId(),
                    startTime,
                    endTime,
                    form.getDayOfWeek().getValue()
            );
            if (assignmentByPoolId.isPresent()) {
                Assignment assignment = assignmentByPoolId.get();
                String name = DayOfWeek.of(assignment.getDayOfWeek()).name();
                throw new ValidationException(
                        "assignment.pool-id.validate.exist",
                        pool.getName(),
                        I18nUtil.getMessage("day-of-week." + name),
                        assignment.getStartTime(),
                        assignment.getEndTime()
                );
            }

            Optional<Assignment> assignmentByUserId = this.assignmentRepository.findByUserIdAndStartTimeBetweenAndDayOfWeek(
                    creationRequest.getUserId(),
                    startTime,
                    endTime,
                    form.getDayOfWeek().getValue()
            );
            if (assignmentByUserId.isPresent()) {
                Assignment assignment = assignmentByUserId.get();
                String name = DayOfWeek.of(assignment.getDayOfWeek()).name();
                throw new ValidationException(
                        "assignment.user-id.validate.exist",
                        user.getFullName(),
                        I18nUtil.getMessage("day-of-week." + name),
                        assignment.getStartTime().toString(),
                        assignment.getEndTime().toString(),
                        pool.getName()
                );
            }

            Assignment assignment = new Assignment();
            assignment.setActive(false);
            assignment.setCourseId(form.getCourseId());
            assignment.setDayOfWeek(form.getDayOfWeek().getValue());
            assignment.setCourseId(form.getCourseId());
            assignment.setUserId(creationRequest.getUserId());
            assignment.setEndTime(endTime);
            assignment.setStartTime(startTime);
            assignment.setPoolId(form.getPoolId());
            assignment = this.assignmentRepository.save(assignment);
        }
    }
}
