package com.company;

import java.util.concurrent.RejectedExecutionException;
import java.util.*;
import java.util.stream.IntStream;

public class Thread_Pool_Executor {
    private final int maximumPoolSize;
    private final ArrayList<PoolWorker> workers;
    private final Blocking_Queue queue;
    private int taskCount;

    public Thread_Pool_Executor(int corePoolSize, int maximumPoolSize, int keepAliveTime, int queueSize) {
        this.maximumPoolSize = maximumPoolSize;
        queue = new Blocking_Queue(queueSize, keepAliveTime * 1000);
        workers = new ArrayList<>(maximumPoolSize);

        IntStream.range(0, corePoolSize).forEach(i -> workers.add(new PoolWorker(i,false)));
        IntStream.range(0, corePoolSize).forEach(i -> System.out.println("Added new worker " + i));
        IntStream.range(0, corePoolSize).forEach(i -> workers.get(i).start());
    }

    public void execute(Runnable task) {
        taskCount++;

        synchronized (queue) {
            try {
                queue.put(task);
            } catch (Exception FullQueueException) {
                if(workers.size() < maximumPoolSize) {
                    System.out.println("Added new worker " + (workers.size()));
                    PoolWorker newWorker = new PoolWorker(workers.size(), true);
                    workers.add(newWorker);
                    newWorker.runTask(task);
                    newWorker.start();
                }
                else {
                    taskCount--;
                    throw new RejectedExecutionException("Rejected task");
                }
            }
        }
    }

    public int getPoolSize() {
        return workers.size();
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void shutdown() {
        IntStream.range(0, maximumPoolSize).forEach(i -> workers.get(i).stopThread());
        workers.clear();
        System.out.println("Shutting down thread pool");
    }

    private class PoolWorker extends Thread {
        private final int id;
        private boolean isRunning;
        private final boolean canDie;

        public PoolWorker(int id, boolean canDie) {
            isRunning = true;
            this.canDie = canDie;
            this.id = id;
        }

        public void stopThread() {
            isRunning = false;
        }

        public void runTask(Runnable task) {
            try {
                task.run();
            } catch (RuntimeException e) {
                System.out.println("Thread pool is interrupted due to an issue: " + e.getMessage());
            }
        }

        public void run() {
            while (isRunning) {
                Runnable task;
                try {
                    task = (Runnable) queue.take(canDie);
                } catch (Exception IdleWorkerShutdown) {
                    System.out.println("Worker " + id + " shutdown");
                    stopThread();
                    workers.remove(this);
                    return;
                }
                try {
                    if (task != null) {
                        task.run();
                    }
                } catch (RuntimeException e) {
                    System.out.println("Thread pool is interrupted due to an issue: " + e.getMessage());
                }
            }
        }
    }
}