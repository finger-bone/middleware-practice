package com.github.fingerbone;

import com.github.fingerbone.interfaces.*;
import com.github.fingerbone.simple.SimpleMessageFactory;

class Producer implements Runnable {
    private final MessageQueue queue;
    private final String name;

    public Producer(MessageQueue queue, String name) {
        this.queue = queue;
        this.name = name;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            Message message = SimpleMessageFactory.createMessage("Message " + i + " from " + name);
            try {
                queue.enqueue(message);
                System.out.println("Producer " + name + " put " + message.getContent());
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

