package com.swimmingpool.chatgpt.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ChatGptResponse {
    private String id;
    private String object;
    private Date created;
    private String model;
    private Usage usage;
    private List<Choice> choices;
}
