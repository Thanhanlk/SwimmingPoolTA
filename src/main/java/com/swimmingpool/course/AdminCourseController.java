package com.swimmingpool.course;

import com.swimmingpool.common.constant.AppConstant;
import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.dto.Result;
import com.swimmingpool.course.request.CourseCreationRequest;
import com.swimmingpool.course.response.CourseCreationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/course")
@RequiredArgsConstructor
public class AdminCourseController {

    private final ICourseService courseService;

    @GetMapping
    public String index(Model model) {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPage(1);
        pageResponse.setPageSize(10);
        pageResponse.setTotal(100);
        pageResponse.setItems(List.of(1,2,3,4,5,6,7,8,89,1));
        model.addAttribute(AppConstant.ResponseKey.PAGING, pageResponse);
        return "admin/pages/course/index";
    }

    @GetMapping("/create")
    public String getCrateCourse(Model model) {
        model.addAttribute("courseCreation", new CourseCreationRequest());
        return "admin/pages/course/create";
    }

    @PostMapping("/create")
    public String postCreateCourse(@ModelAttribute @Valid CourseCreationRequest courseCreationRequest, RedirectAttributes redirectAttributes) {
        Result<CourseCreationResponse> result = this.courseService.saveCourse(courseCreationRequest);
        redirectAttributes.addFlashAttribute(AppConstant.ResponseKey.RESULT, result);
        if (!result.isSuccess()) {
            return "redirect:/admin/course/create";
        }
        return "redirect:/admin/course";
    }
}
