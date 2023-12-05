package com.swimmingpool.chatgpt.request;

import com.swimmingpool.chatgpt.constant.Role;
import lombok.Data;

@Data
public class Message {
    private Role role;
    private String content;
}
