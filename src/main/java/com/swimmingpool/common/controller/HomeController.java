package com.swimmingpool.common.controller;

import com.swimmingpool.user.UserConstant;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .filter(x -> x.getAuthority().equals(UserConstant.Role.ADMIN.name()))
                .findFirst()
                .map(x -> "/admin/pages/home/index")
                .orElse("/user/pages/home/index");
    }
}
