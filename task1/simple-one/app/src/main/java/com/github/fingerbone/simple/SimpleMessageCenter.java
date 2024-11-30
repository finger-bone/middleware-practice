package com.github.fingerbone.simple;

import java.util.*;

import com.github.fingerbone.interfaces.*;


public class SimpleMessageCenter implements MessageCenter {
    private List<MessageQueue> queues = new ArrayList<>();
    private volatile static Optional<SimpleMessageCenter> instance = Optional.empty();
    private SimpleMessageCenter() {}
    public static SimpleMessageCenter getInstance() {
        instance = Optional.ofNullable(
            instance.orElseGet(() -> {
                return new SimpleMessageCenter();
            })
        );
        return instance.get();
    }
    @Override
    public void registerQueue(MessageQueue queue) {
        queues.add(queue);
    }
    @Override
    public void unregisterQueue(MessageQueue queue) {
        queues.remove(queue);
    }
    @Override
    public void broadcast(Message message) {
        for(MessageQueue queue : queues) {
            queue.enqueue(message);
        }
    }
}
