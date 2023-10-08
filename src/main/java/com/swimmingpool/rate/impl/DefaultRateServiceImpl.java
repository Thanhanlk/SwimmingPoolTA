package com.swimmingpool.rate.impl;

import com.swimmingpool.rate.IRateService;
import com.swimmingpool.rate.response.RateResponse;
import org.springframework.stereotype.Service;

@Service
public class DefaultRateServiceImpl implements IRateService {

    @Override
    public RateResponse getRate() {
        throw new UnsupportedOperationException("");
    }
}
