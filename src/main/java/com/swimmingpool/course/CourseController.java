package com.swimmingpool.course;

import com.swimmingpool.common.controller.BaseController;
import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.course.request.CourseSearchRequest;
import com.swimmingpool.course.response.CourseSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController extends BaseController {

    private final ICourseService courseService;

    @GetMapping
    public String index(CourseSearchRequest searchRequest, Model model) {
        if (searchRequest == null) {
            searchRequest = new CourseSearchRequest();
        }
        searchRequest.setActive(true);
        model.addAttribute("searchRequest", new CourseSearchRequest());
        PageResponse<CourseSearchResponse> pageResponse = this.courseService.searchCourse(searchRequest);
        this.addPagingResult(model, pageResponse);
        this.addCss(model, "/user/css/shop", "/user/css/product", "/user/css/paging");
        return "user/pages/course/index";
    }
}
