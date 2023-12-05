package com.swimmingpool.chatgpt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Configuration
@Slf4j
public class ChatGptConfig {

    @Bean("chatGptRestTemplate")
    public RestTemplate chatGptRestTemplate(ChatGptProperties chatGptProperties) {
        return new RestTemplateBuilder()
                .setReadTimeout(Duration.ofMillis(chatGptProperties.getReadTimeout()))
                .setConnectTimeout(Duration.ofMillis(chatGptProperties.getConnectionTimeout()))
                .rootUri(chatGptProperties.getUrl())
                .defaultHeader("Authorization", String.format("Bearer %s", chatGptProperties.getApiKey()))
                .interceptors((request, body, execution) -> {
                    log.info("request: {}-{}", request.getURI(), new String(body, StandardCharsets.UTF_8));
                    return execution.execute(request, body);
                })
                .build();
    }
}
