package com.github.fingerbone.handler;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fingerbone.message.Message;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class Dispatcher {
    private final MessageQueue messageQueue;
    private final WebClient webClient;
    private final int dispatchInterval = 1000;
    private final ObjectMapper objectMapper;

    public Dispatcher(@Autowired MessageQueue messageQueue, @Autowired WebClient webClient, @Autowired ObjectMapper objectMapper) {
        this.messageQueue = messageQueue;
        this.webClient = webClient;
        this.objectMapper = objectMapper;
        startDispatching();
    }

    private boolean prefixMatch(List<String> src, List<String> tgt) {
        if(tgt.size() > src.size()) {
            return false;
        }
        boolean result = true;
        for(int i = 0; i < tgt.size(); i++) {
            if(!tgt.get(i).equals("*") && !tgt.get(i).equals(src.get(i))) {
                result = false;
                break;
            }
        }
        return result;
    }

    private Mono<Void> dispatch(Message message, String callback) {
        try {
            var encoded = objectMapper.writeValueAsString(message.toRecord());
            log.info("Dispatching message: " + encoded + "to: " + callback);
            return webClient.post()
            .uri(callback)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(encoded))
            .retrieve()
            .bodyToMono(Void.class);
        }
        catch (JsonProcessingException e) {
            log.error("Error sending message", e);
            return Mono.just(null);
        }
    }

    public void startDispatching() {
        Flux.interval(Duration.ofMillis(dispatchInterval))
            .flatMap(tick -> {
                if (messageQueue.isEmpty()) {
                    return Flux.empty();
                }
                Message message = messageQueue.dequeue();
                if (message != null) {
                    return Flux.just(message);
                }
                return Flux.empty();
            })
            .flatMap(message -> {
                var registerItems = messageQueue.getRegisterItems();
                return Flux.fromIterable(registerItems)
                    .flatMap(registerItem -> {
                        var tgt = registerItem.getTopics();
                        var src = message.getTopic();
                        if (prefixMatch(src, tgt)) {
                            return dispatch(message, registerItem.getCallback());
                        }
                        return Mono.empty();
                    });
            })
            .subscribe();
    }
}