package com.swimmingpool.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserConstant {

    public enum Role {
        USER, TEACHER, ADMIN
    }
}
