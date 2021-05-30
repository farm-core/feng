package com.yun.test.test;

import lombok.SneakyThrows;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName ProdConsumerDemo
 * @Description 多线程中横向判断将if判断改为while放置虚假唤醒
 * @Author wxf
 * @Date 2021/2/12 11:14
 * @Version 1.0
 */
class Aricodition {
    private int num = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void contsumer() {

        lock.lock();
        try {
            while (num != 0) {
                condition.await();
            }
            System.out.println(Thread.currentThread().getName() + "\t" + num);
            num++;
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void prod() {
        lock.lock();
        try {
            while (num == 0) {
                condition.await();
            }
            System.out.println(Thread.currentThread().getName() + "\t" + num);
            num--;
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /*public synchronized void contsumer() throws InterruptedException {

        while (num != 0) {
            this.wait();
        }
        System.out.println(Thread.currentThread().getName() + "\t" + num);
        num++;
        this.notifyAll();
    }

    public synchronized void prod() throws InterruptedException {

        while (num == 0) {
            this.wait();
        }
        System.out.println(Thread.currentThread().getName() + "\t" + num);
        num--;
        this.notifyAll();
    }*/
}

public class ProdConsumerDemo {
    public static void main(String[] args) {
        Aricodition aricodition = new Aricodition();

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    aricodition.contsumer();
                }
            }
        }, "A").start();

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    aricodition.prod();
                }
            }
        }, "B").start();

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    aricodition.contsumer();
                }
            }
        }, "C").start();

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    aricodition.prod();
                }
            }
        }, "D").start();
    }
}
