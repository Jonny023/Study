## 查看docker资源使用

[参考](https://www.yisu.com/zixun/445257.html)

`PID`：进程的ID
`USER`：进程所有者
`PR`：进程的优先级别，越小越优先被执行
`NInice`：值
`VIRT`：进程占用的虚拟内存
`RES`：进程占用的物理内存
`SHR`：进程使用的共享内存
`S`：进程的状态。S表示休眠，R表示正在运行，Z表示僵死状态，N表示该进程优先值为负数
`%CPU`：进程占用CPU的使用率
`%MEM`：进程使用的物理内存和总内存的百分比
`TIME+`：该进程启动后占用的总的CPU时间，即占用CPU使用时间的累加值。
`COMMAND`：进程启动命令名称

```shell
#查看占用过高的应用
docker stats  #动态实时显示
docker stats --no-stream  #静态显示

#使用top命令查询占用过高的应用
top -c -b -o +%MEM | head -n 20 | tail -15

#修改compose file(版本小于v3）
mem_limit:  10g

#如果compose版本大于v3
deploy:
  resources:
    limits:
      # cpus: '0.5'  一般不设置，0.5代表单核50%的用量
      memory: 4G
    reservations:
      # cpus: '0.2'
      memory: 200M

#重启相关容器
docker-compose up -d evo-basic evo-rcs
```

**Dockerfile增加jvm参数**

```dockerfile
CMD java  $JAVA_OPTIONS -jar java-container.jar
```

**docker run**

```shell
docker run -d --name mycontainer8g -p 8080:8080 -m 800M -e JAVA_OPTIONS='-Xmx300m' rafabene/java-container:openjdk-env
```



### dockerfile配置jvm

[参考](https://segmentfault.com/a/1190000007271728)

```dockerfile
ENTRYPOINT ["java","-jar","-Xms2048m", "-Xmx2048m","-XX:+PrintGCDetails", "-XX:+PrintGCTimeStamps", "-XX:+HeapDumpOnOutOfMemoryError", "-XX:HeapDumpPath=/opt/infosec/cssp", "-Xloggc:/opt/infosec/cssp","/opt/infosec/cssp/cssp-csm.jar"]
```

```dockerfile
FROM java:8
VOLUME /tmp
ADD app.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 9001
ENV JAVA_OPTS="\
-server \
-Xmx2g \
-Xms2g \
-Xmn1g \
-XX:SurvivorRatio=8 \
-XX:MetaspaceSize=256m \
-XX:MaxMetaspaceSize=512m \
-XX:+UseParallelGC \
-XX:ParallelGCThreads=4 \
-XX:+UseParallelOldGC \
-XX:+UseAdaptiveSizePolicy \
-XX:+PrintGCDetails \
-XX:+PrintTenuringDistribution \
-XX:+PrintGCTimeStamps \
-XX:+HeapDumpOnOutOfMemoryError \
-XX:HeapDumpPath=/ \
-Xloggc:/gc.log \
-XX:+UseGCLogFileRotation \
-XX:NumberOfGCLogFiles=5 \
-XX:GCLogFileSize=10M"
ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.jar
    
 # 第二种
 ENTRYPOINT ["/bin/bash", "-c", "java -jar app.jar --spring.config.additional-location=$(ls /config/*.properties | tr '\n' ',')"]
```

| 配置                | 说明                                                      |
| ------------------- | --------------------------------------------------------- |
| -verbose:gc         | 显示GC的情况                                              |
| -Xms20M             | 设置堆初始大小20M                                         |
| -Xmx20M             | 设置堆最大大小20M                                         |
| -Xmn10M             | 设置堆中 new Generation 新生代的大小为10M                 |
| -XX:+PrintGCDetails | 输出GC的详细日志                                          |
| -XX:ServivorRatio=8 | 设置 New Generation新生代中的 Eden区与Servivor区比例是8:1 |
| -XX:+UseSerialGC    | 设置GC回收器模式是串型垃圾回收器                          |

## 删除none的镜像

```shell
docker images | grep none | awk '{print $3}' | xargs docker rmi
```



## 问题排查

```shell
#查看java进程
jps
jps -mlvV
jcmd pid GC.class_histogram 

#查看gc情况
jstat -gc pid
#每10s间隔采集20个样本 
jstat -gcutil -h10 pid 10000 20
jstat -gcnew -h3 pid 10000 20
jstat -gcmetacapacity pid
#输出到文件
jstack -l pid > jstack.log

jstack pid
jmap -histo pid

#导出dump
jstack pid > file.dump
jmap -dump:file=./a.hprof,format=b pid

#jhat 可以通过网页端口访问，默认端口7000
jhat a.hprof

#获得线程ID=的十六进制值
printf "%x\n" pid

jstack -l pid | grep pid对应的16进制 -A20
```

### dump关注点

`死锁`，Deadlock（**`重点关注`**）
`执行中`，Runnable
`等待资源`，Waiting on condition（**`重点关注`**）
`等待获取监视器`，Waiting on monitor entry（**`重点关注`**）
`暂停`，Suspended
`对象等待中`，Object.wait() 或 TIMED_WAITING
`阻塞`，Blocked（**`重点关注`**）
`停止`，Parked

* [dump在线分析](http://spotify.github.io/threaddump-analyzer/)

>Can't attach to the process: ptrace(PTRACE_ATTACH, ..) failed for 1: Operation not permitted

docker run添加参数：`docker run --cap-add SYS_PTRACE`

