#!/bin/bash
# 发布服务脚本
source_root="/home/clear/datacenter/"
app_root="/home/clear/project/"
default_remote_list="10.110.18.171,10.110.18.172"
docker_registry="10.150.18.14:6000"


function module_dir() {
    app_name=$1
    if [[ "eureka" == "$app_name" ]] || [[ "$app_name" == "config" ]] || [[ "$app_name" == "monitor" ]] || [[ "$app_name" == "admin" ]]; then
        echo "center/${app_name}/"
    elif [[ "$app_name" == cmacast* ]]; then
        echo "cmacast/${app_name}/"
    elif [[ "$app_name" == env* ]]; then
        echo "env/${app_name}/"
    elif [[ "$app_name" == pic* ]]; then
        echo "picture/${app_name}/"
    elif [[ "$app_name" == auth* ]]; then
        echo "authority/${app_name}/"
    else
        echo "${app_name}/"
    fi
}

function run_remote_app() {
    list=$1
    name=$2
    if [[ "$name" == "gateway" ]] || [[ "$name" == "datasup" ]]; then
        docker_cmd="docker run -d --net=host --name=${name} --restart=always   -e \"CONFIG_LABEL=release\" \
        -e \"EUREKA_NODE_1_HOST=10.110.18.198\" -e \"EUREKA_NODE_2_HOST=10.110.18.171\" -e \"EUREKA_NODE_3_HOST=10.110.18.172\" ${docker_registry}/${name} \
        java -jar -XX:+UseG1GC -Xms2g -Xmx2g -XX:MaxGCPauseMillis=200 -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps \
        -XX:+PrintGCCause -XX:+HeapDumpOnOutOfMemoryError app.jar"
    else
        docker_cmd="docker run -d --net=host --name=${name} --restart=always  -e \"CONFIG_LABEL=release\" \
        -e \"EUREKA_NODE_1_HOST=10.110.18.198\" -e \"EUREKA_NODE_2_HOST=10.110.18.171\" -e \"EUREKA_NODE_3_HOST=10.110.18.172\" ${docker_registry}/${name} \
        java -jar -Xms1024m -Xmx1024m -Xmn512m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M \
        -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause app.jar"
    fi

    array=(${list//,/ })
    for var in ${array[@]}
    do
        eval ssh ${var} docker stop ${name} && docker rm ${name}
        eval ssh ${var} docker image rm  ${docker_registry}/${name}
        eval ssh ${var} docker pull ${docker_registry}/${name}
	    d_run="ssh ${var} \"${docker_cmd}\""
	    echo "${d_run}"
	    eval ${d_run}
        if [[ $? == 0 ]]; then
	        echo "${var} run completed "
        fi
    done

}

function restart_remote_app() {
    list=$1
    name=$2
    array=(${list//,/ })
    for var in ${array[@]}
    do
        ssh ${var} "docker restart ${name}"
    done
}

function restart_app() {
    name=$1
    docker restart ${name}
}

function modify_app() {
    name=$1
    m_dir=$(module_dir "${name}")
    mvn clean package -DskipTests=true -pl ${m_dir}/ -am
    echo "停止并删除容器 ${name} "
    docker stop ${name} && docker rm ${name}
    docker_run "${name}"
}

function docker_run() {
    name=$1
    if [[ "$name" == "gateway" ]] || [[ "$name" == "datasup" ]]; then
        docker run -d --net=host --name=${name} --restart=always   -e "CONFIG_LABEL=release" \
        -e "EUREKA_NODE_1_HOST=10.110.18.198" -e "EUREKA_NODE_2_HOST=10.110.18.171" -e "EUREKA_NODE_3_HOST=10.110.18.172" ${docker_registry}/${name} \
        java -jar -XX:+UseG1GC -Xms2g -Xmx2g -XX:MaxGCPauseMillis=200 -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps \
        -XX:+PrintGCCause -XX:+HeapDumpOnOutOfMemoryError app.jar
    else
        docker run -d --net=host --name=${name} --restart=always  -e "CONFIG_LABEL=release" \
        -e "EUREKA_NODE_1_HOST=10.110.18.198" -e "EUREKA_NODE_2_HOST=10.110.18.171" -e "EUREKA_NODE_3_HOST=10.110.18.172" ${docker_registry}/${name} \
        java -jar -Xms1024m -Xmx1024m -Xmn512m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M \
        -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause app.jar
    fi

    echo "镜像启动运行 ${name}"
}

function build_app() {
    name=$1
    profile=$2

    if [[ -n "$profile" ]]; then
        profile="-P"${profile}
    fi

    m_dir=$(module_dir "${name}")

    if [[ ! -d "${source_root}${m_dir}" ]]; then
        echo "$name 未找到"
        return 0
    fi

    mvn clean package -DskipTests=true -pl ${m_dir}/ -am ${profile}

    echo "*********$name 服务开始运行 ********************************************************"
    return 1
}


    type=$1
    branch=$2
    app_list=$3
    echo "脚本开始执行 操作类型: ${type} 代码分支: ${branch} 服务列表: ${app_list} "
    cd ${source_root}
    echo "******************************<更新代码>**************************************************************"
    git pull origin $branch
    echo "******************************<代码更新完成>***********************************************************"
    app_array=(${app_list//,/ })
    if [[ "$type" == "restart" ]]; then
        for app in ${app_array[@]}
        do
            restart_app ${app}
        done
    elif [[ "$type" == "build" ]]; then
        for app in ${app_array[@]}
        do
            build_app ${app}
        done
    elif [[ "$type" == "run" ]]; then
        for app in ${app_array[@]}
        do
            docker_run ${app}
        done
    elif [[ "$type" == "start" ]]; then
        for app in ${app_array[@]}
        do
            build_app ${app}
            docker_run ${app}
        done
    elif [[ "$type" == "modify" ]]; then
        for app in ${app_array[@]}
        do
            modify_app ${app}
        done
    elif [[ "$type" == "remote" ]]; then
        echo "输入远程服务器IP，多个服务器使用逗号分隔"
        read remote_addr
        if [[ -z "$remote_addr" ]]; then
           remote_addr=${default_remote_list}
        fi
        echo "远程服务器列表 ${remote_addr}"
        for app in ${app_array[@]}
        do
            run_remote_app ${remote_addr} ${app}
        done
    elif [[ "$type" == "remote_restart" ]]; then
        echo "输入远程服务器IP，多个服务器使用逗号分隔"
        read remote_addr
        if [[ -z "$remote_addr" ]]; then
           remote_addr=${default_remote_list}
        fi
        echo "远程服务器列表 ${remote_addr}"
        for app in ${app_array[@]}
        do
            restart_remote_app ${remote_addr} ${app}

        done
    fi



