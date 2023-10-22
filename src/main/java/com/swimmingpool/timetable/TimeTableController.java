package com.swimmingpool.timetable;

import com.swimmingpool.timetable.response.TimeTableWrapper;
import com.swimmingpool.timetable.response.TimetableResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/time-table")
@RequiredArgsConstructor
public class TimeTableController {

    private final ITimetableService timetableService;

    @GetMapping
    public String index(Model model) {
        List<TimetableResponse> userTimetable = this.timetableService.getUserTimetable();
        TimeTableWrapper timeTableWrapper = new TimeTableWrapper(userTimetable);
        model.addAttribute("timetables", timeTableWrapper.getData());
        model.addAttribute("numberRow", timeTableWrapper.getNumberRow());
        return "/user/pages/timetable/index";
    }
}
