package com.yun.test.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @ClassName NIOserver
 * @Description TODO
 * @Author wxf
 * @Date 2021/2/20 23:12
 * @Version 1.0
 */
public class NIOserver {
    public static void main(String[] args) throws Exception {
        // 1.获取服务通道和事件集合
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();

        // 2.绑定端口设置非阻塞
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        serverSocketChannel.configureBlocking(false);

        // 3.注册当前事件类型
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            if (selector.select(1000) == 0) {
                System.out.println("当前无连接~~");
                continue;
            }

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 如果有一个连接过来
                if (selectionKey.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("有客户端连接...  连接的通道为：" + socketChannel.hashCode());
                    socketChannel.configureBlocking(false);

                    // 获取通道并处理信息
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                // 读的事件
                if (selectionKey.isReadable()) {
                    // 获取连接和数据
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();

                    socketChannel.read(buffer);
                    System.out.println("客户端发送的数据是：" + new String(buffer.array()));
                }

                // 清楚已经处理的事件
                iterator.remove();
            }
        }
    }
}
