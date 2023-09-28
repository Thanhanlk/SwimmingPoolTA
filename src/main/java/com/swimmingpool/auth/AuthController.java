package com.swimmingpool.auth;

import com.swimmingpool.common.constant.AppConstant;
import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.exception.BusinessException;
import com.swimmingpool.common.dto.Result;
import com.swimmingpool.user.IUserService;
import com.swimmingpool.user.request.UserRegisterRequest;
import com.swimmingpool.user.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;

    @GetMapping("/test")
    public String index(@RequestParam(required = false) String data, Model model) {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPage(1);
        pageResponse.setPageSize(10);
        pageResponse.setTotal(100);
        pageResponse.setItems(List.of(1,2,3,4,5,6,7,8,89,1));
        model.addAttribute(AppConstant.ResponseKey.PAGING, pageResponse);
        return "admin/pages/category/index";
    }

    @GetMapping
    public String getLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("userRegister", new UserRegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@ModelAttribute @Valid UserRegisterRequest registerRequest, RedirectAttributes redirectAttributes) {
        try {
            Result<UserResponse> result = this.userService.registerUser(registerRequest);
            redirectAttributes.addFlashAttribute(AppConstant.ResponseKey.RESULT, result);
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage(), "redirect:/auth/register", Map.of("userRegister", registerRequest));
        }
        return "redirect:/auth";
    }
}
