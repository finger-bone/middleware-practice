package com.github.fingerbone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fingerbone.message.MessageQueueClient;

@Configuration
public class MqConfig {
    @Value("${mq.server}")
    private String server;

    @Bean
    WebClient webClient() {
        return WebClient.create();
    }

    @Bean
    MessageQueueClient messageQueueClient(@Autowired WebClient webClient, @Autowired ObjectMapper objectMapper) {
        return new MessageQueueClient(server, webClient, objectMapper);
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
