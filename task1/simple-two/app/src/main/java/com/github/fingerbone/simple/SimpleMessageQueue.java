package com.github.fingerbone.simple;

import java.util.ArrayList;
import java.util.List;

import com.github.fingerbone.interfaces.Message;
import com.github.fingerbone.interfaces.MessageListener;
import com.github.fingerbone.interfaces.MessageQueue;

/**
 * A simple message queue implementation.
 */
public class SimpleMessageQueue implements MessageQueue {
    private final int capacity; // Queue capacity
    private final List<Message> messages = new ArrayList<>(); // Stores the messages
    private final List<MessageListener> listeners = new ArrayList<>(); // Listeners for message changes
    private final Object lock = new Object(); // Synchronization lock

    /**
     * Constructor to initialize the message queue with a specified capacity.
     * 
     * @param capacity the maximum number of messages the queue can hold
     */
    public SimpleMessageQueue(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Adds a message to the queue, waiting if the queue is full.
     * 
     * @param message the message to be added
     * @throws InterruptedException if interrupted while waiting
     */
    @Override
    public void enqueue(Message message) throws InterruptedException {
        synchronized (lock) {
            while (messages.size() == capacity) {
                lock.wait(); // Wait until space is available
            }
            messages.add(message); // Add the message
            notifyListeners(); // Notify listeners of the change
            lock.notifyAll(); // Notify waiting threads
        }
    }

    /**
     * Removes a message from the queue, waiting if the queue is empty.
     * 
     * @return the removed message
     * @throws InterruptedException if interrupted while waiting
     */
    @Override
    public Message dequeue() throws InterruptedException {
        synchronized (lock) {
            while (messages.isEmpty()) {
                lock.wait(); // Wait until a message is available
            }
            Message message = messages.remove(0); // Remove the message
            notifyListeners(); // Notify listeners of the change
            lock.notifyAll(); // Notify waiting threads
            return message;
        }
    }

    /**
     * Adds a listener to be notified of queue changes.
     * 
     * @param listener the listener to add
     */
    @Override
    public void addListener(MessageListener listener) {
        synchronized (listeners) {
            listeners.add(listener); // Add the listener
        }
    }

    /**
     * Removes a listener from being notified of queue changes.
     * 
     * @param listener the listener to remove
     */
    @Override
    public void removeListener(MessageListener listener) {
        synchronized (listeners) {
            listeners.remove(listener); // Remove the listener
        }
    }

    /**
     * Notifies all listeners of a change in the message queue.
     */
    private void notifyListeners() throws InterruptedException {
        synchronized (listeners) {
            for (MessageListener listener : listeners) {
                if (listener != null) {
                    listener.onMessageChanged(this); // Notify the listener
                }
            }
        }
    }
}