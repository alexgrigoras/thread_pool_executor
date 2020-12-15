package com.company;

import java.util.concurrent.*;

public class Main {

    private static void execute_tasks(Thread_Pool_Executor thread_pool, int taskDelay, int taskFrequency, int start_nr, int stop_nr) {
        for (int i = start_nr; i < stop_nr; i++) {
            Pool_Task task = new Pool_Task(i, taskDelay);

            try {
                thread_pool.execute(task);
            }
            catch (RejectedExecutionException e) {
                System.out.println("[" + i + "] > Task rejected");
            }

            try {
                Thread.sleep(taskFrequency);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Task count = " + thread_pool.getTaskCount());
        System.out.println("Pool size = " + thread_pool.getPoolSize());
    }

    private static void execute_tasks(ThreadPoolExecutor thread_pool, int taskDelay, int taskFrequency, int start_nr, int stop_nr) {
        for (int i = start_nr; i < stop_nr; i++) {
            Pool_Task task = new Pool_Task(i, taskDelay);

            try {
                thread_pool.execute(task);
            }
            catch (RejectedExecutionException e) {
                System.out.println("[" + i + "] > Task rejected");
            }

            try {
                Thread.sleep(taskFrequency);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Task count = " + thread_pool.getTaskCount());
        System.out.println("Pool size = " + thread_pool.getPoolSize());
    }

    public static void main(String[] args) {
        int corePoolSize = 3;                       // the initial number of processes that are spawned
        int maximumPoolSize = 6;                    // the maximum number of processes that can be spawned
        int keepAliveTime = 5;                      // the nr. of second that additional processes can stay alive if idle
        int queueSize = 6;                          // the capacity of the tasks queue
        TimeUnit timeUnit = TimeUnit.SECONDS;       // the time unit for Java Thread pool executor
        int taskDelay = 2 * 1000;                   // 2 seconds for processing

        // Create blocking queue for Java ThreadPoolExecutor library
        BlockingQueue<Runnable> queue=new LinkedBlockingQueue(queueSize);

        // Start thread pool
        Thread_Pool_Executor thread_pool = new Thread_Pool_Executor(corePoolSize, maximumPoolSize, keepAliveTime, queueSize);     // Implemented Thread pool executor
        //ThreadPoolExecutor thread_pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, queue);     // Java Library Thread pool executor

        // Execute tasks
        execute_tasks(thread_pool, taskDelay, 1, 0, 50);
        execute_tasks(thread_pool, taskDelay, 1000, 50, 100);
        execute_tasks(thread_pool, taskDelay, 1, 100, 150);

        // Shutdown thread pool
        thread_pool.shutdown();
    }
}