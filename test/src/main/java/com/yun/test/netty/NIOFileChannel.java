package com.yun.test.netty;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @ClassName NIOFileChannel
 * @Description TODO
 * @Author wxf
 * @Date 2021/2/20 20:03
 * @Version 1.0
 */
public class NIOFileChannel {
    public static void main(String[] args) throws IOException {
        String str = "NIO test";

        // 1.创建输出流
        FileOutputStream fileOutputStream = new FileOutputStream("F:\\project\\test\\1.txt");

        // 2.创建文件通道
        FileChannel fileChannel = fileOutputStream.getChannel();

        // 3.创建buffer缓冲区并将要输出的内容添加
        ByteBuffer byteBuffer = ByteBuffer.allocate(str.getBytes().length);
        byteBuffer.put(str.getBytes());

        // 4.反转流读取buffer将其写入到通道中
        byteBuffer.flip();
        fileChannel.write(byteBuffer);

        // 5.关闭基础流
        fileOutputStream.close();
    }
}
