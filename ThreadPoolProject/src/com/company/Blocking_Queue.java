package com.company;

public class Blocking_Queue {
    private final Object[] items;
    private int putCount, takeCount, count;
    private final int waitTime;

    public Blocking_Queue(int queueSize, int waitTime) {
        if(queueSize > 0) {
            items = new Object[queueSize];
        }
        else {
            items = new Object[1];
        }
        this.waitTime = waitTime;
    }

    public boolean isFull() {
        return count == items.length;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public synchronized void put(Object x) throws Exception {
        if (isFull()) {
            throw new Exception("FullQueueException");
        }
        items[putCount] = x;
        if (++putCount == items.length) {
            putCount = 0;
        }
        ++count;
        notifyAll();
    }

    public synchronized Object take(boolean canDie) throws Exception {
        if (isEmpty()) {
            try {
                wait(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(isEmpty()) {
            // If worker is in range [corePoolSize, maximumPoolSize], it can be shutdown
            if (canDie) throw new Exception("IdleWorkerShutdown");
            // If worker is in range [corePoolSize, maximumPoolSize], it return nothing
            else return null;
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