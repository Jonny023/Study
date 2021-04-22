# kafka windows版

* 进入到`windows`目录下执行

```bash
zookeeper-server-start.bat ..\..\config\zookeeper.properties

kafka-server-start.bat ..\..\config\server.properties
```

1）、创建主题

```
kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic mytopic
```

2）、查看创建的主题

```
kafka-topics.bat --list --zookeeper localhost:2181
```

3）、启动生产者

```
kafka-console-producer.bat --broker-list localhost:9092 --topic mytopic
```

此时，可输入任意字符，等消费者启动后，就能收到了。

4）、启动消费者

```
kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic mytopic --from-beginning
```

5）、删除topic
# 逻辑删除
```
kafka-run-class.bat kafka.admin.TopicCommand --delete --topic mytopic --zookeeper localhost:2181
```

