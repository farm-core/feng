#!/bin/bash
# 发布服务脚本
type=$1
branch=$2
cd /home/clear/datacenter
echo "******************************<更新代码>**************************************************************"
git pull origin $branch
echo "******************************<代码更新完成>***********************************************************"

echo "******************************<输入部署的服务名>*******************************************************"
read name

#echo "******************************<输入部署的服务端口号>*******************************************************"
#read port

echo "******************************<输入部署服务配置文件profiles>*******************************************"
read profile

jar_name="datacenter-"${name}".jar"
if [[ -n "$profile" ]]; then
    profile="-P"${profile}
fi
echo "开始打包: $jar_name "
echo "打包分支：$profile "



if [[ "eureka" == "$name" ]] || [[ "$name" == "config" ]] || [[ "$name" == "monitor" ]] || [[ "$name" == "admin" ]]; then
  echo "*********center : $name 服务开始打包 ********************************************************"
  cd /home/clear/datacenter
  mvn clean package -DskipTests=true -pl center/${name}/ -am ${profile}
  echo "*********center : $name 服务打包完成 ********************************************************"
  if [[ "restart" == "$type" ]]; then
    echo "*********center重启 $name 服务>************************************************************"
    docker restart ${name}
    echo "*********center : $name 服务重启完成 ******************************************************"
    docker logs -f ${name}
  else
    if [[ "eureka" == "$name" ]]; then
        echo "输入节点1地址、端口"
        read host1
        read port1
        echo "输入节点2地址、端口"
        read host2
        read port2
        docker run -d --net=host --name ${name} --restart always --volume /home/clear/project/center/${name}/:/home/clear/program/  \
        -e "EUREKA_NODE_1_HOST=${host1}" -e "EUREKA_NODE_1_PORT=${port1}" -e "EUREKA_NODE_2_HOST=${host2}" -e "EUREKA_NODE_1_PORT=${port2}"  openjdk:v0.1 \
        java -jar -Xms1024m -Xmx1024m -Xmn512m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause ${jar_name}
        echo "*********center : $name 服务开始运行 ********************************************************"
        docker logs -f ${name}
    fi
    docker run -d --net=host --name ${name} --restart always --volume /home/clear/project/center/${name}/:/home/clear/program/  openjdk:v0.1 \
    java -jar -Xms1024m -Xmx1024m -Xmn512m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause ${jar_name}
    echo "*********center : $name 服务开始运行 ********************************************************"
    docker logs -f ${name}
  fi
elif [[ "$name" == cmacast* ]]; then
  cd /home/clear/datacenter
  echo "*********camcast : $name 服务开始打包********************************************************"
  mvn clean package -DskipTests=true -pl cmacast/${name}/ -am ${profile}
  echo "*********camcast : $name 服务打包完成********************************************************"
  if [[ "restart" == "$type" ]]; then
    echo "*********cmacast重启 $name 服务>************************************************************"
    docker restart ${name}
    echo "*********cmacast : $name 服务重启完成 ******************************************************"
    docker logs -f ${name}
  else
    docker run -d --net=host --name ${name} --restart always --volume /home/clear/project/cmacast/${name}/:/home/clear/program/  openjdk:v0.1 \
    java -jar -Xms1024m -Xmx1024m -Xmn512m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause ${jar_name}
    echo "*********camcast : $name 服务开始运行********************************************************"
    docker logs -f ${name}
  fi
elif [[ "$name" == env* ]]; then
  cd /home/clear/datacenter
  echo "*********env : $name 服务开始打包*************************************************************"
  mvn clean package -DskipTests=true -pl env/${name}/ -am  ${profile}
  echo "*********env : $name 服务打包完成*************************************************************"
  if [[ "restart" == "$type" ]]; then
    echo "*********env $name 服务>************************************************************"
    docker restart ${name}
    echo "*********env : $name 服务重启完成 ******************************************************"
    docker logs -f ${name}
  else
    docker run -d --net=host --name ${name} --restart always --volume /home/clear/project/env/${name}/:/home/clear/program/  openjdk:v0.1 \
    java -jar -Xms1024m -Xmx1024m -Xmn512m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause ${jar_name}
    echo "*********env : $name 服务开始运行*************************************************************"
    docker logs -f ${name}
  fi

elif [[ "$name" == pic* ]]; then
  echo "*********pic : $name 服务开始打包*************************************************************"
  cd /home/clear/datacenter
  mvn clean package -DskipTests=true -pl picture/${name}/ -am  ${profile}
  echo "*********pic : $name 服务打包完成*************************************************************"
  if [[ "restart" == "$type" ]]; then
    echo "*********pic 重启 $name 服务>************************************************************"
    docker restart ${name}
    echo "*********pic : $name 服务重启完成 ******************************************************"
    docker logs -f ${name}
  else
    docker run -d --net=host --name ${name} --restart always --volume /home/clear/project/picture/${name}:/home/clear/program/  openjdk:v0.1 \
    java -jar -Xms1024m -Xmx1024m -Xmn512m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause ${jar_name}
    echo "*********pic : $name 服务开始运行*************************************************************"
    docker logs -f ${name}
  fi

else
  if [[ ! -d "/home/clear/datacenter/$name/" ]]; then
    echo "没有找到服务"
  else
    echo "*********datacenter : $name 服务开始打包*****************************************************"
    cd /home/clear/datacenter
    mvn clean package -DskipTests=true -pl ${name}/ -am  ${profile}
    echo "*********datacenter : $name 服务打包完成*****************************************************"
    if [[ "restart" == "$type" ]]; then
      echo "*********datacenter重启 $name 服务>************************************************************"
      docker restart ${name}
      echo "*********datacenter : $name 服务重启完成 ******************************************************"
      docker logs -f ${name}
    else
      docker run -d --net=host --name ${name} --restart always --volume /home/clear/project/${name}/:/home/clear/program/  openjdk:v0.1 \
      java -jar -Xms2048m -Xmx2048m -Xmn1024m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=20 -XX:MetaspaceSize=128M -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause ${jar_name}
      echo "*********datacenter : $name 服务开始运行*****************************************************"
      docker logs -f ${name}
    fi
  fi
fi