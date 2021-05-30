package com.yun.test.netty;

import java.nio.IntBuffer;

/**
 * @ClassName NioBuffer
 * @Description TODO
 * @Author wxf
 * @Date 2021/2/19 22:10
 * @Version 1.0
 */
public class NioBuffer {
    public static void main(String[] args) {
        IntBuffer intBuffer = IntBuffer.allocate(5);

        intBuffer.put(10);
        intBuffer.put(20);
        intBuffer.put(30);
        intBuffer.put(40);
        intBuffer.put(50);

        // buffer反转
        intBuffer.flip();

        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
