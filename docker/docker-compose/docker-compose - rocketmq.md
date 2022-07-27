## docker-compose部署rocketmq

### 1.拉取镜像

```sh
docker pull rocketmqinc/rocketmq:4.3.2
```

### 2.创建配置

* 创建文件

```sh
mkdir -p {/opt/rocketmq/conf,/opt/rocketmq/logs,/opt/rocketmq/store} & touch /opt/rocketmq/conf/broker.conf & touch /opt/rocketmq/docker-compose.yml

# 配置权限
chmod -R 777 /opt/rocketmq
```

* 写入配置

> vi /opt/rocketmq/conf/borker.conf

```properties
brokerClusterName=DefaultCluster
brokerName=broker-a
brokerId=0
deleteWhen=04
fileReservedTime=48
brokerRole=ASYNC_MASTER
flushDiskType=ASYNC_FLUSH
brokerIP1=192.168.110.140
defaultTopicQueueNums=4
autoCreateTopicEnable=true
autoCreateSubscriptionGroup=true
```

### 3.docker-compose.yml

> vi /opt/rocketmq/docker-compose.yml

```yaml
version: '2'
services:
  namesrv:
    image: rocketmqinc/rocketmq:4.4.0
    container_name: rmqnamesrv
    restart: always
    ports:
      - 9876:9876
    volumes:
      - /opt/rocketmq/logs:/home/rocketmq/logs
      - /opt/rocketmq/store:/home/rocketmq/store
    environment:
      JAVA_OPT_EXT: "-Duser.home=/home/rocketmq -server -Xms512m -Xmx512m -Xmn128m"
      TZ: Asia/Shanghai
    command: ["sh", "mqnamesrv"]
  broker:
    image: rocketmqinc/rocketmq:4.4.0
    container_name: rmqbroker
    restart: always
    ports:
      - 10909:10909
      - 10911:10911
      - 10912:10912
    volumes:
      - /opt/rocketmq/logs:/home/rocketmq/logs
      - /opt/rocketmq/store:/home/rocketmq/store
      - /opt/rocketmq/conf/broker.conf:/opt/rocketmq/conf/broker.conf
    #command: sh mqbroker -n namesrv:9876
    command: ["sh", "mqbroker", "-n", "namesrv:9876", "-c", "/opt/rocketmq/conf/broker.conf"]
    depends_on:
      - namesrv
    environment:
      #NAMESRV_ADDR: "namesrv:9876"
      #JAVA_HOME: /usr/lib/jvm/jre
      JAVA_OPT_EXT: "-Duser.home=/home/rocketmq -server -Xms768m -Xmx1024m -Xmn256m"
      TZ: Asia/Shanghai
  console:
    image: styletang/rocketmq-console-ng
    container_name: rocketmq-console-ng
    restart: always
    ports:
      - 8087:8080
    depends_on:
      - namesrv
    environment:
      - JAVA_OPTS= -Dlogging.level.root=info -Drocketmq.namesrv.addr=rmqnamesrv:9876
      - Dcom.rocketmq.sendMessageWithVIPChannel=false
      - TZ=Asia/Shanghai
```

### 4.启动

```
docker-compose up -d
```

### 5.访问地址

[http://192.168.110.140:8087](http://192.168.110.140:8087)



## 网络

```sh
# 查看容器信息
docker inspect 容器名称

# 查看容器网络
docker inspect rmqbroker -f "{{json .NetworkSettings.Networks}}"

# 所有本地网络
docker network ls

# 网络详情
docker network inspect 容器id
docker network inspect bridge|host|none
```

## 问题

> 是mq存储时间不对，可以检查操作系统的时间是否和网络时间同步

[时间同步](https://github.com/Jonny023/Study/blob/master/springcloud/mall/vagrant%E4%B8%8B%E8%BD%BD.md)
