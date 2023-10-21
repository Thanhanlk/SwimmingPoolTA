package com.swimmingpool.assignment;

import com.swimmingpool.course.Course;
import com.swimmingpool.course.ICourseService;
import com.swimmingpool.order.response.TimetableResponse;
import com.swimmingpool.pool.IPoolService;
import com.swimmingpool.pool.Pool;
import com.swimmingpool.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/assignment")
@RequiredArgsConstructor
public class AssignmentController {

    private final IAssignmentService assignmentService;
    private final ICourseService courseService;
    private final IPoolService poolService;

    @GetMapping("/timetable")
    private String index(@AuthenticationPrincipal UserResponse userResponse, Model model) {
        List<TimetableResponse> assignments = this.assignmentService.findStartedByUserIdAndActive(userResponse.getId()).stream()
                .map(x -> {
                    Course course = this.courseService.findByIdThrowIfNotPresent(x.getCourseId());
                    Pool pool = this.poolService.findByIdThrowIfNotPresent(x.getPoolId());
                    return TimetableResponse.builder()
                            .assignmentId(x.getId())
                            .dayOfWeek(x.getDayOfWeek())
                            .startTime(x.getStartTime())
                            .endTime(x.getEndTime())
                            .courseCode(course.getCode())
                            .courseName(course.getName())
                            .poolName(pool.getName())
                            .poolCode(pool.getCode())
                            .build();
                })
                .toList();
        model.addAttribute("assignments", assignments);
        return "/user/pages/timetable/index";
    }
}
