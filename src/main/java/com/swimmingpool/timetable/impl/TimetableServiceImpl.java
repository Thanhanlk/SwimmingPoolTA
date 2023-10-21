package com.swimmingpool.timetable.impl;

import com.swimmingpool.timetable.ITimetableService;
import com.swimmingpool.timetable.response.TimetableResponse;
import com.swimmingpool.user.IUserService;
import com.swimmingpool.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimetableServiceImpl implements ITimetableService {

    private final CustomTimetableRepository customTimetableRepository;
    private final IUserService userService;

    @Override
    public List<TimetableResponse> getUserTimetable() {
        UserResponse userResponse = this.userService.getCurrentUserThrowIfNot();
        return this.customTimetableRepository.getUserTimetable(userResponse.getUsername());
    }

    @Override
    public List<TimetableResponse> getTeacherTimetable() {
        UserResponse userResponse = this.userService.getCurrentUserThrowIfNot();
        return this.customTimetableRepository.getTeacherTimeTeacher(userResponse.getUsername());
    }
}
