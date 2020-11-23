package com.company;

public class Thread_Pool_Executor {
    private final int corePoolSize;
    private final int maximumPoolSize;
    private final int keepAliveTime;
    private final PoolWorker[] workers;
    private final Blocking_Queue queue;
    private int taskCount;

    public Thread_Pool_Executor(int corePoolSize, int maximumPoolSize, int keepAliveTime, int queueSize) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
        queue = new Blocking_Queue(queueSize);
        workers = new PoolWorker[maximumPoolSize];
        taskCount = queue.size();

        for (int i = 0; i < corePoolSize; i++) {
            workers[i] = new PoolWorker();
            workers[i].start();
        }
    }

    public void execute(Runnable task) {
        if (taskCount > corePoolSize && taskCount <= maximumPoolSize) {
            workers[taskCount-1] = new PoolWorker();
            workers[taskCount-1].start();
            task.run();
        }

        synchronized (queue) {
            try {
                queue.put(task);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdown() {
        System.out.println("Shutting down thread pool");
        for (int i = 0; i < corePoolSize; i++) {
            workers[i] = null;
        }
    }

    private class PoolWorker extends Thread {
        public void run() {
            Runnable task = null;

            while (true) {
                taskCount = queue.size();
                try {
                    task = (Runnable) queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    task.run();
                } catch (RuntimeException e) {
                    System.out.println("Thread pool is interrupted due to an issue: " + e.getMessage());
                }
            }
        }
    }
}