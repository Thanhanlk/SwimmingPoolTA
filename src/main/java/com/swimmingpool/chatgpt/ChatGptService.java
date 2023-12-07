package com.swimmingpool.chatgpt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swimmingpool.chatgpt.constant.ChatGptConstant;
import com.swimmingpool.chatgpt.constant.Role;
import com.swimmingpool.chatgpt.request.ChatGptRequest;
import com.swimmingpool.chatgpt.request.Message;
import com.swimmingpool.chatgpt.response.ChatGptResponse;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class ChatGptService {

    @Setter(onMethod_ = {@Autowired, @Qualifier("chatGptWebClient")})
    private WebClient chatGptWebClient;

    @Setter(onMethod_ = @Autowired)
    private ChatGptProperties chatGptProperties;

    @Setter(onMethod_ = @Autowired)
    private ObjectMapper objectMapper;

    public Flux<ChatGptResponse> sendMessageAsStream(String userInput, List<Message> chatHistoryList) {
        Message message = new Message();
        message.setRole(Role.USER);
        message.setContent(userInput);

        chatHistoryList.add(message);
        ChatGptRequest request = new ChatGptRequest();
        request.setStream(true);
        request.setModel(this.chatGptProperties.getModel());
        request.setMessages(chatHistoryList);

        return this.chatGptWebClient.post()
                .uri(ChatGptConstant.ENDPOINT_FOR_CHAT_COMPLETION)
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(String.class)
                .map(json -> {
                    try {
                        return this.objectMapper.readValue(json, ChatGptResponse.class);
                    } catch (Exception ex) {
                        return new ChatGptResponse();
                    }
                });
    }
}
