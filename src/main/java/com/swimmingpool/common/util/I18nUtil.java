package com.swimmingpool.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class I18nUtil {

    private static MessageSource messageSource;

    @Autowired
    public I18nUtil(MessageSource messageSource) {
        I18nUtil.messageSource = messageSource;
    }

    public static String getMessage(String key, Object...param) {
        return messageSource.getMessage(key, param, LocaleContextHolder.getLocale());
    }
}
