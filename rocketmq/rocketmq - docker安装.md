## docker安装rocketmq

> 通过docker部署rocketmq导致外部无法连接，分片ip为容器的172网段



* 创建namesrv服务

```sh
docker run -d \
--restart=always \
--name rmqnamesrv \
-p 9876:9876 \
-v /etc/rocketmq/data/namesrv/logs:/root/logs \
-v /etc/rocketmq/data/namesrv/store:/root/store \
-e "MAX_POSSIBLE_HEAP=100000000" \
rocketmqinc/rocketmq:4.3.2 \
sh mqnamesrv 

```

* 创建配置存放目录（宿主机）

```sh
mkdir -p /etc/rocketmq/data/namesrv/logs /etc/rocketmq/data/namesrv/store
mkdir -p /etc/rocketmq/data/broker/logs /etc/rocketmq/data/broker/store /etc/rocketmq/conf
chmod -R 777 /etc/rocketmq
```


* 创建配置(宿主机)

> vi /etc/rocketmq/conf/broker.conf

```properties
# 所属集群名称，如果节点较多可以配置多个
brokerClusterName = DefaultCluster
#broker名称，master和slave使用相同的名称，表明他们的主从关系
brokerName = broker-a
#0表示Master，大于0表示不同的slave
brokerId = 0
#表示几点做消息删除动作，默认是凌晨4点
deleteWhen = 04
#在磁盘上保留消息的时长，单位是小时
fileReservedTime = 48
#有三个值：SYNC_MASTER，ASYNC_MASTER，SLAVE；同步和异步表示Master和Slave之间同步数据的机制；
brokerRole = ASYNC_MASTER
#刷盘策略，取值为：ASYNC_FLUSH，SYNC_FLUSH表示同步刷盘和异步刷盘；SYNC_FLUSH消息写入磁盘后才返回成功状态，ASYNC_FLUSH不需要；
flushDiskType = ASYNC_FLUSH
# 设置broker节点所在服务器的ip地址
brokerIP1 = 192.168.56.101
# 磁盘使用达到95%之后,生产者再写入消息会报错 CODE: 14 DESC: service not available now, maybe disk full
diskMaxUsedSpaceRatio=95

```

* 构建broker

```sh
docker run -d  \
--restart=always \
--name rmqbroker \
--link rmqnamesrv:namesrv \
-p 10909:10909 \
-p 10911:10911 \
-p 10912:10912 \
-v /etc/rocketmq/data/broker/logs:/root/logs \
-v /etc/rocketmq/data/broker/store:/root/store \
-v /etc/rocketmq/conf/broker.conf:/opt/rocketmq-4.3.2/conf/broker.conf \
-e "NAMESRV_ADDR=namesrv:9876" \
-e "MAX_POSSIBLE_HEAP=200000000" \
rocketmqinc/rocketmq:4.3.2 \
sh mqbroker -c /opt/rocketmq-4.3.2/conf/broker.conf 

```

## 可视化控制台

```sh
docker run -d \
--restart=always \
--name rmqadmin \
-e "JAVA_OPTS=-Drocketmq.namesrv.addr=192.168.56.101:9876 \
-Dcom.rocketmq.sendMessageWithVIPChannel=false" \
-p 8080:8080 \
pangliang/rocketmq-console-ng
```

## rocketmq控制台点击【生产者】搜索报错

> 原因是程序调用发送消息后就shutdown了，若想要看效果可以注释掉shutdown()方法，搜索时选择生产者对应的topic以及生产者组

```java
// 关闭生产者实例
//producer.shutdown();
```

## 可视化控制台2

```sh
docker run -d --name rocketmq-dashboard -e "JAVA_OPTS=-Drocketmq.namesrv.addr=192.168.56.101:9876" -p 8888:8080 -t apacherocketmq/rocketmq-dashboard:latest
```

> 可能出现问题，docker pull 是一直waiting

```sh
[root@localhost ~]# docker run -d --name rocketmq-dashboard -e "JAVA_OPTS=-Drocketmq.namesrv.addr=192.168.56.101:9876" -p 8888:8080 -t youlixishi/rocket                                                         mq-dashboard
Unable to find image 'youlixishi/rocketmq-dashboard:latest' locally
latest: Pulling from youlixishi/rocketmq-dashboard
2d473b07cdd5: Retrying in 1 second
07f579ac0467: Retrying in 1 second
bc5957953950: Retrying in 1 second
5d5200b5890f: Waiting
c68650c06f95: Waiting
463e6149cbb7: Waiting
e38e6ba6d8ed: Waiting
5f7a753fba61: Waiting
1e039b1cde67: Waiting
b8adbfd02420: Waiting
0f1e390e1026: Waiting
a6b906c9838c: Waiting

```

### 解决方法

> 更换docker镜像源地址

```sh
# 创建目录
mkdir -p /etc/docker

# 写入
tee /etc/docker/daemon.json <<-'EOF'
{
   "registry-mirrors": ["https://9cpn8tt6.mirror.aliyuncs.com"]
}
EOF

# 重启服务
systemctl daemon-reload
systemctl restart docker

# 查看配置
cat /etc/docker/daemon.json 
```

# 清空mq数据重启
```sh
rm -rf /etc/rocketmq/data/broker/logs/rocketmqlogs/*
rm -rf /etc/rocketmq/data/broker/store/*
rm -rf /etc/rocketmq/data/namesrv/logs/*
rm -rf /etc/rocketmq/data/namesrv/store/*
```
