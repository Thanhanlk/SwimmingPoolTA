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
