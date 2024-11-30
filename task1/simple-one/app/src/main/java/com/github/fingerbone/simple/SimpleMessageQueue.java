package com.github.fingerbone.simple;

import java.util.*;

import com.github.fingerbone.interfaces.*;

public class SimpleMessageQueue implements MessageQueue {
    private List<Message> messages = new ArrayList<>();
    private List<MessageListener> listeners = new ArrayList<>();

    @Override
    public void enqueue(Message message) {
        messages.add(message);
        notifyListeners();
    }

    @Override
    public Message dequeue() {
        if (messages.isEmpty()) {
            return null;
        }
        return messages.remove(0);
    }

    @Override
    public void addListener(MessageListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(MessageListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (MessageListener listener : listeners) {
            if(listener != null) {
                listener.onMessageChanged(this);
            }
        }
    }
}
