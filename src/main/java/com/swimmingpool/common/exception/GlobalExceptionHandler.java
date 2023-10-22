package com.swimmingpool.common.exception;

import com.swimmingpool.common.constant.AppConstant;
import com.swimmingpool.common.dto.Result;
import com.swimmingpool.common.util.I18nUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({AuthenticationException.class})
    public String handleAuthenticationException(AuthenticationException ex, Model model) {
        log.error(ex.getMessage(), ex);
        Result<Object> fail = Result.fail(null, I18nUtil.getMessage("user.login.fail"));
        model.addAttribute(AppConstant.ResponseKey.RESULT, fail);
        return "login";
    }

    @ExceptionHandler({DisabledException.class})
    public String handleDisabledException(DisabledException ex, Model model) {
        log.error(ex.getMessage(), ex);
        Result<Object> fail = Result.fail(null, I18nUtil.getMessage("user.login.locked"));
        model.addAttribute(AppConstant.ResponseKey.RESULT, fail);
        return "login";
    }

    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(BusinessException ex,HttpServletRequest request, RedirectAttributes redirectAttributes) {
        log.error(ex.getMessage(), ex);
        Result<Object> fail = Result.fail(ex.getData(), ex.getMessage());
        redirectAttributes.addFlashAttribute(AppConstant.ResponseKey.RESULT, fail);
        if (Objects.nonNull(ex.getData())) {
            ex.getData().forEach((key, value) -> redirectAttributes.addFlashAttribute(key, value));
        }
        String redirectUrl = request.getParameter(AppConstant.RequestKey.REDIRECT_URL);
        if (StringUtils.hasLength(ex.getRedirectUrl())) return ex.getRedirectUrl();
        if (StringUtils.hasLength(redirectUrl)) return redirectUrl;
        return "redirect:" + AppConstant.Endpoint.HOME;
    }

    @ExceptionHandler(BindException.class)
    public String handleBindException(BindException ex, RedirectAttributes redirectAttributes, HttpServletRequest req) {
        log.error(ex.getMessage(), ex);
        String messages = ex.getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .map(I18nUtil::getMessage)
                .collect(Collectors.joining("</br>"));
        Result<Object> fail = Result.fail(ex.getTarget(), messages);
        redirectAttributes.addFlashAttribute(AppConstant.ResponseKey.RESULT, fail);
        String referer = req.getHeader("referer");
        String redirectUrl = req.getParameter(AppConstant.RequestKey.REDIRECT_URL);
        if (StringUtils.hasLength(redirectUrl)) return redirectUrl;
        return "redirect:" + referer;
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleUnKnowException(Exception ex, Model model, HttpServletRequest request) {
        log.error(ex.getMessage(), ex);
        String redirectUrl = request.getParameter(AppConstant.RequestKey.REDIRECT_URL);
        if (StringUtils.hasLength(redirectUrl)) {
            model.addAttribute("goBack", redirectUrl);
        } else {
            model.addAttribute("goBack", request.getHeader("referer"));
        }
        model.addAttribute("home", AppConstant.Endpoint.HOME);
        model.addAttribute("error", ex.getMessage());
        return "error/500";
    }
}
