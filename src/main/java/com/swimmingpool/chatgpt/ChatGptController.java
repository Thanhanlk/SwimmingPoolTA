package com.swimmingpool.chatgpt;

import com.swimmingpool.chatgpt.constant.ChatGptConstant;
import com.swimmingpool.chatgpt.request.Message;
import com.swimmingpool.chatgpt.response.ChatGptResponse;
import com.swimmingpool.chatgpt.response.Choice;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/chat-gpt")
@RequiredArgsConstructor
public class ChatGptController {

    private final ChatGptService chatGptService;

    @GetMapping("/send-message")
    public Message sendMessage(@RequestParam String message, HttpSession httpSession) {
        ChatGptResponse chatGptResponse = this.chatGptService.sendMessage(message, httpSession);
        return Optional.ofNullable(chatGptResponse)
                .map(ChatGptResponse::getChoices)
                .filter(x -> !x.isEmpty())
                .map(x -> x.get(0))
                .map(Choice::getMessage)
                .stream()
                .peek(x -> {
                    List<Message> messages = (List<Message>) httpSession.getAttribute(ChatGptConstant.CHAT_HIS_KEY);
                    messages.add(x);
                    httpSession.setAttribute(ChatGptConstant.CHAT_HIS_KEY, messages);
                })
                .findFirst()
                .orElse(null);
    }
}
