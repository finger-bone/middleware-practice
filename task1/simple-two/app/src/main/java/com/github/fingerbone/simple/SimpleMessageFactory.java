package com.github.fingerbone.simple;
import com.github.fingerbone.interfaces.*;

public class SimpleMessageFactory {
    private SimpleMessageFactory() {}   
    public static Message createMessage(String content) {
        return new SimpleMessage(content);
    }
    public static MessageQueue createMessageQueue(int capacity) {
        return new SimpleMessageQueue(capacity);
    }
    public static MessageCenter createMessageCenter() {
        return SimpleMessageCenter.getInstance();
    }
}
