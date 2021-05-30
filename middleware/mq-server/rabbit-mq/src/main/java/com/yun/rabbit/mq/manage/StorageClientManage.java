package com.yun.rabbit.mq.manage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志管理器
 *
 * @author wxf
 * @date 2020/3/18
 */
//@Component
//@Slf4j
//public class StorageClientManage<T extends DefaultStorageClient> {
//    private static final String INDEX_SUFFIX = "_receive";
//
//    private static TransportClient client;
//
//    private static ConcurrentHashMap<String, DefaultStorageClient> clientMap = new ConcurrentHashMap<>();
//
//    public StorageClientManage(TransportClient client) {
//        StorageClientManage.client = client;
//    }
//
//
//    void add(String clientName, DefaultStorageClient client) {
//        boolean containsKey = clientMap.containsKey(clientName);
//        if (containsKey) {
//            return;
//        }
//        clientMap.put(clientName, client);
//    }
//
//    /**
//     * 获取或者创建callbackclient
//     *
//     * @param clientName queue队列名称
//     * @return client
//     * @author wxf
//     * @date 2020/3/6 15:16
//     */
//    public static DefaultStorageClient get(String clientName) {
//        DefaultStorageClient client = clientMap.get(clientName);
//        if (Objects.isNull(client)) {
//            createClient(clientName);
//        }
//        log.debug("获取日志client  {}", clientName);
//        return clientMap.get(clientName);
//    }
//
//    public static DefaultStorageClient getReceiveClient(String clientName) {
//        clientName = clientName + INDEX_SUFFIX;
//        DefaultStorageClient client = clientMap.get(clientName);
//        if (Objects.isNull(client)) {
//            createClient(clientName);
//        }
//        log.debug("获取日志client  {}", clientName);
//        return clientMap.get(clientName);
//    }
//
//    private static synchronized void createClient(String clientName) {
//        log.debug("创建日志记录client {}", clientName);
//        clientMap.putIfAbsent(clientName, new DefaultStorageClient(clientName, client));
//    }
//
//    void createClient(String clientName, String clazz) {
//        try {
//            Object o = Class.forName(clazz).newInstance();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    void createClient(String clientName, T t) {
//        clientMap.put(clientName, t);
//    }
//
//}
