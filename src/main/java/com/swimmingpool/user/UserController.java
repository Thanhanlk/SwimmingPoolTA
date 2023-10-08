package com.swimmingpool.user;

import com.swimmingpool.common.constant.AppConstant;
import com.swimmingpool.common.dto.Result;
import com.swimmingpool.common.exception.BusinessException;
import com.swimmingpool.common.exception.ValidationException;
import com.swimmingpool.common.util.I18nUtil;
import com.swimmingpool.user.request.ChangePassword;
import com.swimmingpool.user.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping("/change-password")
    public String changePassword(@Valid ChangePassword changePassword, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String returnUrl = Optional.ofNullable(request.getHeader("referer"))
                .filter(StringUtils::hasLength)
                .map(x -> "redirect:" + x)
                .orElse("redirect:" + AppConstant.Endpoint.HOME);
        try {
            this.userService.changePassword(changePassword);
            Result<Object> result = Result.success(null, I18nUtil.getMessage("change-password.success"));
            redirectAttributes.addFlashAttribute(AppConstant.ResponseKey.RESULT, result);
            return returnUrl;
        } catch (BusinessException exception) {
            exception.setRedirectUrl(returnUrl);
            exception.setData(Map.of("changePassword", changePassword));
            throw exception;
        }
    }

    @PostMapping("/update")
    public String updateUser(
            @ModelAttribute UserResponse userUpdateRequest,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        if (!userDetails.getUsername().equals(userUpdateRequest.getUsername())) {
            throw new ValidationException("user.update.not-auth", "redirect:/home");
        }
        this.userService.updateUser(userUpdateRequest);
        Result<Object> success = Result.success(null, I18nUtil.getMessage("user.update.success"));
        redirectAttributes.addFlashAttribute(AppConstant.ResponseKey.RESULT, success);
        return Optional.ofNullable(request.getHeader("referer"))
                .filter(StringUtils::hasLength)
                .map(x -> "redirect:" + x)
                .orElse("redirect:" + AppConstant.Endpoint.HOME);
    }
}
