package com.github.fingerbone.simple;

import com.github.fingerbone.interfaces.Message;

public class SimpleMessage implements Message {
    private final String content;
    
    public SimpleMessage(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }
}
