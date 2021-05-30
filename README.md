# yi-yun  管理系统说明文档[当前服务均可以配置为集群模式]
###项目结构
```lua
yi-yun
  │
  ├─center                                                              --系统中心总模块
  │   ├─admin                                                               --管理中心【12101】
  │   ├─auth                                                                --鉴权中心【12102】
  │
  ├─cloud                                                               --分布式组件管理总模块
  │   ├─config                                                              --配置中心【13101】
  │   ├─monitor                                                             --监测中心【13102】
  │   ├─eureka                                                              --注册中心【8761】
  │   ├─feign-client                                                        --接口调用中心【13103】
  │   └─gateway                                                             --网关中心【13104】
  │
  ├─commons                                                             --公共模块
  │   ├─common-utils                                                        --公共工具类模块
  │   └─upload-download                                                     --公共上传下载模块
  │
  ├─doc                                                                 --文档
  │
  ├─middleware                                                          --中间件工具总模块
  │   ├─mq-server                                                           --mq-server中间件总模块
  │   │ └─rabbit-mq                                                             --rabbit-mq模块
  │   │                                                         
  │   ├─tx-lcn-server                                                       --分布式事务服务管理模块            
  │   ├─websocket                                                           --websocket服务工具模块
  │   └─xxl-job                                                             --xxl-job定时管理总模块
  │        ├─doc                                                                --文档
  │        ├─xxl-job-admin                                                      --xxl定时管理服务中心
  │        ├─xxl-job-core                                                       --xxl公共依赖模块
  │        └─xxl-job-executor-samples                                           --xxl定时管理版本样例模块
  │                     ├─xxl-job-executor-sample-frameless                         --简单原始样例
  │                     ├─xxl-job-executor-sample-spring                            --spring版本样例
  │                     └─xxl-job-executor-sample-springboot                        --springboot版本样例
  │
  ├─model                                                               --基础总模块
  │   └─model-base                                                          --公共实体模块
  │
  ├─test                                                                --日常测试使用模块【9999】

```
----
一、运行环境
----

<br>
   -  版本:SpringCloud/SpringBoot 2.X<br>
   -  环境:JDK8/11<br>
   -  编码:UTF-8<br>
  - IDE:Spring Tool Suit(STS)/IDEA(推荐)/VSCode with STS(New)<br>
 
 ```xml
  <properties>
     <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
     <java.version>1.8</java.version>
   </properties>
 ```

----
二、有关项目启动和配置的说明
----

- 由于子项目太多，暂时移除所有模块的引用，`请自己根据需要请到父项目的pom.xml启用对应module` <br>
- 大部分模块均为`springboot` 可独立启动模块，可不依赖eureka注册中心等springcloud组件。
- 如果是微服务，请请先启动eureka基础模块，因为它是注册中心，大多数微服务必须依赖于它才能实现必要的功能。 <br>
- gateway路由中心，启用gateway，并配置yml文件即可(已经带了一点小配置，可根据实际情况修改)。 <br>
- 按需启用xxl-job-admin/websocket/admin/monitor等等<br>
- 其他模块均不需要eureka等模块，可直接启动
- 分布式组件服务启动需要依赖配置config-server模块在github有响应代码可以拉取

----
三、使用说明
----

Eureka
----
微服务注册中心，SpringCloud全家桶，Netflix版注册中心。这个优先启动，是一切微服务的基础也可以修改配置，进行集群，这里默认单机单例 <br>
<table><tbody>
<tr><td>http://127.0.0.1:8761/eureka</td> <td>注册中心</td></tr><br>
</tbody></table>


