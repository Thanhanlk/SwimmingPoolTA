package com.swimmingpool.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConstant {

    public static final BigDecimal ZERO = new BigDecimal("0");
    public static final int DATE_INDICATE_NEWEST = 30;
    public static final int PERCENT_INDICATE_HOT_SALE = 35;
    public static final int NUMBER_BEST_SELLER_SHOW = 8;
    public static final int NUMBER_IMAGE_SHOW_HOME = 3; // UI only shows three images

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Validation {
        public static final String RGX_VIETNAM_PHONE = "^(0|\\+84)\\d{9}";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Endpoint {
        public static final String HOME = "/home";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class RequestKey {
        public static final String REDIRECT_URL = "_REDIRECT_URL_KEY";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ResponseKey {
        public static final String RESULT = "_RESULT_KEY";
        public static final String PAGING = "_PAGING_KEY";
        public static final String JS = "_JS_KEY";
        public static final String CSS = "_CSS_KEY";
    }
}
