package com.company;

import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        int corePoolSize = 3;
        int maximumPoolSize = 6;
        int keepAliveTime = 5;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        int queueSize = 6;
        int i;

        BlockingQueue<Runnable> queue=new LinkedBlockingQueue(queueSize);

        Thread_Pool_Executor thread_pool = new Thread_Pool_Executor(corePoolSize, maximumPoolSize, keepAliveTime, queueSize);

        //ThreadPoolExecutor thread_pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, queue);

        for (i = 0; i < 50; i++) {
            Pool_Task task = new Pool_Task(i);

            try {
                thread_pool.execute(task);
            }
            catch (RejectedExecutionException e) {
                System.out.println("[" + i + "] > Task rejected");
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Task count = " + thread_pool.getTaskCount());
        System.out.println("Pool size = " + thread_pool.getPoolSize());

        for (i = 50; i < 100; i++) {
            Pool_Task task = new Pool_Task(i);

            try {
                thread_pool.execute(task);
            }
            catch (RejectedExecutionException e) {
                System.out.println("[" + i + "] > Task rejected");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Task count = " + thread_pool.getTaskCount());
        System.out.println("Pool size = " + thread_pool.getPoolSize());

        for (i = 100; i < 150; i++) {
            Pool_Task task = new Pool_Task(i);

            try {
                thread_pool.execute(task);
            }
            catch (RejectedExecutionException e) {
                System.out.println("[" + i + "] > Task rejected");
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Task count = " + thread_pool.getTaskCount());
        System.out.println("Pool size = " + thread_pool.getPoolSize());

        thread_pool.shutdown();
    }
}
