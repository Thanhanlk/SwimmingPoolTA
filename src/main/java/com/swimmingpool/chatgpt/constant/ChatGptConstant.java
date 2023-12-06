package com.swimmingpool.chatgpt.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatGptConstant {

    public static final String CHAT_HIS_KEY = "CHAT_HIS_SESSION_KEY";
    public static final String ENDPOINT_FOR_CHAT_COMPLETION = "/v1/chat/completions";
}
