package com.swimmingpool.image;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageConstant {

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        COURSE("course/");

        private final String path;
    }
}
