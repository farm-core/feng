package com.yun.tx.lcn.server;

import com.codingapi.txlcn.tm.config.EnableTransactionManagerServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
//增加服务放注解
@EnableTransactionManagerServer
public class TxLcnServerApplication {

    public static void main(String[] args) {
        // http://127.0.0.1:7970/admin/index.html 启动  密码123456
        SpringApplication.run(TxLcnServerApplication.class, args);
    }

}
