package com.swimmingpool.exception;

import com.swimmingpool.common.constant.AppConstant;
import com.swimmingpool.common.dto.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({AuthenticationException.class})
    public String handleAuthenticationException(AuthenticationException ex, Model model) {
        log.error(ex.getMessage(), ex);
        Result<Object> fail = Result.fail(null, "Tên đăng nhập hoặc mật khẩu không chính xác.");
        model.addAttribute(AppConstant.Handler.RESULT_KEY, fail);
        return "login";
    }

    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(BusinessException ex, RedirectAttributes redirectAttributes) {
        log.error(ex.getMessage(), ex);
        Result<Object> fail = Result.fail(ex.getData(), ex.getMessage());
        redirectAttributes.addFlashAttribute(AppConstant.Handler.RESULT_KEY, fail);
        if (Objects.nonNull(ex.getData())) {
            ex.getData().forEach(redirectAttributes::addFlashAttribute);
        }
        return ex.getRedirectUrl();
    }

    @ExceptionHandler(BindException.class)
    public String handleBindException(BindException ex, RedirectAttributes redirectAttributes, HttpServletRequest req) {
        log.error(ex.getMessage(), ex);
        String messages = ex.getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("</br>"));
        Result<Object> fail = Result.fail(ex.getTarget(), messages);
        redirectAttributes.addFlashAttribute(AppConstant.Handler.RESULT_KEY, fail);
        String referer = req.getHeader("referer");
        return "redirect:" + referer;
    }
}
