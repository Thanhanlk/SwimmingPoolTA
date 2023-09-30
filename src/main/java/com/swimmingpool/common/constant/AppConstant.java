package com.swimmingpool.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConstant {

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
