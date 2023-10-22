package com.swimmingpool.ticket.impl.handler;

import com.swimmingpool.assignment.Assignment;
import com.swimmingpool.assignment.IAssignmentService;
import com.swimmingpool.course.Course;
import com.swimmingpool.course.ICourseService;
import com.swimmingpool.ticket.AbstractTicketHandler;
import com.swimmingpool.ticket.Ticket;
import com.swimmingpool.ticket.model.AdditionalInfo;
import com.swimmingpool.ticket.model.CloseCourseAdditionalInfo;
import com.swimmingpool.ticket.request.CreateTicketRequest;
import com.swimmingpool.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CloseCourseTicketHandler extends AbstractTicketHandler {

    private final IAssignmentService assignmentService;
    private final ICourseService courseService;

    @Override
    public void validateCreateTicket(CreateTicketRequest createTicketRequest) {
        Assignment byIdThrowIfNotPresent = this.assignmentService.findByIdThrowIfNotPresent(createTicketRequest.getObjectId());
        User user = this.userService.findByIdThrowIfNotPresent(byIdThrowIfNotPresent.getUserId());
        this.checkCreator(user.getUsername());
    }

    @Override
    @Transactional
    public void approve(Ticket ticket) {
        Assignment assignment = this.assignmentService.findByIdThrowIfNotPresent(ticket.getObjectId());
        assignment.setActive(false);
        this.assignmentService.save(assignment);
        super.approve(ticket);
    }

    @Override
    public AdditionalInfo getAdditionalInfo(String objectId) {
        Assignment assignment = this.assignmentService.findByIdThrowIfNotPresent(objectId);
        Course course = this.courseService.findByIdThrowIfNotPresent(assignment.getCourseId());
        CloseCourseAdditionalInfo closeCourseAdditionalInfo = new CloseCourseAdditionalInfo();
        closeCourseAdditionalInfo.setNumberLesson(course.getNumberOfLesson());
        closeCourseAdditionalInfo.setCourseCode(course.getCode());
        closeCourseAdditionalInfo.setCourseName(course.getName());
        closeCourseAdditionalInfo.setStartDate(assignment.getStartDate());
        closeCourseAdditionalInfo.setStartTime(assignment.getStartTime());
        closeCourseAdditionalInfo.setEndTime(assignment.getEndTime());
        return closeCourseAdditionalInfo;
    }
}