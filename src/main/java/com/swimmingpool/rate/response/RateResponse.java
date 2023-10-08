package com.swimmingpool.rate.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class RateResponse {
    private boolean success;
    private String timestamp;
    private String base;
    private String date;
    private Map<String, BigDecimal> rates;
}
