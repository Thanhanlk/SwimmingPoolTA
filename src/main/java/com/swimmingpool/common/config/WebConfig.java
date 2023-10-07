package com.swimmingpool.common.config;

import com.swimmingpool.common.interceptor.CommonInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Configuration
@RequiredArgsConstructor
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    private static final Locale VN = new Locale("vi", "VN");

    private final CommonInterceptor commonInterceptor;

    @PostConstruct
    public void init() {
        LocaleContextHolder.setDefaultLocale(VN);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.commonInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/resources/**")
                .order(0);

        registry.addInterceptor(this.localeChangeInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/resources/**")
                .order(0);
    }

    @Bean
    @Primary
    public LocaleResolver localeResolver() {
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(VN);
        cookieLocaleResolver.setCookieSecure(true);
        cookieLocaleResolver.setCookieHttpOnly(true);
        return cookieLocaleResolver;
    }

    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }
}
