package com.swimmingpool.common.interceptor;

import com.swimmingpool.cart.ICartService;
import com.swimmingpool.cart.response.CartResponse;
import com.swimmingpool.user.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommonInterceptor implements HandlerInterceptor {

    private final IUserService userService;
    private final ICartService cartService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (Objects.nonNull(modelAndView)) {
            this.getCardSize(request);
            request.setAttribute("currentUri", request.getServletPath());
            request.setAttribute("uriWithQueryString", this.getUriWithParameters(request));
        }
    }

    private void getCardSize(HttpServletRequest request) {
        try {
            this.userService.getCurrentUserThrowIfNot();
            List<CartResponse> myCart = this.cartService.getMyCart();
            request.setAttribute("cartSize", myCart.size());
        } catch (Exception ignore) {}
    }

    private String getUriWithParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        return request.getRequestURI() + "?x=" + parameterMap.keySet().stream()
                .filter(key -> !"page".equals(key) || !"x".equals(key))
                .map(key -> key + "=" + String.join(",", parameterMap.get(key)))
                .collect(Collectors.joining("&"));
    }
}
