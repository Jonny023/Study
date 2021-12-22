# hive问题

无法连接hive

> Could not initialize class org.apache.hadoop.hive.conf.HiveConf$
>
> java.lang.NoClassDefFoundError: org/apache/hadoop/conf/Configuration

### 添加依赖

```properties
<properties>
	<hadoop.version>2.6.5</hadoop.version>
</properties>
<dependency>
    <groupId>org.apache.hadoop</groupId>
    <artifactId>hadoop-common</artifactId>
    <version>${hadoop.version}</version>
</dependency>
```

