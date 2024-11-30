package com.github.fingerbone.interfaces;

public interface MessageQueue {
    void enqueue(Message message) throws InterruptedException;
    Message dequeue() throws InterruptedException;
    void addListener(MessageListener listener);
    void removeListener(MessageListener listener);
}
