package com.github.fingerbone.message;

import java.util.List;

import lombok.Getter;
import reactor.core.publisher.Mono;

@Getter
public abstract class MessageQueueListener {
    private final String identifier;
    private final String callback;
    private final List<String> topics;

    public MessageQueueListener(String identifier, String callback, List<String> topics) {
        this.identifier = identifier;
        this.callback = callback;
        this.topics = topics;
    }

    public Mono<Void> onReceive(MessageRecord message) {
        return Mono.empty();
    }

    public RegisterItem toRegisterRequest() {
        return new RegisterItem(identifier, callback, topics);
    }
}
