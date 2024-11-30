package com.github.fingerbone;
import com.github.fingerbone.interfaces.*;
import com.github.fingerbone.simple.*;

public class App {
    public static void main(String[] args) {
        // Create the message center
        MessageCenter center = SimpleMessageFactory.createMessageCenter();

        // Create a message queue and register it to the message center
        MessageQueue queue = new SimpleMessageQueue(5);
        center.registerQueue(queue);

        // Create two producers and two consumers
        Producer producer1 = new Producer(queue, "Producer 1");
        Producer producer2 = new Producer(queue, "Producer 2");
        Consumer consumer1 = new Consumer(queue, "Consumer 1");
        Consumer consumer2 = new Consumer(queue, "Consumer 2");

        // Start producer threads
        new Thread(producer1).start();
        new Thread(producer2).start();

        // Start consumer threads
        new Thread(consumer1).start();
        new Thread(consumer2).start();

        // // Wait for a while before unregistering the queue
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        center.unregisterQueue(queue);
    }
}
