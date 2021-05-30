package com.yun.test.netty;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @ClassName NIOClient
 * @Description TODO
 * @Author wxf
 * @Date 2021/2/20 23:33
 * @Version 1.0
 */
public class NIOClient {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.configureBlocking(false);

        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",6666);

        if(!socketChannel.connect(inetSocketAddress)){

            while (!socketChannel.finishConnect()){
                System.out.println("连接需要时间，但是不会阻塞，可以继续干别的事...");
            }
        }

        String str = "服务来接成功...";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());

        socketChannel.write(buffer);
        System.in.read();
    }
}
