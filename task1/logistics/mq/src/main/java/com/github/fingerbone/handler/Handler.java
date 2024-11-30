package com.github.fingerbone.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.fingerbone.message.Message;
import com.github.fingerbone.message.MessageRecord;
import com.github.fingerbone.message.RegisterItem;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class Handler {
    
    private final MessageQueue messageQueue;

    public Handler(@Autowired MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @GetMapping("/capacity")
    Mono<Integer> getRemainingCapacity() {
        return Mono.just(messageQueue.getRemainingCapacity());
    }

    @PostMapping("/register-client")
    Mono<Boolean> registerClient(@RequestBody RegisterItem registerItem) {
        log.info("registering client: {}", registerItem);
        messageQueue.register(registerItem);
        return Mono.just(true);
    }

    @PostMapping("/unregister-client")
    Mono<Boolean> unregisterClient(@RequestBody RegisterItem registerItem) {
        return Mono.just(messageQueue.unregister(registerItem));
    }

    @PostMapping("/send-message")
    Mono<Boolean> sendMessage(@RequestBody MessageRecord message) {
        log.info("message received: {}", message);
        messageQueue.enqueue(Message.fromRecord(message));
        return Mono.just(true);
    }
}
