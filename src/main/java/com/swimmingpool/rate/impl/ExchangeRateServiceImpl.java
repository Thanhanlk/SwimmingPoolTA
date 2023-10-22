package com.swimmingpool.rate.impl;

import com.swimmingpool.rate.IRateService;
import com.swimmingpool.rate.response.RateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "rate.exchange", value = "enabled")
@Primary
@Slf4j
public class ExchangeRateServiceImpl implements IRateService {

    @Qualifier("exchangeRestTemplate")
    private final RestTemplate restTemplate;

    @Override
    public RateResponse getRate() {
        log.info("get rate by exchange rate provider");
        RateResponse forObject = this.restTemplate.getForObject("/latest", RateResponse.class);
        log.info("response rate by exchange rate provider: {}", forObject);
        return forObject;
    }
}
