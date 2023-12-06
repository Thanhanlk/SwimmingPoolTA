package com.swimmingpool.chatgpt.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ChatGptResponse {
    private String id;
    private String object;
    private Long created;
    private String model;
    private Usage usage;

    @JsonProperty("system_fingerprint")
    private String systemFingerprint;
    private List<Choice> choices;
}
