package com.yun.test.算法;

import java.util.Stack;

public class LouTi {
    /**
     * 一个人爬楼梯，一步可以迈一级，二级，三级台阶，如果楼梯有N级，编写程序，输出所有走法。
     *
     * @param args
     */
    public static void main(String[] args) {
        Stack<Integer> stt = new Stack<Integer>();

        buileT(stt, 10);
    }

    public static void buileT(Stack<Integer> stt, int N) {
        if (N >= 1) {
            stt.push(1);
            buileT(stt, N - 1);
            stt.pop();
        }
        if (N >= 2) {
            stt.push(2);
            buileT(stt, N - 2);
            stt.pop();
        }
        if (N >= 3) {
            stt.push(3);
            buileT(stt, N - 3);
            stt.pop();
        }
        if (N == 0) {
            for (int i : stt) {
                System.out.print("Step:" + i + "-->");
            }
            System.out.println("完成");
        }
    }
}
