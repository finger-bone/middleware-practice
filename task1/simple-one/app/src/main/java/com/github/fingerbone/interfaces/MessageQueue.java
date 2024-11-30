package com.github.fingerbone.interfaces;

public interface MessageQueue {
    void enqueue(Message message);
    Message dequeue();
    void addListener(MessageListener listener);
    void removeListener(MessageListener listener);
}
