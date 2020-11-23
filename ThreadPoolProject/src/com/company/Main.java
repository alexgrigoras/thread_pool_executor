package com.company;

public class Main {

    public static void main(String[] args) {
        Thread_Pool_Executor thread_pool = new Thread_Pool_Executor(12, 12, 1, 5);

        for (int i = 0; i < 50; i++) {
            Pool_Task task = new Pool_Task(i);
            thread_pool.execute(task);
        }

        thread_pool.shutdown();
    }
}
