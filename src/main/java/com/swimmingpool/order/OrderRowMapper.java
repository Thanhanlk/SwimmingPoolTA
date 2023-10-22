package com.swimmingpool.order;

import com.swimmingpool.order.response.OrderSearchResponse;
import jakarta.persistence.Tuple;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.DayOfWeek;
import java.util.Date;
import java.util.function.Function;

@UtilityClass
public class OrderRowMapper {

    public Function<Tuple, OrderSearchResponse> orderSearchRowMapper() {
        return tuple -> {
            Integer day = tuple.get("day", Integer.class);
            OrderSearchResponse orderSearchResponse = new OrderSearchResponse();
            orderSearchResponse.setId(tuple.get("id", String.class));
            orderSearchResponse.setPoolName(tuple.get("poolName", String.class));
            orderSearchResponse.setCourseName(tuple.get("courseName", String.class));
            orderSearchResponse.setDay(DayOfWeek.of(day));
            orderSearchResponse.setStartTime(tuple.get("startTime", Time.class));
            orderSearchResponse.setEndTime(tuple.get("endTime", Time.class));
            orderSearchResponse.setFullName(tuple.get("fullName", String.class));
            orderSearchResponse.setPhone(tuple.get("phone", String.class));
            orderSearchResponse.setCreatedDate(tuple.get("createdDate", Date.class));
            orderSearchResponse.setMethodPayment(tuple.get("methodPayment", String.class));
            orderSearchResponse.setStatus(tuple.get("status", String.class));
            orderSearchResponse.setTotal(tuple.get("total", BigDecimal.class));
            orderSearchResponse.setStartDate(tuple.get("startDate", Date.class));
            orderSearchResponse.setDiscount(tuple.get("discount", Integer.class));
            return orderSearchResponse;
        };
    }
}
