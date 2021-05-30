#!/usr/bin/env bash

#========= 18 ===========
docker run -d --net=host --name eureka --restart always --volume /home/clear/project/center/eureka/:/home/clear/program/  \
-e "EUREKA_NODE_1_HOST=10.150.18.18" -e "EUREKA_NODE_1_PORT=8761" -e "EUREKA_NODE_2_HOST=10.150.18.16" -e "EUREKA_NODE_2_PORT=8761"  openjdk:v0.1 \
java -jar -Xms1024m -Xmx1024m -Xmn512m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M -Xloggc:'gc.log' \
-XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause datacenter-eureka.jar

docker run -d --net=host --name eureka --restart always --volume /home/clear/project/center/eureka/:/home/clear/program/  \
-e "EUREKA_NODE_1_HOST=10.150.18.17" -e "EUREKA_NODE_1_PORT=8761" -e "EUREKA_NODE_2_HOST=10.150.18.18" -e "EUREKA_NODE_1_PORT=8761"  openjdk:v0.1 \
java -jar -Xms1024m -Xmx1024m -Xmn512m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M -Xloggc:'gc.log' \
-XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause datacenter-eureka.jar
#服务列表
cmacast-aws-service,cmacast-cawn-service,cmacast-gps-service,cmacast-hydr-service,cmacast-idb,cmacast-lpd-service,cmacast-lupardat-service,cmacast-radi-service,cmacast-rffc-service,cmacast-sand-service,cmacast-ship-service,cmacast-surf-service,cmacast-upper-service,cmacast-wrpd-service

#18clear admin 测试容器
docker run -d --net=host --name admin --restart=always --memory=2G --memory-swap=3G --init \
--volume /home/clear/project/center/admin:/home/clear/program/  10.150.18.14:6000/baseimage:v2.0 \
java -jar -Xms2g -Xmx2g -Xmn1024m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M \
-Xloggc:'/home/clear/program/gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause -Duser.timezone=GMT+8 \
/home/clear/program/datacenter-admin.jar --spring.profiles.active=test
#198测试admin
docker run -d --net=host --name admin --restart=always --memory=2G --memory-swap=3G --init \
--volume /home/clear/project/center/admin:/home/clear/program/  10.150.18.14:6000/baseimage:v2.0 \
java -jar -Xms2g -Xmx2g -Xmn1024m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M \
-Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause -Duser.timezone=GMT+8 \
datacenter-admin.jar --spring.profiles.active=test


#================================ 198 171 172  baseimage部署==========================
#eureka 198
docker run -d --net=host --name=eureka --restart always --memory=1G --memory-swap=1500M --init --cpus=2 \
-e "EUREKA_NODE_1_HOST=10.110.18.171" -e "EUREKA_NODE_2_HOST=10.110.18.172" \
--volume /home/clear/project/center/eureka/:/home/clear/program/ 10.150.18.14:6000/baseimage:v8 \
java -jar -Xms1g -Xmx1g -Xmn512m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M \
-Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause datacenter-eureka.jar
# eureka 171
docker run -d --net=host --name=eureka --restart always --memory=1G --memory-swap=1500M --init --cpus=2 \
-e "EUREKA_NODE_1_HOST=10.110.18.198" -e "EUREKA_NODE_2_HOST=10.110.18.172" \
--volume /home/clear/project/center/eureka/:/home/clear/program/ 10.150.18.14:6000/baseimage:v8 \
java -jar -Xms1g -Xmx1g -Xmn512m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M \
-Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause datacenter-eureka.jar
# eureka 172
docker run -d --net=host --name=eureka --restart always --memory=1G --memory-swap=1500M --init --cpus=2 \
-e "EUREKA_NODE_1_HOST=10.110.18.171" -e "EUREKA_NODE_2_HOST=10.110.18.198" \
--volume /home/clear/project/center/eureka/:/home/clear/program/ 10.150.18.14:6000/baseimage:v8 \
java -jar -Xms1g -Xmx1g -Xmn512m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M \
-Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause datacenter-eureka.jar

# config
docker run -d --net=host --name=config --restart=always --memory=1G --memory-swap=1500M --init --cpus=2 \
-e "CONFIG_LABEL=release" -e "EUREKA_NODE_1_HOST=10.110.18.198" -e "EUREKA_NODE_2_HOST=10.110.18.171" -e "EUREKA_NODE_3_HOST=10.110.18.172" \
--volume /home/clear/project/center/config/:/home/clear/program/ 10.150.18.14:6000/baseimage:v8 \
java -jar -Xms1g -Xmx1g -Xmn512m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M \
-Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause datacenter-config.jar

# monitor
docker run -d --net=host --name=monitor --restart=always --memory=1G --memory-swap=1500M --init --cpus=2 \
-e "EUREKA_NODE_1_HOST=10.110.18.198" -e "EUREKA_NODE_2_HOST=10.110.18.171" -e "EUREKA_NODE_3_HOST=10.110.18.172" \
--volume /home/clear/project/center/monitor/:/home/clear/program/ 10.150.18.14:6000/baseimage:v1 \
java -jar -Xms1g -Xmx1g -Xmn512m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M \
-Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause datacenter-monitor.jar

# gateway 部署在120 121 122
docker run -d --net=host --name=gateway --restart=always --memory=2G --memory-swap=3G --init --cpus=2 \
-e "CONFIG_LABEL=release" -e "EUREKA_NODE_1_HOST=10.110.18.198" -e "EUREKA_NODE_2_HOST=10.110.18.171" -e "EUREKA_NODE_3_HOST=10.110.18.172" \
--volume /home/clear/project/gateway/:/home/clear/program/ 10.150.18.14:6000/baseimage:v8 \
java -jar -XX:+UseG1GC -Xms2g -Xmx2g -XX:MaxGCPauseMillis=200 -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps \
-XX:+PrintGCCause -XX:+HeapDumpOnOutOfMemoryError datacenter-gateway.jar

# datasup
docker run -d --net=host --name=datasup --restart=always --memory=2G --memory-swap=3G --init --cpus=4 \
-e "CONFIG_LABEL=release" -e "EUREKA_NODE_1_HOST=10.110.18.198" -e "EUREKA_NODE_2_HOST=10.110.18.171" -e "EUREKA_NODE_3_HOST=10.110.18.172" \
--volume /home/clear/project/datasup/:/home/clear/program/ 10.150.18.14:6000/baseimage:v1 \
java -jar -XX:+UseG1GC -Xms2g -Xmx2g -XX:MaxGCPauseMillis=200 -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps \
-XX:+PrintGCCause -XX:+HeapDumpOnOutOfMemoryError datacenter-datasup.jar

# env-service
docker run -d --net=host --name=env-service --restart=always --memory=6G --memory-swap=7G --init --cpus=4 \
-e "CONFIG_LABEL=release" -e "EUREKA_NODE_1_HOST=10.110.18.198" -e "EUREKA_NODE_2_HOST=10.110.18.171" -e "EUREKA_NODE_3_HOST=10.110.18.172" \
--volume /home/clear/project/env/env-service/:/home/clear/program/ 10.150.18.14:6000/baseimage:v1 \
java -jar -XX:+UseG1GC -Xms6g -Xmx6g -XX:MaxGCPauseMillis=200 -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps \
-XX:+PrintGCCause -XX:+HeapDumpOnOutOfMemoryError datacenter-env-service.jar

# auth-server
docker run -d --net=host --name=auth-server --restart=always --memory=1G --memory-swap=1500M --init --cpus=2 \
-e "CONFIG_LABEL=release" -e "EUREKA_NODE_1_HOST=10.110.18.198" -e "EUREKA_NODE_2_HOST=10.110.18.171" -e "EUREKA_NODE_3_HOST=10.110.18.172" \
--volume /home/clear/project/authority/auth-server/:/home/clear/program/ 10.150.18.14:6000/baseimage:v1 \
java -jar -XX:+UseG1GC -Xms1g -Xmx1g -XX:MaxGCPauseMillis=200 -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps \
-XX:+PrintGCCause -XX:+HeapDumpOnOutOfMemoryError datacenter-auth-server.jar

# admin
docker run -d --net=host --name=admin --restart=always --memory=2G --memory-swap=3G --init --cpus=3 \
-e "CONFIG_LABEL=release" -e "EUREKA_NODE_1_HOST=10.110.18.198" -e "EUREKA_NODE_2_HOST=10.110.18.171" -e "EUREKA_NODE_3_HOST=10.110.18.172" \
--volume /home/clear/project/center/admin/:/home/clear/program/ 10.150.18.14:6000/baseimage:v1 \
java -jar -Xms2g -Xmx2g -Xmn1024m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M \
-Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause -Duser.timezone=GMT+8 datacenter-admin.jar --spring.profiles.active=test
# idb-analysis-aws
docker run -d --net=host --name=idb-analysis-aws --restart=always --memory=1.5G --memory-swap=2G --init --cpus=4 \
-e "CONFIG_LABEL=release" -e "EUREKA_NODE_1_HOST=10.110.18.198" -e "EUREKA_NODE_2_HOST=10.110.18.171" -e "EUREKA_NODE_3_HOST=10.110.18.172" \
--volume /home/clear/project/idb/idb-analysis-aws/:/home/clear/program/ --volume /mnt:/mnt 10.150.18.14:6000/baseimage:v1 \
java -jar -Xms1g -Xmx1g -Xmn512m -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M \
-Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause -Duser.timezone=GMT+8 datacenter-idb-analysis-aws.jar


#================================ 198 171 172  服务镜像部署===========================
#eureka 198
docker run -d --net=host --name eureka --restart always --memory=1G --memory-swap=1500M --init \
-e "EUREKA_NODE_1_HOST=10.110.18.171" -e "EUREKA_NODE_2_HOST=10.110.18.172" 10.150.18.14:6000/eureka \
java -jar -Xms1g -Xmx1g -Xmn512m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M \
-Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause app.jar
# eureka 171
docker run -d --net=host --name=eureka --restart=always --memory=1G --memory-swap=1500M --init \
-e "EUREKA_NODE_1_HOST=10.110.18.198" -e "EUREKA_NODE_2_HOST=10.110.18.172" 10.150.18.14:6000/eureka \
java -jar -Xms1g -Xmx1g -Xmn512m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M \
-Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause app.jar
# eureka 172
docker run -d --net=host --name=eureka --restart=always --memory=1G --memory-swap=1500M --init \
-e "EUREKA_NODE_1_HOST=10.110.18.171" -e "EUREKA_NODE_2_HOST=10.110.18.198" 10.150.18.14:6000/eureka \
java -jar -Xms1g -Xmx1g -Xmn512m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M \
-Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause app.jar

# config
docker run -d --net=host --name=config --restart=always  --memory=1G --memory-swap=1500M --init -e "CONFIG_LABEL=release" \
-e "EUREKA_NODE_1_HOST=10.110.18.198" -e "EUREKA_NODE_2_HOST=10.110.18.171" -e "EUREKA_NODE_3_HOST=10.110.18.172" 10.150.18.14:6000/config \
java -jar -Xms1g -Xmx1g -Xmn512m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M \
-Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause app.jar

# monitor
docker run -d --net=host --name=monitor --restart=always --memory=1G --memory-swap=1500M --init \
-e "EUREKA_NODE_1_HOST=10.110.18.198" -e "EUREKA_NODE_2_HOST=10.110.18.171" -e "EUREKA_NODE_3_HOST=10.110.18.172" 10.150.18.14:6000/monitor \
java -jar -Xms1g -Xmx1g -Xmn512m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M \
-Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause app.jar

# gateway
docker run -d --net=host --name=gateway --restart=always --memory=2G --memory-swap=3G --init -e "CONFIG_LABEL=release" \
-e "EUREKA_NODE_1_HOST=10.110.18.198" -e "EUREKA_NODE_2_HOST=10.110.18.171" -e "EUREKA_NODE_3_HOST=10.110.18.172" 10.150.18.14:6000/gateway \
java -jar -XX:+UseG1GC -Xms2g -Xmx2g -XX:MaxGCPauseMillis=200 -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps \
-XX:+PrintGCCause -XX:+HeapDumpOnOutOfMemoryError app.jar
# datasup
docker run -d --net=host --name=datasup --restart=always --memory=2G --memory-swap=3G --init -e "CONFIG_LABEL=release" \
-e "EUREKA_NODE_1_HOST=10.110.18.198" -e "EUREKA_NODE_2_HOST=10.110.18.171" -e "EUREKA_NODE_3_HOST=10.110.18.172" 10.150.18.14:6000/datasup \
java -jar -XX:+UseG1GC -Xms2g -Xmx2g -XX:MaxGCPauseMillis=200 -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps \
-XX:+PrintGCCause -XX:+HeapDumpOnOutOfMemoryError app.jar

# env-service
docker run -d --net=host --name=env-service --restart=always --memory=4G --memory-swap=5G --init -e "CONFIG_LABEL=release" \
-e "EUREKA_NODE_1_HOST=10.110.18.198" -e "EUREKA_NODE_2_HOST=10.110.18.171" -e "EUREKA_NODE_3_HOST=10.110.18.172" 10.150.18.14:6000/env-service \
java -jar -XX:+UseG1GC -Xms4g -Xmx4g -XX:MaxGCPauseMillis=200 -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps \
-XX:+PrintGCCause -XX:+HeapDumpOnOutOfMemoryError app.jar

# auth-server
docker run -d --net=host --name=auth-server --restart=always --memory=1G --memory-swap=1500M --init -e "CONFIG_LABEL=release" \
-e "EUREKA_NODE_1_HOST=10.110.18.198" -e "EUREKA_NODE_2_HOST=10.110.18.171" -e "EUREKA_NODE_3_HOST=10.110.18.172" 10.150.18.14:6000/auth-server \
java -jar -XX:+UseG1GC -Xms1g -Xmx1g -XX:MaxGCPauseMillis=200 -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps \
-XX:+PrintGCCause -XX:+HeapDumpOnOutOfMemoryError app.jar

#admin
docker run -d --net=host --name=admin --restart=always --init --memory=2G --memory-swap=3G --init -e "CONFIG_LABEL=release" \
-e "EUREKA_NODE_1_HOST=10.110.18.198" -e "EUREKA_NODE_2_HOST=10.110.18.171" -e "EUREKA_NODE_3_HOST=10.110.18.172" 10.150.18.14:6000/admin \
java -jar -XX:+UseG1GC -Xms2g -Xmx2g -XX:MaxGCPauseMillis=200 -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps \
-XX:+PrintGCCause -XX:+HeapDumpOnOutOfMemoryError app.jar