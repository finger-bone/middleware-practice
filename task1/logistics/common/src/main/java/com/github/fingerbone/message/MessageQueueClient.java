package com.github.fingerbone.message;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

/**
 * Server needs to implement the following endpoints:
 * 
 * - /capacity return the remaining capacity of the queue
 * - /send-message accept a message and return true if the message was sent
 * - /register-client accept a client identifier and a callback url
 * - /unregister-client accept a client identifier and return true if the client was unregistered
 * 
 * If the server gets a message, forward it to the callback url based on topics
 */

@Slf4j
public class MessageQueueClient {
    
    private final String url;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public MessageQueueClient(String url, WebClient webClient, ObjectMapper objectMapper) {
        this.url = url;
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    public Mono<Integer> getRemainingCapacity() {
        log.info("Getting remaining capacity");
        log.info(url + "/capacity");
        return webClient.get().uri(
            url + "/capacity"
        ).retrieve().bodyToMono(Integer.class);
    }

    public Mono<Boolean> sendMessage(MessageRecord message) {
        try {
            var encoded = objectMapper.writeValueAsString(message);
            log.info(encoded);
            return webClient.post()
            .uri(url + "/send-message")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(encoded))
            .retrieve()
            .bodyToMono(Boolean.class)
            .doOnError(error -> 
                log.error("Failed to register listener after retries: {}", error.getMessage())
            )
            .onErrorReturn(false);
        }
        catch (JsonProcessingException e) {
            log.error("Error sending message", e);
            return Mono.just(false);
        }
    }

    public Mono<Boolean> register(MessageQueueListener listener) {
        log.info("Registering listener: {}", listener);
        log.info("Destination: {}", url);
        
        try {
            String encoded = objectMapper.writeValueAsString(listener);
            return webClient.post()
            .uri(url + "/register-client")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(encoded))
            .retrieve()
            .bodyToMono(Boolean.class)
            .retryWhen(
                Retry.fixedDelay(12, Duration.ofSeconds(10)) // Retry 5 times with 2 seconds delay
                    .filter(throwable -> {
                        log.error("Retrying due to error: {}", throwable.getMessage());
                        return true; // Retry for all exceptions
                    })
                    .doAfterRetry(retrySignal -> 
                        log.warn("Retrying... Attempt #{}", retrySignal.totalRetries() + 1)
                    )
            )
            .doOnError(error -> 
                log.error("Failed to register listener after retries: {}", error.getMessage())
            )
            .onErrorReturn(false); // Return false if all retries fail
        }
        catch (JsonProcessingException e) {
            log.error("Error encoding listener: {}", e.getMessage());
            return Mono.just(false);
        }
    }

    public Mono<Boolean> unregister(MessageQueueListener listener) {
        try {
            log.info("Unregistering listener: {}", listener.toRegisterRequest());
            String encoded = objectMapper.writeValueAsString(listener);
            return webClient.post()
            .uri(url + "/unregister-client")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(encoded))
            .retrieve()
            .bodyToMono(Boolean.class)
            .retryWhen(
                Retry.fixedDelay(12, Duration.ofSeconds(10)) // Retry 5 times with 2 seconds delay
                    .filter(throwable -> {
                        log.error("Retrying due to error: {}", throwable.getMessage());
                        return true; // Retry for all exceptions
                    })
                    .doAfterRetry(retrySignal -> 
                        log.warn("Retrying... Attempt #{}", retrySignal.totalRetries() + 1)
                    )
            )
            .doOnError(error -> 
                log.error("Failed to register listener after retries: {}", error.getMessage())
            )
            .onErrorReturn(false);
        }
        catch (JsonProcessingException e) {
            log.error("Error encoding listener: {}", e.getMessage());
            return Mono.just(false);
        }
    }

}
