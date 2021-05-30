#!/bin/bash
# 发布服务脚本
source_root="/home/clear/datacenter/"
app_root="/home/clear/project/"

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
    else
        echo "${app_root}${app_name}/"
    fi
}

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
        docker_run="docker run -d --net=host --name ${name} --restart always --volume ${a_dir}:/home/clear/program/  openjdk:v0.1 \
        java -jar -XX:+UseG1GC -Xms2g -Xmx2g -XX:MaxGCPauseMillis=200 -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps \
        -XX:+PrintGCCause -XX:+HeapDumpOnOutOfMemoryError ${jar_name}"
    else
        docker_run="docker run -d --net=host --name ${name} --restart always --volume ${a_dir}:/home/clear/program/  openjdk:v0.1 \
        java -jar -Xms2g -Xmx2g -Xmn1024m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M \
        -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause ${jar_name}"
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
    mvn clean package -DskipTests=true -pl ${m_dir}/ -am ${profile}

    if [[ "$name" == "gateway" ]] || [[ "$name" == "datasup" ]]; then
        docker run -d --net=host --name ${name} --restart always --volume ${a_dir}:/home/clear/program/  openjdk:v0.1 \
        java -jar -XX:+UseG1GC -Xms2g -Xmx2g -XX:MaxGCPauseMillis=200 -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps \
        -XX:+PrintGCCause -XX:+HeapDumpOnOutOfMemoryError ${jar_name}
    else
        docker run -d --net=host --name ${name} --restart always --volume ${a_dir}:/home/clear/program/  openjdk:v0.1 \
        java -jar -Xms2048m -Xmx2048m -Xmn1024m -XX:SurvivorRatio=3 -XX:+UseParallelGC -XX:ParallelGCThreads=10 -XX:MetaspaceSize=128M \
        -Xloggc:'gc.log' -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause ${jar_name}
    fi

    echo "*********$name 服务开始运行 ********************************************************"
    return 1
}


#main() {
    type=$1
    branch=$2
    app_list=$3
    echo "脚本开始执行"
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



