package com.github.fingerbone.interfaces;

public interface MessageCenter {
    void registerQueue(MessageQueue queue);
    void unregisterQueue(MessageQueue queue);
    void broadcast(Message message);
}
