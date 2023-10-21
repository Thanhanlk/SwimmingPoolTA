package com.swimmingpool.timetable.response;

import lombok.Getter;
import lombok.NonNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class TimeTableWrapper {
    private int numberRow;
    private Map<Integer, List<TimetableResponse>> data;

    public TimeTableWrapper(@NonNull List<TimetableResponse> timetableResponses) {
        Map<Integer, List<TimetableResponse>> timetables = timetableResponses.stream()
                .collect(Collectors.groupingBy(TimetableResponse::getDayOfWeek, LinkedHashMap::new, Collectors.toList()));
        Integer numberRow = timetables.keySet().stream()
                .map(timetables::get)
                .map(Collection::size)
                .max(Integer::compareTo)
                .orElse(0);
        this.numberRow = numberRow;
        this.data = timetables;
    }
}
