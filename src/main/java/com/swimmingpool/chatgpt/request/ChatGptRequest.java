package com.swimmingpool.chatgpt.request;

import lombok.Data;

import java.util.List;

@Data
public class ChatGptRequest {
    private String model;
    private List<Message> messages;
    private Boolean stream;
}
