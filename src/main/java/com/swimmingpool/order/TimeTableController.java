package com.swimmingpool.order;

import com.swimmingpool.order.response.TimetableResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/time-table")
@RequiredArgsConstructor
public class TimeTableController {

    private final IOrderService orderService;

    @GetMapping
    public String index(Model model) {
        Map<Integer, List<TimetableResponse>> timetables = this.orderService.getTimetableCurrentUser().stream()
                .collect(Collectors.groupingBy(TimetableResponse::getDayOfWeek, LinkedHashMap::new, Collectors.toList()));
        Integer numberRow = timetables.keySet().stream()
                .map(timetables::get)
                .map(Collection::size)
                .max(Integer::compareTo)
                .orElse(0);
        model.addAttribute("timetables", timetables);
        model.addAttribute("numberRow", numberRow);
        return "/user/pages/timetable/index";
    }
}
