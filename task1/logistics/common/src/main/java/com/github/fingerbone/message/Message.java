package com.github.fingerbone.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class Message {
    private final String message;
    @Getter
    private final List<String> topic;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Optional<Message> fromObject(Object obj, List<String> topics) {
        try {
            return Optional.ofNullable(new Message(
                objectMapper.writeValueAsString(obj),
                new ArrayList<>(topics)
            ));
        }
        catch(JsonProcessingException e) {
            return Optional.empty();
        }
    }

    public <T> Optional<T> getPayload(Class<T> clazz) {
        try {
            return Optional.of(objectMapper.readValue(message, clazz));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Message fromRecord(MessageRecord record) {
        return new Message(record.getMessage(), record.getTopic());
    }

    public MessageRecord toRecord() {
        return new MessageRecord(message, topic);
    }
}
