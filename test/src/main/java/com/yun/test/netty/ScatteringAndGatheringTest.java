package com.yun.test.netty;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @ClassName ScatteringAndGatheringTest
 * @Description TODO
 * @Author wxf
 * @Date 2021/2/20 22:34
 * @Version 1.0
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws Exception{

        // 1.获取网络服务
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 2.绑定socket并启动
        InetSocketAddress inetSocketAddress = new InetSocketAddress(12333);
        serverSocketChannel.socket().bind(inetSocketAddress);

        // 3.创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        // 4.监听客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        System.out.println("通道连接成功~~");
        while (true) {
            socketChannel.write(byteBuffers);
        }
    }
}
