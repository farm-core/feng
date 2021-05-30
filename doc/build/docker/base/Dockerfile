FROM 10.150.18.14:6000/alpine-jdk:latest
MAINTAINER 基础运行镜像 <zhanggk@3clear.com>
WORKDIR /home/clear/program
#COPY *.jar /home/clear/program/app.jar
#ENV APP app.jar
#增加clear用户
RUN set -eux; \
    addgroup --gid 1000 -S clear; \
    adduser clear -S -u 1000 -G clear -h /home/clear/ -s /bin/bash -D;
#设置文件权限
RUN chmod +x /home/clear/program -R; \
    chgrp clear /home/clear -R; \
    chown clear /home/clear -R;
USER clear
#CMD java -jar /home/clear/program/$APP