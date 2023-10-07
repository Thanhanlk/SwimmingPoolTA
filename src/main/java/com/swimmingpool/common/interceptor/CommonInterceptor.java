package com.swimmingpool.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CommonInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (Objects.nonNull(modelAndView)) {
            modelAndView.addObject("currentUri", request.getServletPath());
            modelAndView.addObject("uriWithQueryString", this.getUriWithParameters(request));
        }
    }

    private String getUriWithParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        return request.getRequestURI() + "?x=" + parameterMap.keySet().stream()
                .filter(key -> !"page".equals(key) || !"x".equals(key))
                .map(key -> key + "=" + String.join(",", parameterMap.get(key)))
                .collect(Collectors.joining("&"));
    }
}
