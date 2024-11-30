package com.github.fingerbone;
import com.github.fingerbone.interfaces.*;

/*
 * // 简单的消息队列类，实现了 MessageQueue 接口
public class SimpleMessageQueue implements MessageQueue {
private int capacity; // 队列容量
private List<Message> messages = new ArrayList<>();
private List<MessageListener> listeners = new ArrayList<>();
private Object lock = new Object();
public SimpleMessageQueue(int capacity) {
this.capacity = capacity;
}
@Override
public void enqueue(Message message) throws InterruptedException {
synchronized (lock) {
while (messages.size() == capacity) {
lock.wait();
}
messages.add(message);
notifyListeners();
lock.notifyAll();
}
}
@Override
public Message dequeue() throws InterruptedException {
synchronized (lock) {
while (messages.isEmpty()) {
lock.wait();
}
Message message = messages.remove(0);
notifyListeners();
lock.notifyAll();
return message;
}
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
if (listener != null) {
listener.onMessageChanged(this);
}
}
}
}
// 消息工厂，用来创建各个组件
public class MessageFactory {
// 私有化构造函数，禁止外部创建对象
private MessageFactory() {}
// 创建一个 Message 对象
public static Message createMessage(String content) {
return new SimpleMessage(content);
}
// 创建一个 MessageQueue 对象
public static MessageQueue createMessageQueue(int capacity) {
return new SimpleMessageQueue(capacity);
}
// 创建一个 MessageCenter 对象
public static MessageCenter createMessageCenter() {
return SimpleMessageCenter.getInstance();
}
}
// 生产者类，用于向消息队列中添加消息
public class Producer implements Runnable {
private final MessageQueue queue;
private final String name;
public Producer(MessageQueue queue, String name) {
this.queue = queue;
this.name = name;
}
@Override
public void run() {
for (int i = 0; i < 10; i++) {
Message message = MessageFactory.createMessage("Message " + i + "
from " + name);
try {
queue.enqueue(message);
System.out.println("Producer " + name + " put " +
message.getContent());
Thread.sleep(100);
} catch (InterruptedException e) {
e.printStackTrace();
}
}
}
}
// 消费者类，用于从消息队列中取出消息并处理
public class Consumer implements Runnable {
private final MessageQueue queue;
private final String name;
public Consumer(MessageQueue queue, String name) {
this.queue = queue;
this.name = name;
}
@Override
public void run() {
while (true) {
try {
Message message = queue.dequeue();
System.out.println("Consumer " + name + " got " +
message.getContent());
Thread.sleep(200);
} catch (InterruptedException e) {
e.printStackTrace();
}
}
}
}
public class Example {
public static void main(String[] args) {
// 创建消息中心
MessageCenter center = MessageFactory.createMessageCenter();
// 创建一个消息队列并注册到消息中心
MessageQueue queue = new SimpleMessageQueue(5);
center.registerQueue(queue);
// 创建两个生产者和两个消费者
Producer producer1 = new Producer(queue,
Producer producer2 = new Producer(queue,
Consumer consumer1 = new Consumer(queue,
Consumer consumer2 = new Consumer(queue,
//启动两个生产者线程，用于向消息队列发送消息
new Thread(producer1).start();
new Thread(producer2).start();
//启动两个消费者线程，用于从消息队列接收消息
new Thread(consumer1).start();
new Thread(consumer2).start();
"Producer 1");
"Producer 2");
"Consumer 1");
"Consumer 2");
// 稍微等待一段时间后，注销消息队列
try {
Thread.sleep(5000);
} catch (InterruptedException e) {
e.printStackTrace();
}
center.unregisterQueue(queue);
}
}
 */

 class Consumer implements Runnable {
    private final MessageQueue queue;
    private final String name;

    public Consumer(MessageQueue queue, String name) {
        this.queue = queue;
        this.name = name;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message message = queue.dequeue();
                System.out.println("Consumer " + name + " got " + message.getContent());
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
