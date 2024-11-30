package com.github.fingerbone.handler;

import com.github.fingerbone.message.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.springframework.stereotype.Component;

@Component
public class MessageQueue {

    private final int capacity = 32;
    private final List<RegisterItem> registerItems = new ArrayList<>();
    private Queue<Message> messages = new ArrayDeque<>(capacity);

    public int getRemainingCapacity() {
        return capacity - messages.size();
    }

    public void enqueue(Message message) {
        synchronized (this) {
            messages.add(message);
        }
    }

    public boolean isEmpty() {
        return messages.isEmpty();
    }

    public Message dequeue() {
        synchronized (this) {
            return messages.poll();
        }
    }

    public void register(RegisterItem registerItem) {
        registerItems.add(registerItem);
    }

    public boolean unregister(RegisterItem registerItem) {
        var itemToDelete = registerItems.stream().filter(it -> {
            return it.getIdentifier().equals(registerItem.getIdentifier()) && it.getCallback().equals(registerItem.getCallback());
        }).findFirst();
        if (itemToDelete.isEmpty()) {
            return false;
        }
        registerItems.remove(itemToDelete.get());
        return true;
    }

    public List<RegisterItem> getRegisterItems() {
        return new ArrayList<>(registerItems);
    }

    public List<Message> peekMessages() {
        return new ArrayList<>(messages);
    }
}
