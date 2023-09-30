package com.swimmingpool.course;

import com.swimmingpool.common.constant.AppConstant;
import com.swimmingpool.common.controller.BaseController;
import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.dto.Result;
import com.swimmingpool.common.exception.BusinessException;
import com.swimmingpool.common.util.I18nUtil;
import com.swimmingpool.course.request.CourseCreationRequest;
import com.swimmingpool.course.request.CourseSearchRequest;
import com.swimmingpool.course.response.CourseCreationResponse;
import com.swimmingpool.course.response.CourseSearchResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin/course")
@RequiredArgsConstructor
public class AdminCourseController extends BaseController {

    private final ICourseService courseService;

    @GetMapping
    public String index(CourseSearchRequest courseSearchRequest, Model model) {
        PageResponse<CourseSearchResponse> pageResponse = this.courseService.searchCourse(courseSearchRequest);
        this.addPagingResult(model, pageResponse);
        this.addJavascript(model, "/admin/javascript/column-controller");
        this.addCss(model, "/admin/css/column-controller", "/admin/css/user");
        return "admin/pages/course/index";
    }

    @GetMapping("/create")
    public String getCrateCourse(Model model, HttpServletRequest req) {
        if (!model.containsAttribute("courseCreation")) {
            model.addAttribute("courseCreation", new CourseCreationRequest());
        }
        this.addJavascript(model, "/admin/javascript/ckeditor/ckeditor", "/admin/javascript/ckeditor");
        model.addAttribute("redirectUrl", req.getRequestURI());
        return "admin/pages/course/create";
    }

    @PostMapping
    public String postCreateCourse(
            @ModelAttribute("courseCreation") @Valid CourseCreationRequest courseCreationRequest,
            @RequestParam(name = AppConstant.RequestKey.REDIRECT_URL, required = false) String redirectUrl,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        try {
            CourseCreationResponse creationResponse = this.courseService.saveCourse(courseCreationRequest);
            Result<CourseCreationResponse> result = Result.success(creationResponse, I18nUtil.getMessage("course.create.success"));
            redirectAttributes.addFlashAttribute(AppConstant.ResponseKey.RESULT, result);
            return "redirect:/admin/course";
        } catch (BusinessException ex) {
            ex.setData(Map.of("courseCreation", courseCreationRequest));
            ex.setRedirectUrl("redirect:" + Optional.ofNullable(redirectUrl).orElse("/admin/course/create"));
            throw ex;
        }
    }

    @GetMapping("/update/{slug}")
    public String getUpdate(@RequestParam String id, Model model, HttpServletRequest req) {
        try {
            if (!model.containsAttribute("courseCreation")) {
                Course course = this.courseService.findByIdThrowIfNotPresent(id);
                CourseCreationRequest courseCreationRequest = new CourseCreationRequest();
                courseCreationRequest.setId(course.getId());
                courseCreationRequest.setCode(course.getCode());
                courseCreationRequest.setName(course.getName());
                courseCreationRequest.setDiscount(course.getDiscount());
                courseCreationRequest.setPrice(course.getPrice());
                courseCreationRequest.setNumberOfLesson(course.getNumberOfLesson());
                courseCreationRequest.setNumberOfStudent(course.getNumberOfStudent());
                courseCreationRequest.setShortDescription(course.getShortDescription());
                courseCreationRequest.setDescription(course.getDescription());
                courseCreationRequest.setSlug(course.getSlug());
                courseCreationRequest.setIsShowHome(course.getIsShowHome());
                model.addAttribute("courseCreation", courseCreationRequest);
            }

            model.addAttribute("redirectUrl", req.getRequestURI() + "?id=" + id);
            this.addJavascript(model, "/admin/javascript/ckeditor/ckeditor", "/admin/javascript/ckeditor");
            return "admin/pages/course/create";
        }catch (BusinessException ex) {
            ex.setRedirectUrl("redirect:/admin/course");
            throw ex;
        }
    }

    @GetMapping("/change-status")
    public String getChangeStatus(@RequestParam String id, RedirectAttributes redirectAttributes) {
        try {
            this.courseService.changeStatus(id);
            Result<Object> success = Result.success(null, I18nUtil.getMessage("course.status.update.success"));
            redirectAttributes.addFlashAttribute(AppConstant.ResponseKey.RESULT, success);
            return "redirect:/admin/course";
        } catch (BusinessException ex) {
            ex.setRedirectUrl("redirect:/admin/course");
            throw ex;
        }
    }
}
