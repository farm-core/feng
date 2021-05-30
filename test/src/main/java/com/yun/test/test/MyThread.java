package com.yun.test.test;

import java.util.concurrent.atomic.AtomicInteger;

public class MyThread extends Thread {

    public static AtomicInteger value = new AtomicInteger();

    @Override
    public void run() {
        //原子自增
        value.incrementAndGet();
    }
}