package com.yun.rabbit.mq.bean;

import com.yun.rabbit.mq.util.CorrelationUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @program: springboot-rabbit
 * @description: 抽象发送失败callback记录  ---  es记录
 * @author: wxf
 * @date: 2020-03-05 15:34
 **/
//@Slf4j
//public class DefaultStorageClient extends BaseStorageClient implements Runnable {
//
//    private String name;
//
//    private String index;
//
//    private TransportClient client;
//
//    private boolean exist = false;
//
//    public DefaultStorageClient(String clientName, TransportClient transportClient) {
//        this.name = clientName;
//        this.client = transportClient;
//        this.index = INDEX_PREFIX + clientName;
//        init();
//    }
//
//
//    public void storage(String correlationId) {
//        BaseWrapper wrapper = CorrelationUtil.action(correlationId);
//        IndexResponse response = client.prepareIndex(index, name).setSource(createJson(wrapper)).get();
////        log.error("defeate logs:{}\n{}\nNNNN", correlationId, wrapper);
////        log.debug("loginfo{} ### status {}", wrapper, response.status().getStatus());
//    }
//
//    private XContentBuilder createJson(BaseWrapper wrapper) {
//        try {
//            XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
//                    .startObject()
//                    .field("queue", wrapper.getQueue())
//                    .field("tag", wrapper.getTag())
//                    .field("action", wrapper.getAction())
//                    .field("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(wrapper.getTimestamp()))
//                    .endObject();
//            return xContentBuilder;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    private void init() {
//        if (!exist && !isIndexExist()) {
//            try {
//                createIndex();
//                exist = true;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private boolean isIndexExist() {
//        IndicesExistsResponse inExistsResponse = client.admin().indices().exists(new IndicesExistsRequest(index)).actionGet();
//        return inExistsResponse.isExists();
//    }
//
//    private void createIndex() throws IOException {
//        CreateIndexRequestBuilder createIndex = client.admin().indices().prepareCreate(index);
//        XContentBuilder mapping = XContentFactory.jsonBuilder()
//                .startObject()
//                //设置之定义字段
//                .startObject("properties")
//                //设置数据ID
//                .startObject("tag").field("type", "keyword").endObject()
//                //设置数据操作类型
//                .startObject("action").field("type", "long").endObject()
//                .startObject("queue").field("type", "keyword").endObject()
//                //设置数据插入时间
//                .startObject("timestamp").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss:SSS").endObject()
//                .endObject()
//                .endObject();
//        createIndex.addMapping(name, mapping);
//        CreateIndexResponse res = createIndex.execute().actionGet();
//    }
//
//    @Override
//    public void run() {
//
//    }
//}
