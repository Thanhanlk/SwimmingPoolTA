package com.swimmingpool.common.controller;

import com.swimmingpool.common.constant.AppConstant;
import com.swimmingpool.course.ICourseService;
import com.swimmingpool.course.response.CourseImageDTO;
import com.swimmingpool.course.response.CourseResponse;
import com.swimmingpool.user.UserConstant;
import com.swimmingpool.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class HomeController extends BaseController {

    private final ICourseService courseService;

    @GetMapping(value = {AppConstant.Endpoint.HOME, "/"})
    public String home(@AuthenticationPrincipal UserResponse userResponse, Model model) {
        if (Objects.isNull(userResponse)) return this.userHome(model);
        return switch (userResponse.getRole()) {
            case USER -> this.userHome(model);
            case ADMIN -> "redirect:/admin/pool";
            case TEACHER -> "redirect:/teacher/time-table";
            default -> throw new UnsupportedOperationException("");
        };
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        this.addCss(model, "/user/css/contact", "/user/css/map");
        return "user/pages/contact/index";
    }

    private String userHome(Model model) {
        List<CourseImageDTO> productImage = this.courseService.getCourseImage(AppConstant.NUMBER_IMAGE_SHOW_HOME);
        List<CourseResponse> bestSeller = this.courseService.getBestSeller(AppConstant.NUMBER_BEST_SELLER_SHOW);
        List<CourseResponse> newest = this.courseService.getNewest(AppConstant.NUMBER_BEST_SELLER_SHOW);
        List<CourseResponse> hotSales = this.courseService.getHotSales(AppConstant.NUMBER_BEST_SELLER_SHOW);
        model.addAttribute("productImage", productImage);
        model.addAttribute("bestSeller", bestSeller);
        model.addAttribute("newest", newest);
        model.addAttribute("hotSales", hotSales);
        this.addCss(model, "/user/css/product", "/user/css/banner", "/user/css/hero");
        return "user/pages/home/index";
    }
}
