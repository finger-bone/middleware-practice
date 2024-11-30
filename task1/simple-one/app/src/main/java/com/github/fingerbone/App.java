package com.github.fingerbone;
import com.github.fingerbone.interfaces.*;
import com.github.fingerbone.simple.*;

public class App {
    public static void main(String[] args) {
        MessageCenter center = SimpleMessageFactory.createMessageCenter();
        MessageQueue queue1 = SimpleMessageFactory.createMessageQueue();
        MessageQueue queue2 = SimpleMessageFactory.createMessageQueue();
        center.registerQueue(queue1);
        center.registerQueue(queue2);
        MessageListener listener1 = new MessageListener() {
            @Override
            public void onMessageChanged(MessageQueue queue) {
                Message message = queue.dequeue();
                System.out.println("Listener 1: " + message.getContent());
            }
        };
        queue1.addListener(listener1);
        MessageListener listener2 = new MessageListener() {
            @Override
            public void onMessageChanged(MessageQueue queue) {
                Message message = queue.dequeue();
                System.out.println("Listener 2: " + message.getContent());
            }
        };
        queue2.addListener(listener2);
        Message message = SimpleMessageFactory.createMessage("Hello, world!");
        center.broadcast(message);
        center.unregisterQueue(queue1);
        center.unregisterQueue(queue2);
        queue1.removeListener(listener1);
        queue2.removeListener(listener2);

    }
}
