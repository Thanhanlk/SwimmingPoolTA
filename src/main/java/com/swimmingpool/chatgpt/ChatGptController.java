package com.swimmingpool.chatgpt;

import com.swimmingpool.chatgpt.constant.ChatGptConstant;
import com.swimmingpool.chatgpt.constant.Role;
import com.swimmingpool.chatgpt.request.Message;
import com.swimmingpool.chatgpt.response.ChatGptResponse;
import com.swimmingpool.chatgpt.response.Choice;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/chat-gpt")
@RequiredArgsConstructor
public class ChatGptController {

    private final ChatGptService chatGptService;

    @PostMapping(value = "/stream-message", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<byte[]> sendMessage(
            @RequestParam String userInput,
            @SessionAttribute(required = false, name = ChatGptConstant.CHAT_HIS_KEY) List<Message> chatHistoryList,
            HttpSession httpSession
    ) {
        if (chatHistoryList == null) {
            chatHistoryList = new ArrayList<>();
        }
        List<Message> finalChatHistoryList = chatHistoryList;
        StringBuffer fullMessage = new StringBuffer();
        return this.chatGptService.sendMessageAsStream(userInput, chatHistoryList)
                .map(res -> Optional.of(res)
                        .map(ChatGptResponse::getChoices)
                        .filter(x -> !x.isEmpty())
                        .map(x -> x.get(0))
                        .map(Choice::getMessage)
                        .map(Message::getContent)
                        .map(x -> x.getBytes(StandardCharsets.UTF_8))
                        .orElse(new byte[0]))
                .doOnNext(bytes -> {
                  fullMessage.append(new String(bytes, StandardCharsets.UTF_8));
                })
                .doOnComplete(() -> {
                    if (fullMessage.isEmpty()) {
                        return;
                    }
                    Message _message = new Message();
                    _message.setRole(Role.ASSISTANT);
                    _message.setContent(fullMessage.toString());
                    finalChatHistoryList.add(_message);
                    httpSession.setAttribute(ChatGptConstant.CHAT_HIS_KEY, finalChatHistoryList);
                });
    }
}
