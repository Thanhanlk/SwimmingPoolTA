package com.swimmingpool.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConstant {

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
        public static String RESULT = "_RESULT_KEY";
        public static String PAGING = "_PAGING_KEY";
    }
}
