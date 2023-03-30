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
-p 10911:10911 \
-p 10909:10909 \
-v /etc/rocketmq/data/broker/logs:/root/logs \
-v /etc/rocketmq/data/broker/store:/root/store \
-v /etc/rocketmq/conf/broker.conf:/opt/rocketmq-4.3.2/conf/broker.conf \
-e "NAMESRV_ADDR=namesrv:9876" \
-e "MAX_POSSIBLE_HEAP=200000000" \
rocketmqinc/rocketmq \
sh mqbroker -c /opt/rocketmq-4.3.2/conf/broker.conf 

```
