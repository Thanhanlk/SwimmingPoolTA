package com.swimmingpool.assignment.impl;

import com.swimmingpool.assignment.Assignment;
import com.swimmingpool.assignment.AssignmentRepository;
import com.swimmingpool.assignment.IAssignmentService;
import com.swimmingpool.assignment.request.AssignmentCreationRequest;
import com.swimmingpool.assignment.request.AssignmentField;
import com.swimmingpool.assignment.request.AssignmentSearchRequest;
import com.swimmingpool.assignment.response.AssignmentCreationResponse;
import com.swimmingpool.assignment.response.AssignmentSearchResponse;
import com.swimmingpool.assignment.response.AvailableAssignmentResponse;
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
import org.springframework.util.CollectionUtils;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.*;
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
    public List<Assignment> findByPoolId(String poolId) {
        log.info("find assignment by pool-id: {}", poolId);
        return this.assignmentRepository.findByPoolId(poolId);
    }

    @Override
    public List<Assignment> findByIds(List<String> ids) {
        log.info("find assignment by ids: {}", ids);
        return this.assignmentRepository.findAllById(ids);
    }

    @Override
    public List<Assignment> findByCourseId(String courseId) {
        log.info("find assignment by course-id: {}", courseId);
        return this.assignmentRepository.findByCourseId(courseId);
    }

    @Override
    public List<AvailableAssignmentResponse> findAvailableAssignmentByCourseId(String courseId) {
        log.info("find active assignment by course-id: {}", courseId);
        return this.customAssignmentRepository.findAvailableAssignment(courseId);
    }

    @Override
    public Assignment findByIdThrowIfNotPresent(String id) {
        log.info("find assignment by id: {}", id);
        return this.assignmentRepository.findById(id)
                .orElseThrow(() -> new ValidationException("assignment.id.validate.not-exist", id));
    }

    @Override
    public List<Assignment> findByUserId(String userId) {
        return this.assignmentRepository.findByUserId(userId);
    }

    @Override
    public void save(Assignment assignment) {
        this.assignmentRepository.save(assignment);
    }

    @Override
    public PageResponse<AssignmentSearchResponse> searchAssignment(AssignmentSearchRequest request) {
        PageResponse<AssignmentCreationResponse> pageResponse = this.customAssignmentRepository.searchAssignment(request);
        List<AssignmentCreationResponse> items = pageResponse.getItems();
        Map<String, List<AssignmentCreationResponse>> assignmentGroup = items.stream()
                .collect(Collectors.groupingBy(AssignmentCreationResponse::getUserId, LinkedHashMap::new, Collectors.toList()));
        List<AssignmentSearchResponse> newItems = assignmentGroup.keySet().stream()
                .map(key -> {
                    List<AssignmentCreationResponse> assignmentCreationResponses = assignmentGroup.get(key);
                    AssignmentSearchResponse assignmentSearchResponse = new AssignmentSearchResponse();
                    if (!CollectionUtils.isEmpty(assignmentCreationResponses)) {
                        AssignmentCreationResponse a = assignmentCreationResponses.get(0);
                        assignmentSearchResponse.setUserName(a.getUsername());
                        assignmentSearchResponse.setFullName(a.getFullName());
                    }
                    assignmentSearchResponse.setUserId(key);
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
            Assignment assignment = new Assignment();
            this.saveAssignment(assignment, form, user);
        }
    }

    @Override
    @Transactional
    public void updateAssignment(AssignmentCreationRequest creationRequest) {
        log.info("update assignment: {}", creationRequest);
        User user = this.userService.findByIdThrowIfNotPresent(creationRequest.getUserId());
        List<String> ids = creationRequest.getAssignmentFields().stream()
                .map(AssignmentField::getId)
                .filter(Objects::nonNull)
                .toList();
        if (!CollectionUtils.isEmpty(ids)) {
            this.assignmentRepository.deleteAllNotIds(ids, creationRequest.getUserId());
        }
        Map<String, Assignment> assignmentByUserIdMap = this.findByUserId(creationRequest.getUserId()).stream()
                .collect(Collectors.toMap(Assignment::getId, v -> v));
        for (AssignmentField form : creationRequest.getAssignmentFields()) {
            Assignment assignment = assignmentByUserIdMap.getOrDefault(form.getId(), new Assignment());
            if (Objects.nonNull(assignment.getStartDate()) && assignment.getStartDate().before(new Date())) {
                continue;
            }
            this.saveAssignment(assignment, form, user);
        }
    }

    private Time[] validateBeforeCreateAssignment(AssignmentField form, User user) {
        if (form.getStartDate().before(new Date())) {
            throw new ValidationException("assignment.start-date.validate.past");
        }
        Time startTime = DateUtil.strToTime(form.getStartTime());
        Time endTime = DateUtil.strToTime(form.getEndTime());
        if (startTime.after(endTime)) {
            throw new ValidationException("assignment.start-time.validate.less-then-end-time");
        }
        this.courseService.findByIdThrowIfNotPresent(form.getCourseId());
        Pool pool = this.poolService.findByIdThrowIfNotPresent(form.getPoolId());
        List<Assignment> assignmentByPoolId = this.assignmentRepository.findByPoolIdAndStartTimeBetweenAndDayOfWeek(
                form.getPoolId(),
                startTime,
                endTime,
                form.getDayOfWeek().getValue()
        ).stream().filter(x -> !x.getId().equals(form.getId())).toList();
        if (!assignmentByPoolId.isEmpty()) {
            Assignment a = assignmentByPoolId.get(0);
            String name = DayOfWeek.of(a.getDayOfWeek()).name();
            throw new ValidationException(
                    "assignment.pool-id.validate.exist",
                    pool.getName(),
                    I18nUtil.getMessage("day-of-week." + name),
                    a.getStartTime().toString(),
                    a.getEndTime().toString()
            );
        }

        List<Assignment> assignmentByUserId = this.assignmentRepository.findByUserIdAndStartTimeBetweenAndDayOfWeek(
                user.getId(),
                startTime,
                endTime,
                form.getDayOfWeek().getValue()
        ).stream().filter(x -> !x.getId().equals(form.getId())).toList();
        if (!assignmentByUserId.isEmpty()) {
            Assignment a = assignmentByUserId.get(0);
            String name = DayOfWeek.of(a.getDayOfWeek()).name();
            throw new ValidationException(
                    "assignment.user-id.validate.exist",
                    user.getFullName(),
                    I18nUtil.getMessage("day-of-week." + name),
                    a.getStartTime().toString(),
                    a.getEndTime().toString(),
                    pool.getName()
            );
        }
        return new Time[]{startTime, endTime};
    }

    private void saveAssignment(Assignment assignment, AssignmentField form, User user) {
        Time[] times = this.validateBeforeCreateAssignment(form, user);
        assignment.setCourseId(form.getCourseId());
        assignment.setDayOfWeek(form.getDayOfWeek().getValue());
        assignment.setCourseId(form.getCourseId());
        assignment.setUserId(user.getId());
        assignment.setStartTime(times[0]);
        assignment.setEndTime(times[1]);
        assignment.setPoolId(form.getPoolId());
        assignment.setStartDate(form.getStartDate());
        assignment.setActive(true);
        this.assignmentRepository.save(assignment);
    }
}
