package com.company;

import java.util.concurrent.RejectedExecutionException;
import java.util.*;
import java.util.stream.IntStream;

public class Thread_Pool_Executor {
    private final int corePoolSize;
    private final int maximumPoolSize;
    private final int keepAliveTime;
    private final ArrayList<PoolWorker> workers;
    private final Blocking_Queue queue;
    private int taskCount;
    HashMap<PoolWorker, Integer> workersMap;
    private final Timer workersTimer;

    public Thread_Pool_Executor(int corePoolSize, int maximumPoolSize, int keepAliveTime, int queueSize) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
        queue = new Blocking_Queue(queueSize);
        workers = new ArrayList<>(maximumPoolSize);

        IntStream.range(0, corePoolSize).forEach(i -> workers.add(new PoolWorker()));
        IntStream.range(0, corePoolSize).forEach(i -> System.out.println("Added new worker " + i));
        IntStream.range(0, corePoolSize).forEach(i -> workers.get(i).start());

        this.workersTimer = new Timer();
        workersTimer.schedule(new WorkersCheck(), 0, 1000);
    }

    public void execute(Runnable task) {
        taskCount++;

        synchronized (queue) {
            try {
                queue.put(task);
            } catch (Exception FullQueueException) {
                if(workers.size() < maximumPoolSize) {
                    System.out.println("Added new worker " + (workers.size()));
                    workers.add(new PoolWorker());
                    workers.get(workers.size() - 1).runTask(task);
                    workers.get(workers.size() - 1).start();
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

    public void printFreeWorkers() {
        System.out.println(workersMap);
    }

    public void shutdown() {
        IntStream.range(0, maximumPoolSize).forEach(i -> workers.get(i).stopThread());
        workers.clear();
        System.out.println("Shutting down thread pool");
    }

    private class WorkersCheck extends TimerTask {
        public void run() {
            for(int i = corePoolSize; i < workers.size(); i++) {
                if(workers.get(i).isFree() && workers.get(i).getFreeTime() > keepAliveTime) {
                    System.out.println("Removed Worker ");
                    workers.get(i).stopThread();
                    workers.remove(i);
                }
            }
        }
    }

    private class PoolWorker extends Thread {
        private boolean isRunning;
        private boolean isFree;
        private int freeTime;
        private Timer elapsedTime;

        public PoolWorker(){
            isRunning = true;
            freeTime = 0;
        }

        public void stopThread(){
            isRunning = false;
        }

        public boolean isFree() {
            return isFree;
        }

        public int getFreeTime() {
            return freeTime;
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
                isFree = true;
                this.elapsedTime = new Timer();
                elapsedTime.schedule(new IncreaseTime(), 0, 1000);
                try {
                    Runnable task = (Runnable) queue.take();
                    isFree = false;
                    elapsedTime.cancel();
                    freeTime = 0;
                    try {
                        task.run();
                    } catch (RuntimeException e) {
                        System.out.println("Thread pool is interrupted due to an issue: " + e.getMessage());
                    }
                } catch (InterruptedException e) {
                    System.out.println("Queue is interrupted due to an issue: " + e.getMessage());
                }
            }
        }

        private class IncreaseTime extends TimerTask {
            public void run() {
                freeTime++;
            }
        }
    }
}