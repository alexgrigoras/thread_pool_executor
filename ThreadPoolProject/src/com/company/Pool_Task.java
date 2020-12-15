package com.company;

public class Pool_Task implements Runnable {
    private final int id;
    private final int delay;

    public Pool_Task(int task_number, int delayMs) {
        id = task_number;
        delay = delayMs;
    }

    public void run() {
        System.out.println("[" + id + "] > Task running");

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}