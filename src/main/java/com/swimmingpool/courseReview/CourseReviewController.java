package com.swimmingpool.courseReview;

import com.swimmingpool.common.constant.AppConstant;
import com.swimmingpool.common.dto.Result;
import com.swimmingpool.common.exception.BusinessException;
import com.swimmingpool.common.util.I18nUtil;
import com.swimmingpool.course.Course;
import com.swimmingpool.courseReview.request.CreateCourseReviewRequest;
import com.swimmingpool.courseReview.request.UpdateCourseReviewRequest;
import com.swimmingpool.courseReview.response.CreateCourseReviewResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/course-review")
@RequiredArgsConstructor
public class CourseReviewController {

    private final ICourseReviewService courseReviewService;

    @PostMapping
    public String save(@Valid CreateCourseReviewRequest request, RedirectAttributes redirectAttributes) {
        try {
            final CreateCourseReviewResponse response = this.courseReviewService.save(request);
            final Course course = response.getCourseReview().getCourse();
            redirectAttributes.addFlashAttribute(AppConstant.ResponseKey.RESULT, Result.success(null, I18nUtil.getMessage("course-review.create.success", course.getName())));
            return String.format("redirect:/course/%s?courseCode=%s", course.getSlug(), course.getCode());
        } catch (BusinessException ex) {
            throw new BusinessException(ex.getMessage(), "/home");
        }
    }

    @PostMapping("/update")
    public String update(@Valid UpdateCourseReviewRequest request, RedirectAttributes redirectAttributes) {
        try {
            final CreateCourseReviewResponse response = this.courseReviewService.update(request);
            final Course course = response.getCourseReview().getCourse();
            redirectAttributes.addFlashAttribute(AppConstant.ResponseKey.RESULT, Result.success(null, I18nUtil.getMessage("course-review.update.success", course.getName())));
            return String.format("redirect:/course/%s?courseCode=%s", course.getSlug(), course.getCode());
        } catch (BusinessException ex) {
            throw new BusinessException(ex.getMessage(), "/home");
        }
    }
}
