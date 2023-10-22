package com.swimmingpool.common.config;

import com.swimmingpool.rate.RateProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;

@Configuration
@Slf4j
public class RestConfig {

    @Bean
    public RestTemplate defaultRestTemplate() {
        return new RestTemplateBuilder()
                .build();
    }

    @Bean("exchangeRestTemplate")
    @Primary
    @ConditionalOnProperty(prefix = "rate.exchange", value = "enabled")
    public RestTemplate exchangeRestTemplate(RateProperties rateProperties) {
        String url = rateProperties.getUrl();
        String currency = rateProperties.getCurrency();
        String accessToken = rateProperties.getAccessToken();
        return new RestTemplateBuilder()
                .rootUri(url)
                .setReadTimeout(Duration.ofMinutes(1))
                .interceptors((request, body, exe) -> {
                    URI uri = UriComponentsBuilder.fromHttpRequest(request)
                            .queryParam("access_key", accessToken)
                            .queryParam("base", currency)
                            .build().toUri();
                    HttpRequest newRequest = new HttpRequestWrapper(request) {
                        @Override
                        public URI getURI() {
                            return uri;
                        }
                    };
                    log.info("[REQUEST] {} - {}", newRequest.getMethod(), newRequest.getURI());
                    ClientHttpResponse execute = exe.execute(newRequest, body);
                    log.info("[RESPONSE] {} - {}", newRequest.getMethod(), newRequest.getURI());
                    return execute;
                })
                .build();
    }
}
