package com.github.fingerbone;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.fingerbone.handler.MessageQueue;
import com.github.fingerbone.message.MessageRecord;
import com.github.fingerbone.message.RegisterItem;

@Controller
public class DetailController {
    private final MessageQueue messageQueue;

    public DetailController(@Autowired MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @GetMapping
    public String detailPage(Model model) {
        return "detail";
    }

    @GetMapping("/info")
    @ResponseBody
    public Map<String, Object> getMessages() {
        List<MessageRecord> messages = messageQueue.peekMessages().stream()
                .map(m -> m.toRecord())
                .collect(Collectors.toList());
        List<RegisterItem> registerItems = messageQueue.getRegisterItems();

        return Map.of(
                "capacity", messageQueue.getRemainingCapacity(),
                "messages", messages,
                "registerItems", registerItems
        );
    }
    
}
