package com.swimmingpool.chatgpt.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swimmingpool.chatgpt.request.Message;
import lombok.Data;

@Data
public class Choice {

    @JsonProperty("delta")
    private Message message;
    @JsonProperty("finish_reason")
    private String finishReason;
    private Integer index;
}
