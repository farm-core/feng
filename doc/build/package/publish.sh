#!/bin/bash
# 发布服务脚本
type=$1
cd /home/clear/datacenter
echo "******************************<更新代码>**************************************************************"
git pull origin dev
echo "******************************<代码更新完成>***********************************************************"

echo "******************************<输入部署的服务名>*******************************************************"
read name

echo "******************************<输入部署服务配置文件profiles>*******************************************"
read profile

jar_name="datacenter-"${name}".jar"
if [[ -n "$profile" ]]; then
    profile="-P"${profile}
fi
echo ${jar_name}
echo ${profile}

if [[ "restart" == "$type" ]]; then
  echo "******************************<停止'$name'服务>********************************************************"
  eval $( jps -l | grep ${jar_name} | awk '{ printf("pid=%s",$1); }')
  if [[ -n "$pid" ]]; then
    echo -n "*************************<停止服务pid: $pid>**************************************************"
    kill -9 ${pid}
 else
    echo "****************************<没有查到服务pid>******************************************************"
    echo "****************************<退出脚本>*************************************************************"
    exit 1
 fi
fi

if [[ "eureka" == "$name" ]] || [[ "$name" == "config" ]] || [[ "$name" == "monitor" ]] || [[ "$name" == "admin" ]]; then
  echo "*********center : $name 服务开始打包        ********************************************************"
  cd /home/clear/datacenter
  mvn clean package -DskipTests=true -pl center/${name}/ -am ${profile}
  cd /home/clear/project/center/${name}
  nohup java -jar -Xms512m -Xmx512m -Xmn320m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause ${jar_name} > /dev/null 2>&1 &
  cd /home/clear/datacenter
  eval $( jps -l | grep ${jar_name} | awk '{ printf("pid=%s",$1); }')
  echo "********服务运行pid: $pid *******************************************************"

elif [[ "$name" == cmacast* ]]; then
  cd /home/clear/datacenter
  echo "*********camcast : $name 服务开始打包***********************************************************"
  mvn clean package -DskipTests=true -pl cmacast/${name}/ -am ${profile}
  cd /home/clear/project/cmacast/${name}
  echo "********服务开始运行************************************************************"
  nohup java -jar -Xms512m -Xmx512m -Xmn320m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause ${jar_name} > /dev/null 2>&1 &
  cd /home/clear/datacenter
  eval $( jps -l | grep ${jar_name} | awk '{ printf("pid=%s",$1); }')
  echo "********服务运行pid: $pid *******************************************************"

elif [[ "$name" == env* ]]; then
  cd /home/clear/datacenter
  echo "*********  env : $name 服务开始打包 ********************************************************"
  mvn clean package -DskipTests=true -pl env/${name}/ -am  ${profile}
  cd /home/clear/project/env/${name}
  echo -n "$name 服务正在运行"
  nohup java -jar -Xms512m -Xmx512m -Xmn320m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause ${jar_name} > /dev/null 2>&1 &
  cd /home/clear/datacenter
  eval $( jps -l | grep ${jar_name} | awk '{ printf("pid=%s",$1); }')
  echo "********服务运行pid: $pid *******************************************************"

elif [[ "$name" == pic* ]]; then
  cd /home/clear/datacenter
  mvn clean package -DskipTests=true -pl picture/${name}/ -am  ${profile}
  cd /home/clear/project/picture/${name}
  echo -n "$name 服务正在运行"
  nohup java -jar -Xms512m -Xmx512m -Xmn320m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause ${jar_name} > /dev/null 2>&1 &
  cd /home/clear/datacenter
  eval $( jps -l | grep ${jar_name} | awk '{ printf("pid=%s",$1); }')
  echo "********服务运行pid: $pid *******************************************************"

else
  if [[ ! -d "/home/clear/datacenter/$name/" ]]; then
    echo "没有找到服务"
  else
    cd /home/clear/datacenter
    mvn clean package -DskipTests=true -pl ${name}/ -am  ${profile}
    cd /home/clear/project/${name}
    echo -n "$name 服务正在运行"
    nohup java -jar -Xms2048m -Xmx2048m -Xmn1536m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=20 -XX:MetaspaceSize=128M -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause ${jar_name} > /dev/null 2>&1 &
    cd /home/clear/datacenter
  fi
fi