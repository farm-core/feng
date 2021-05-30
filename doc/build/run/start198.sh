#!/bin/bash
# 198服务器发布服务脚本
#源码根路径
source_root="/home/clear/datacenter/"
#jar包根路径
app_root="/home/clear/project/"
#基础docker镜像
base_image="10.150.18.14:6000/baseimage:v1"
#base_image="10.150.18.14:6000/baseimage:v2" 带有python环境
#docker容器工作目录
work_dir="/home/clear/program/"

#构建服务jar包路径
function app_dir() {
    app_name=$1
    if [[ "eureka" == "$app_name" ]] || [[ "$app_name" == "config" ]] || [[ "$app_name" == "monitor" ]] || [[ "$app_name" == "admin" ]]; then
        echo "${app_root}center/${app_name}/"
    elif [[ "$app_name" == cmacast* ]]; then
        echo "${app_root}cmacast/${app_name}/"
    elif [[ "$app_name" == env* ]]; then
        echo "${app_root}env/${app_name}/"
    elif [[ "$app_name" == pic* ]]; then
        echo "${app_root}picture/${app_name}/"
    elif [[ "$app_name" == auth* ]]; then
        echo "${app_root}authority/${app_name}/"
    elif [[ "$app_name" == idb* ]]; then
        echo "${app_root}idb/${app_name}/"
    elif [[ "$app_name" == time* ]]; then
        echo "${app_root}time/${app_name}/"
    else
        echo "${app_root}${app_name}/"
    fi
}
#构建项目模块相对路径
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
    elif [[ "$app_name" == idb* ]]; then
        echo "idb/${app_name}/"
    elif [[ "$app_name" == time* ]]; then
        echo "time/${app_name}/"
    else
        echo "${app_name}/"
    fi
}

#将jar包复制到远程服务器
function copy_remote_app() {
    list=$1
    dir=$2
    jar=${dir}"*.jar"
    array=(${list//,/ })
    for v in ${array[@]}
    do
      echo ${v}
    done

    for var in ${array[@]}
    do
        scp  ${jar}  clear@${var}:${dir}
        if [[ $? != 0 ]]; then
        ssh ${var} "mkdir -p ${dir}"
        scp ${jar} clear@${var}:${dir}
        fi
        if [[ $? == 0 ]]; then
        echo "$var copy completed"
        fi
    done
}

function run_remote_app() {
    list=$1
    name=$2
    a_dir=$(app_dir ${name})
    jar_name="datacenter-"${name}".jar"

    if [[ "$name" == "gateway" ]] || [[ "$name" == "datasup" ]]; then
        docker_run="docker run -d --net=host --name ${name} --restart always --memory=2G --memory-swap=3G --cpus=4 --init --volume ${a_dir}:${work_dir} ${base_image} \
        java -jar -XX:+UseG1GC -Xms2g -Xmx2g -XX:MaxGCPauseMillis=200 -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps \
        -XX:+PrintGCCause -XX:+HeapDumpOnOutOfMemoryError -Duser.timezone=GMT+8 ${jar_name}"
    elif [[ "$name" == "cmacast-aws-service" ]] || [[ "$name" == "env-service" ]]; then
        docker_run="docker run -d --net=host --name ${name} --restart always --memory=4G --memory-swap=6G --cpus=4 --init --volume ${a_dir}:${work_dir} ${base_image} \
        java -jar -Xms4096m -Xmx4096m -Xmn2048m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=256M \
        -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause -Duser.timezone=GMT+8 ${jar_name}"
    else
        docker_run="docker run -d --net=host --name ${name} --restart always --memory=1500M --memory-swap=2G --cpus=4 --init --volume ${a_dir}:${work_dir} ${base_image} \
        java -jar -Xms1g -Xmx1g -Xmn512m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=256M \
        -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause -Duser.timezone=GMT+8 ${jar_name}"
    fi

    array=(${list//,/ })
    for var in ${array[@]}
    do
	    d_run="ssh ${var} \"${docker_run}\""
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
    jar_name="datacenter-"${name}".jar"
    m_dir=$(module_dir "${name}")
    mvn clean package -DskipTests=true -pl ${m_dir}/ -am
    docker restart ${name}
}

function run_app() {
    name=$1
    profile=$2
    # shellcheck disable=SC2027
    jar_name="datacenter-"${name}".jar"

    if [[ -n "$profile" ]]; then
        profile="-P"${profile}
    fi

    m_dir=$(module_dir "${name}")
    a_dir=$(app_dir "${name}")

    if [[ ! -d "${source_root}${m_dir}" ]]; then
        echo "$name 未找到"
        return 0
    fi
    mvn clean package -DskipTests=true -DskipDocker -pl ${m_dir}/ -am ${profile}

#    特殊命令，设置不同的jvm参数
    if [[ "$name" == "gateway" ]] || [[ "$name" == "datasup" ]]; then
        docker run -d --net=host --name ${name} --restart always --memory=2G --memory-swap=3G --cpus=4 --init --volume ${a_dir}:${work_dir} ${base_image} \
        java -jar -XX:+UseG1GC -Xms2g -Xmx2g -XX:MaxGCPauseMillis=200 -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps \
        -XX:+PrintGCCause -XX:+HeapDumpOnOutOfMemoryError -Duser.timezone=GMT+8 ${jar_name}
    elif [[ "$name" == "cmacast-aws-service" ]] || [[ "$name" == "env-service" ]]; then
        docker run -d --net=host --name ${name} --restart always --memory=4G --memory-swap=6G --cpus=4 --init --volume ${a_dir}:${work_dir} ${base_image} \
        java -jar -Xms4096m -Xmx4096m -Xmn2048m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=256M \
        -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause -Duser.timezone=GMT+8 ${jar_name}
    elif [[ "$name" == idb* ]]; then
        docker run -d --net=host --name ${name} --restart always --memory=4G --memory-swap=6G --cpus=4 --init --volume ${a_dir}:${work_dir} --volume /mnt:/mnt ${base_image} \
        java -jar -Xms4096m -Xmx4096m -Xmn2048m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=256M \
        -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause -Duser.timezone=GMT+8 ${jar_name}
    if [[ "$name" == time* ]]; then
        docker run -d --net=host --name ${name} --restart always --memory=2G --memory-swap=3G --cpus=4 --init --volume ${a_dir}:${work_dir} ${base_image} \
        java -jar -XX:+UseG1GC -Xms2g -Xmx2g -XX:MaxGCPauseMillis=200 -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps \
        -XX:+PrintGCCause -XX:+HeapDumpOnOutOfMemoryError -Duser.timezone=GMT+8 ${jar_name}
    else
        docker run -d --net=host --name ${name} --restart always --memory=1500M --memory-swap=2G --cpus=4 --init --volume ${a_dir}:${work_dir} ${base_image} \
        java -jar -Xms1024m -Xmx1024m -Xmn512m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=256M \
        -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause -Duser.timezone=GMT+8 ${jar_name}
    fi

    echo "*********$name 服务开始运行 ********************************************************"
    return 1
}


#main() {
    type=$1
    branch=$2
    app_list=$3
    echo "脚本开始执行"
    # shellcheck disable=SC2164
    cd ${source_root}
    echo "******************************<更新代码>**************************************************************"
    git pull origin $branch
    echo "******************************<代码更新完成>***********************************************************"
    # shellcheck disable=SC2206
    app_array=(${app_list//,/ })
    if [[ "$type" == "restart" ]]; then
        # shellcheck disable=SC2068
        for app in ${app_array[@]}
        do
            restart_app ${app}
        done
    elif [[ "$type" == "start" ]]; then
        for app in ${app_array[@]}
        do
            run_app ${app}
        done
    elif [[ "$type" == "remote" ]]; then
        read remote_addr
        for app in ${app_array[@]}
        do
            a_dir0=$(app_dir ${app})
            copy_remote_app ${remote_addr} ${a_dir0}
        done

        for app in ${app_array[@]}
        do
            run_remote_app ${remote_addr} ${app}

        done
    elif [[ "$type" == "remote_restart" ]]; then
        read remote_addr
        for app in ${app_array[@]}
        do
            a_dir0=$(app_dir ${app})
            copy_remote_app ${remote_addr} ${a_dir0}
        done

        for app in ${app_array[@]}
        do
            restart_remote_app ${remote_addr} ${app}
        done
    fi
#}
#main



