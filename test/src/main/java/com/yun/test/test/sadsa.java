package com.yun.test.test;

public class sadsa {
    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[100];
        for (int i = 0; i < 100; i++) {
            threads[i] = new MyThread();
            threads[i].start();
        }

        for (int j = 0; j < 100; j++) {
            threads[j].join();
        }

        System.out.println("value:" + MyThread.value);
    }
}
