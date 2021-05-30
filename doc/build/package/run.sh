#!/bin/bash
# 发布服务脚本
echo -n "输入部署的服务名："
read name
echo "停止'$name'服务"
pid=jps -l | grep $name | awk '{print $1}'
eval $(awk '{ printf("pid=%s",$1); }' ./t)
echo "停止服务pid $pid"
kill -9 $pid
if [ 'eureka' == $name ]; then
cd /home/clear/project/3clear-cloud
mvn clean package -DskipTests=true -pl 3clear-center/server-eureka/ -am
cd /home/clear/program/eureka
nohup java -jar -Xms1024m -Xmx1024m -Xmn640m -XX:SurvivorRatio=3 server-eureka.jar > /dev/null 2>&1 &
cd /home/clear/project/3clear-cloud
fi

if ['config' == $name]; then
mvn clean package -DskipTests=true -pl 3clear-center/server-config/ -am
cd /home/clear/program/config
nohup java -jar -Xms1024m -Xmx1024m -Xmn640m -XX:SurvivorRatio=3 server-config.jar > /dev/null 2>&1 &
cd /home/clear/project/3clear-cloud
fi

if ['monitor-admin' == $name]; then
echo -n "$name 服务正在编译"
mvn clean package -DskipTests=true -pl 3clear-monitor/monitor-admin/ -am
cd /home/clear/program/monitor-admin
echo -n "$name 服务正在运行"
nohup java -jar -Xms1024m -Xmx1024m -Xmn640m -XX:SurvivorRatio=3 monitor-admin.jar > /dev/null 2>&1 &
cd /home/clear/project/3clear-cloud
fi

cd /home/clear/program/monitor-zipkin
nohup java -jar -Xms1024m -Xmx1024m -Xmn512m -XX:SurvivorRatio=3 monitor-zipkin.jar > /dev/null 2>&1 &
cd /home/clear/project/3clear-cloud

if ['gateway-webflux' == $name]; then
mvn clean package -DskipTests=true -pl 3clear-gateway/gateway-webflux/ -am
cd /home/clear/program/gateway
nohup java -jar -Xms1024m -Xmx1024m -Xmn640m -XX:SurvivorRatio=3 gateway-webflux.jar > /dev/null 2>&1 &
cd /home/clear/project/3clear-cloud
fi

if ['gateway-zuul' == $name]; then
mvn clean package -DskipTests=true -pl 3clear-gateway/gateway-zuul/ -am
cd /home/clear/program/zuul
nohup java -jar -Xms1024m -Xmx1024m -Xmn640m -XX:SurvivorRatio=3 gateway-zuul.jar > /dev/null 2>&1 &
cd /home/clear/project/3clear-cloud
fi

if ['service-admin' == $name]; then
mvn clean package -DskipTests=true -pl 3clear-service/service-admin/ -am
cd /home/clear/program/service-admin
nohup java -jar -Xms1024m -Xmx1024m -Xmn640m -XX:SurvivorRatio=3 service-admin.jar > /dev/null 2>&1 &
cd /home/clear/project/3clear-cloud
fi

if ['author' == $name]; then
mvn clean package -DskipTests=true -pl 3clear-auth/author-server/ -am
cd /home/clear/program/author
nohup java -jar -Xms1024m -Xmx1024m -Xmn640m -XX:SurvivorRatio=3 author-server.jar > /dev/null 2>&1 &
cd /home/clear/project/3clear-cloud
fi

if ['authen' == $name]; then
mvn clean package -DskipTests=true -pl 3clear-auth/authen-server/ -am
cd /home/clear/program/authen
nohup java -jar -Xms1024m -Xmx1024m -Xmn640m -XX:SurvivorRatio=3 authen-server.jar > /dev/null 2>&1 &
cd /home/clear/project/3clear-cloud
fi

docker run -d -p 9411:9411 --name zipkin --restart always --volume /home/clear/program:/program/ \
--env RABBIT_ADDRESSES=10.110.18.205:5672 --env RABBIT_USER=admin --env RABBIT_PASSWORD=admin \
--env STORAGE_TYPE=elasticsearch --env ES_HOSTS=http://10.110.18.205:9200 \
--memory=1024M --memory-swap=1536M baseimage:v1.0 \
java -jar -Xms1024m -Xmx1024m -Xmn640m -XX:SurvivorRatio=3 /program/zipkin-server-2.12.9-exec.jar

if ['zipkin-server' == $name]; then
cd /home/clear/program/monitor-zipkin
nohup java -jar -Xms1024m -Xmx1024m -Xmn640m -XX:SurvivorRatio=3 zipkin-server-2.12.9-exec.jar \
  --RABBIT_ADDRESSES=10.110.18.205:5672 --RABBIT_USER=admin  --RABBIT_PASSWORD=admin \
 --STORAGE_TYPE=elasticsearch  --ES_HOSTS=http://10.110.18.205:9200 > /dev/null 2>&1 &
cd /home/clear/project/3clear-cloud
fi

nohup java -jar -Xms1024m -Xmx1024m -Xmn640m -XX:SurvivorRatio=3 zipkin.jar \
  --RABBIT_ADDRESSES=10.110.18.205:5672 --RABBIT_USER=admin  --RABBIT_PASSWORD=admin \
  --ES_PIPELINE=tags-record-value \
 --STORAGE_TYPE=elasticsearch  --ES_HOSTS=http://10.110.18.205:9200 > /dev/null 2>&1 &

 nohup java -jar -Xms1024m -Xmx1024m -Xmn640m -XX:SurvivorRatio=3 zipkin.jar \
  --RABBIT_ADDRESSES=10.150.18.18:5672 --RABBIT_USER=admin  --RABBIT_PASSWORD=admin \
  --ES_PIPELINE=tags-record-value \
 --STORAGE_TYPE=elasticsearch  --ES_HOSTS=http://10.150.18.17:9200 > /dev/null 2>&1 &


 nohup java -jar -Xms1024m -Xmx1024m -Xmn640m -XX:SurvivorRatio=3 zipkin.jar \
  --RABBIT_ADDRESSES=10.150.18.18:5672 --RABBIT_USER=admin  --RABBIT_PASSWORD=admin \
  --ES_PIPELINE=record_convert \
 --STORAGE_TYPE=elasticsearch  --ES_HOSTS=http://10.150.18.17:9200 > /dev/null 2>&1 &

 nohup java -jar -Xms1024m -Xmx1024m -Xmn640m -XX:SurvivorRatio=3 zipkin.jar \
  --RABBIT_ADDRESSES=10.150.18.18:5672 --RABBIT_USER=admin  --RABBIT_PASSWORD=admin \
  --ES_PIPELINE=zipkin-record-convert \
 --STORAGE_TYPE=elasticsearch  --ES_HOSTS=http://10.110.18.173:9200 > /dev/null 2>&1 &

RABBIT_ADDRESSES=10.110.18.205:5672
RABBIT_USER=admin
RABBIT_PASSWORD=admin
STORAGE_TYPE=elasticsearch
ES_HOSTS=http://10.110.18.205:9200

#awk -v pids=$(jps -l | awk '{printf $1","}')  '{printf substr(pids,0,  length(pids)-1 )}'

echo "查询所有jar包运行内存"
pids=`jps -l | awk '{printf $1","}'`
pidsub=${pids%,*}
top -M -p $pidsub
top=`top -s0 -M -p $pidsub`
jps=`jps -l`
awk 'FNR==NR{dx[$1]=$2; next}{print $1, dx[$1], $2, dx[$1]+$2}' jps top > merge
echo $merge