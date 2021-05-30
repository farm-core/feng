#!/usr/bin/env bash

docker run -d -p 8848:8081 -v /public/nexus/nexus-data:/nexus-data --name nexus --restart=always \
-e INSTALL4J_ADD_VM_PARAMS="-Xms3g -Xmx3g -XX:MaxDirectMemorySize=3g  -Djava.util.prefs.userRoot=/nexus-data" sonatype/nexus3