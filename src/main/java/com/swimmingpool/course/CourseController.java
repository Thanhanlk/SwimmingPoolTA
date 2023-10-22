package com.swimmingpool.course;

import com.swimmingpool.common.constant.AppConstant;
import com.swimmingpool.common.controller.BaseController;
import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.course.request.CourseSearchRequest;
import com.swimmingpool.course.response.CourseDetailResponse;
import com.swimmingpool.course.response.CourseSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        searchRequest.setPageSize(12);
        model.addAttribute("searchRequest", searchRequest);
        PageResponse<CourseSearchResponse> pageResponse = this.courseService.searchCourse(searchRequest);
        this.addPagingResult(model, pageResponse);
        this.addCss(model, "/user/css/shop", "/user/css/product", "/user/css/paging");
        return "user/pages/course/index";
    }

    @GetMapping(value = "/{slug}", params = {"courseCode"})
    public String detail(@PathVariable String slug, @RequestParam String courseCode, Model model) {
        CourseDetailResponse courseDetailResponse = this.courseService.getDetailProduct(courseCode, AppConstant.NUMBER_BEST_SELLER_SHOW);
        if (!slug.equals(courseDetailResponse.getSlug())) {
            return "redirect:/course/" + courseDetailResponse.getSlug() + "?courseCode=" + courseCode;
        }
        PageResponse<CourseSearchResponse> pageResponse = this.courseService.searchCourse(CourseSearchRequest.builder()
                .active(true)
                .page(1)
                .pageSize(4)
                .build());
        model.addAttribute("relatedCourse", pageResponse.getItems());
        model.addAttribute("course", courseDetailResponse);
        this.addCss(model, "/user/css/shop", "/user/css/product", "/user/css/product-detail",  "/user/css/hero");
        this.addJavascript(model, "/user/javascript/product-detail");
        return "user/pages/course/detail";
    }
}
