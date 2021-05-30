FROM 10.150.18.14:6000/alpine-jdk:latest
MAINTAINER 基础运行镜像 包含python运行环境 <zhanggk@3clear.com>
WORKDIR /home/clear/program
#python
RUN set -eux; \
    apk add --no-cache python3 && \
    ln -sf /usr/bin/python3 /usr/bin/python
#增加clear用户
RUN set -eux; \
    addgroup --gid 1000 -S clear; \
    adduser clear -S -u 1000 -G clear -h /home/clear/ -s /bin/bash -D;
#设置文件权限
RUN chmod +x /home/clear/program -R; \
    chgrp clear /home/clear -R; \
    chown clear /home/clear -R;
USER clear
RUN set -eux;\
    mkdir -p  /home/clear/.pip && \
    echo "index-url = https://mirrors.aliyun.com/pypi/simple" >  /home/clear/.pip/pip.conf
CMD ["python","--version"]