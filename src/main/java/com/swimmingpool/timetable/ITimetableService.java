package com.swimmingpool.timetable;

import com.swimmingpool.timetable.response.TimetableResponse;

import java.util.List;

public interface ITimetableService {

    List<TimetableResponse> getUserTimetable();

    List<TimetableResponse> getTeacherTimetable();
}
