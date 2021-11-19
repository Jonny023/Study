# docker安装kafka

## 1.搜索镜像

```shell
docker search zookeeper
```

## 2.拉取镜像

```shell
docker pull wurstmeister/kafka
docker pull wurstmeister/zookeeper
```

## 3.运行容器

```shell
# 运行zk
docker run -d --name zookeeper -p 2181:2181 -t wurstmeister/zookeeper

# 运行kafka
docker run  -d --name kafka \
-p 9092:9092 \
-e KAFKA_BROKER_ID=0 \
-e KAFKA_ZOOKEEPER_CONNECT=192.168.56.101:2181 \
-e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://192.168.56.101:9092 \
-e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 -t wurstmeister/kafka

# 集群
docker run -d --name kafka1 \
-p 9093:9093 \
-e KAFKA_BROKER_ID=1 \
-e KAFKA_ZOOKEEPER_CONNECT=<宿主机IP>:2181 \
-e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://<宿主机IP>:9093 \
-e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9093 -t wurstmeister/kafka
```

## 4.创建topic

```shell
# 进入kafka容器
docker exec -it kafka bash

# 创建topic topic名称带有下划线会有警告
/opt/kafka/bin/kafka-topics.sh --create --zookeeper 192.168.56.101:2181 --replication-factor 1 --partitions 1 --topic my-topic

# 创建topic并指定分区
/opt/kafka/bin/kafka-topics.sh --create --topic my-topic1 --partitions 8 --replication-factor 3 --zookeeper 192.168.56.101:2181

# 运行生产者
/opt/kafka/bin/kafka-console-producer.sh --broker-list 192.168.56.101:9092 --topic my-topic

# 运行消费者
/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server 192.168.56.101:9092 --topic my-topic --from-beginning

# 删除topic
/opt/kafka/bin/kafka-topics.sh --delete --zookeeper 192.168.56.101:2181 --topic my_topic

# 查看list
/opt/kafka/bin/kafka-topics.sh --list --zookeeper 192.168.56.101:2181
```

## 5.分区数设置

```shell
#分区数量的作用：有多少分区就能负载多少个消费者，生产者会自动分配给分区数据，每个消费者只消费自己分区的数据，每个分区有自己独立的offset
#进入kafka容器
vi /opt/kafka/config/server.properties
修改run.partitions=2

#重启容器
docker restart kafka

#修改指定topic
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --alter --partitions 3 --topic topicname
```

