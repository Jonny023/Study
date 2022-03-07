# kafka笔记

```sh
# 创建topic
./bin/kafka-topics.sh --create --topic topicName --partitions 4 --replication-factor 3 --zookeeper loclahost:2181

# 查看组消费情况
./kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group groupName

# 启动消费
./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic topicName --from-beginning
```

