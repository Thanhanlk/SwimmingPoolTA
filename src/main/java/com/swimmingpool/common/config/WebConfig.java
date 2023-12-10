package com.swimmingpool.common.config;

import com.swimmingpool.common.interceptor.CommonInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Configuration
@RequiredArgsConstructor
@EnableWebMvc
@EnableAsync
public class WebConfig implements WebMvcConfigurer {
    private static final Locale VN = new Locale("vi", "VN");

    private final CommonInterceptor commonInterceptor;

    @PostConstruct
    public void init() {
        Locale.setDefault(VN);
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

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor(this.asyncExecutor());
    }

    @Bean
    public AsyncTaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("task-executor");
        taskExecutor.setCorePoolSize(10);
        return taskExecutor;
    }
}
