package com.company;

public class Pool_Task implements Runnable {
    private final int id;

    public Pool_Task(int task_number) {
        id = task_number;
    }

    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[" + id + "] > Task running");
    }
}