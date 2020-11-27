package com.company;

public class Blocking_Queue {
    private final Object[] items;
    private int putCount, takeCount, count;

    public Blocking_Queue(int queueSize) {
        if(queueSize > 0) {
            items = new Object[queueSize];
        }
        else {
            items = new Object[1];
        }
    }

    public boolean isFull() {
        return count == items.length;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public int size() {
        return count;
    }

    public synchronized void put(Object x) throws Exception {
        while (isFull()) {
            throw new Exception("FullQueueException");
        }
        items[putCount] = x;
        if (++putCount == items.length) {
            putCount = 0;
        }
        ++count;
        notifyAll();
    }

    public synchronized Object take() throws InterruptedException {
        while (isEmpty()) {
            wait();
        }
        Object x = items[takeCount];
        if (++takeCount == items.length) {
            takeCount = 0;
        }

        --count;
        notifyAll();

        return x;
    }
}