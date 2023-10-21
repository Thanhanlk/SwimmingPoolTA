package com.swimmingpool.ticket;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TicketConstant {

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        CLOSE_COURSE("Kết thúc khóa học");

        private final String label;
    }

    @RequiredArgsConstructor
    @Getter
    public enum Status {
        PENDING("Chờ duyệt"), REJECT("Từ chối"), CANCEL("Hủy bỏ"), APPROVED("Đã duyệt");

        private final String label;
    }
}
