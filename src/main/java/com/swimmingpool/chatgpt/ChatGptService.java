package com.swimmingpool.chatgpt;

import com.swimmingpool.chatgpt.constant.ChatGptConstant;
import com.swimmingpool.chatgpt.constant.Role;
import com.swimmingpool.chatgpt.request.ChatGptRequest;
import com.swimmingpool.chatgpt.request.Message;
import com.swimmingpool.chatgpt.response.ChatGptResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatGptService {

    @Setter(onMethod_ = {@Autowired, @Qualifier("chatGptRestTemplate")})
    private RestTemplate chatGptRestTemplate;

    @Setter(onMethod_ = @Autowired)
    private ChatGptProperties chatGptProperties;

    public ChatGptResponse sendMessage(String userInput, HttpSession session) {
        Message message = new Message();
        message.setRole(Role.USER);
        message.setContent(userInput);

        Object chatHistory = session.getAttribute(ChatGptConstant.CHAT_HIS_KEY);
        if (chatHistory == null) {
            chatHistory = new ArrayList<>();
        }
        List<Message> chatHistoryList = (List<Message>) chatHistory;
        chatHistoryList.add(message);

        ChatGptRequest request = new ChatGptRequest();
        request.setModel(this.chatGptProperties.getModel());
        request.setMessages(chatHistoryList);
        session.setAttribute(ChatGptConstant.CHAT_HIS_KEY, chatHistoryList);
        return this.chatGptRestTemplate.postForObject(ChatGptConstant.ENDPOINT_FOR_CHAT, request, ChatGptResponse.class);
    }
}
