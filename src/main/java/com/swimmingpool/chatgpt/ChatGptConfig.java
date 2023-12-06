package com.swimmingpool.chatgpt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.function.Consumer;

@Configuration
@Slf4j
public class ChatGptConfig {

    @Bean("chatGptWebClient")
    public WebClient webClient(ChatGptProperties properties) {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(properties.getTimeout()));
        return WebClient.builder()
                .baseUrl(properties.getUrl())
                .defaultHeader("Authorization", String.format("Bearer %s", properties.getApiKey()))
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter((request, next) -> {
                    log.info("request ChatGPT: {}", request.url());
                    return next.exchange(request)
                            .doOnSuccess(clientResponse -> {
                                log.info("response chatGPT: {} status {}", request.url(), clientResponse.statusCode());
                            });
                })
                .build();
    }
}
