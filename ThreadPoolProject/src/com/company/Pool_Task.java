package com.company;

public class Pool_Task implements Runnable {
    private final int nr;

    public Pool_Task(int task_number) {
        nr = task_number;
    }

    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[" + nr + "] > Task running");
    }
}