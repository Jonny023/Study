# docker安装kafka

### 启动docker

```shell
# 查看状态
systemctl status docker

# 重启
systemctl restart docker

# 启动
systemctl start docker

# 停止
systemctl stop docker
```

> Get https://registry-1.docker.io/v2/: read tcp 10.113.74.246:55770->54.161.109.204:443: read: connection reset by peer

```shell
# 编辑配置，修改镜像地址
vim /etc/docker/daemon.json

{
"registry-mirrors":[
    "https://registry.docker-cn.com",
    "https://docker.mirrors.ustc.edu.cn",
    "http://hub-mirror.c.163.com",
    "https://cr.console.aliyun.com/"
  ]
}

# 重启docker
systemctl daemon-reload 
systemctl restart docker
```

### 下载kafka和zookeeper镜像

```shell
# 下载zookeeper
docker pull wurstmeister/zookeeper

# 下载kafka
docker pull wurstmeister/kafka:2.11-0.11.0.3
```

### 启动镜像

```shell
docker run -d --name zookeeper -p 2181:2181 -v /etc/localtime:/etc/localtime wurstmeister/zookeeper

docker run  -d --name kafka -p 9092:9092 -e KAFKA_BROKER_ID=0 -e KAFKA_ZOOKEEPER_CONNECT=10.113.74.246:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://10.113.74.246:9092 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 -t wurstmeister/kafka

docker ps -a
```

### 测试kafka

```shell
# 运行生产者发送消息
docker exec -it kafka /bin/bash
cd /opt/kafka_2.13-2.7.0/bin
./kafka-console-producer.sh --broker-list localhost:9092 --topic test

# 新开一个窗口连接消费者消费
docker exec -it kafka /bin/bash
cd /opt/kafka_2.13-2.7.0/bin
./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning

# 查看topic
./kafka-topics.sh --zookeeper 10.113.74.246:2181 --list

# 创建topic
./bin/kafka-topics.sh --create --topic USER_SERVICE.TOPIC1 --partitions 8 --replication-factor 3 --zookeeper 192.168.1.20:2181
```

### 问题

> Error response from daemon: Container 09bf9eeb0bc48f7d580421e5d8ddfa05d537c34b675dba7fe4628d930e083f27 is not running

```bash
docker start 09bf9eeb0bc48f7d580421e5d8ddfa05d537c34b675dba7fe4628d930e083f27
```

> SpringBoot整合kafka报could not be established. Broker may not be available.

* 进入容器

```
docker exec -it kafka /bin/bash && cd /opt/kafka_2.13-2.7.0/config
```

* 修改kafka的server.properties

```properties
listeners=PLAINTEXT://kafka-host:9092
advertised.listeners=PLAINTEXT://kafka-host:9092
```

### docker命令

```bash
# 查看所有镜像
docker images

# 查看容器
docker ps -a

# 删除容器
docker rm 09bf9eeb0bc4
```

