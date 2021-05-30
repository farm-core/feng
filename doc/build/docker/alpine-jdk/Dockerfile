FROM alpine:latest
MAINTAINER 最小Java运行镜像,包含JDK bash <zhanggk@3clear.com>
# JDK
ADD jdk-8u221-linux-x64.tar.gz /usr/local

# 设置apk国内镜像源 ali
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories

# 安装glibc && bash
RUN apk add --no-cache bash
# 离线安装glibc
COPY sgerrand.rsa.pub /etc/apk/keys/
COPY glibc-2.30-r0.apk /
COPY glibc-bin-2.30-r0.apk /
COPY glibc-dev-2.30-r0.apk /
COPY glibc-i18n-2.30-r0.apk /
RUN  apk add --no-cache glibc-2.30-r0.apk && \
     apk add --no-cache glibc-bin-2.30-r0.apk && \
     apk add --no-cache glibc-dev-2.30-r0.apk && \
     apk add --no-cache glibc-i18n-2.30-r0.apk && \
     rm -rf glibc-2.30-r0.apk && \
     rm -rf glibc-bin-2.30-r0.apk && \
     rm -rf glibc-dev-2.30-r0.apk && \
     rm -rf glibc-i18n-2.30-r0.apk

# 设置语言环境变量
ENV PATH=/usr/glibc-compat/bin:$PATH
RUN localedef -i zh_CN -f UTF-8 zh_CN.UTF-8
ENV LANG=zh_CN.UTF-8

# 设置JAVA环境变量
ENV JAVA_HOME=/usr/local/jdk1.8.0_221
ENV CLASSPATH=$JAVA_HOME/bin
ENV PATH=.:$JAVA_HOME/bin:$PATH
CMD ["java","-version"]

# 设置容器时区
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone