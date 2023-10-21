package com.swimmingpool.timetable;

import com.swimmingpool.common.controller.BaseController;
import com.swimmingpool.timetable.response.TimeTableWrapper;
import com.swimmingpool.timetable.response.TimetableResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherTimeTableController extends BaseController {

    private final ITimetableService timetableService;

    @GetMapping("/time-table")
    public String index(Model model) {
        List<TimetableResponse> teacherTimetable = this.timetableService.getTeacherTimetable();
        TimeTableWrapper timeTableWrapper = new TimeTableWrapper(teacherTimetable);
        model.addAttribute("numberRow", timeTableWrapper.getNumberRow());
        model.addAttribute("timetables", timeTableWrapper.getData());
        this.addJavascript(model, "/admin/javascript/column-controller");
        this.addCss(model, "/admin/css/column-controller", "/admin/css/user");
        return "/teacher/pages/timetable/index";
    }
}
