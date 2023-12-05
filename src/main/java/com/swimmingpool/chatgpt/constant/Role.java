package com.swimmingpool.chatgpt.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    SYSTEM,
    USER,
    ASSISTANT;

    @JsonCreator
    public static Role fromString(String key) {
        return key == null
                ? null
                : Role.valueOf(key.toUpperCase());
    }


    @JsonValue
    public String getRole() {
        return this.name().toLowerCase();
    }
}
