package com.github.fingerbone;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.fingerbone.message.MessageQueueListener;
import com.github.fingerbone.message.MessageRecord;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/receive-message")
@RequiredArgsConstructor
public class WarehouseListenerController {
    private final MessageQueueListener listener;

    @PostMapping
    public Mono<Void> onReceive(@RequestBody MessageRecord message) {
        return listener.onReceive(message);
    }
}
