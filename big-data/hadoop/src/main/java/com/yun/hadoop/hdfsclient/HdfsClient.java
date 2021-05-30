package com.yun.hadoop.hdfsclient;

import lombok.SneakyThrows;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;

/**
 * @ClassName HdfsClient
 * @Description TODO
 * @Author wxf
 * @Date 2021/2/16 16:44
 * @Version 1.0
 */
public class HdfsClient {

    @SneakyThrows
    public static void main(String[] args) {
        FileSystem fileSystem =
                FileSystem.get(URI.create("hdfs://49.233.31.66:9000"), new Configuration(), "wxf");

        fileSystem.copyFromLocalFile(new Path("F:\\project\\test\\1.txt"), new Path("/"));

        fileSystem.close();
    }
}
