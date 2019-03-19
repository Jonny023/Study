# 报错

> 错误提示

```
nested exception is java.lang.IllegalStateException: availableProcessors is already set to [4], rejecting [4]
```

> 解决方法

```java
@Configuration
public class ESConfig {
    @PostConstruct
    void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }
}
```

# 整合

> 依赖

```xml
<!--ElasticSearch-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
</dependency>
<!--需要引入transport-netty3-client，否则会启动报错-->
<dependency>
    <groupId>org.elasticsearch.plugin</groupId>
    <artifactId>transport-netty3-client</artifactId>
    <version>5.6.10</version>
</dependency>
```

> 新增配置文件`elasticsearch.properties`

```properties
es.host=127.0.0.1
es.port=9300
es.cluster-name=my-es
```

> 新建配置类

```java

package com.common.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @description: es配置类
 * @author: Lee
 * @create: 2019-03-18
 **/
@Component
@ConfigurationProperties(prefix = "es")
@PropertySource(value = "classpath:elasticsearch.properties", ignoreResourceNotFound = false, encoding = "UTF-8")
public class ESConfig {

    private Logger log = LoggerFactory.getLogger(ESConfig.class);

    private String host;

    private Integer port;

    private String clusterName;

    @PostConstruct
    void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    @Bean
    public TransportClient client() {
        try {

            // 9300是es的tcp服务端口
            InetSocketTransportAddress node = new InetSocketTransportAddress(InetAddress.getByName(host), port);

            // 设置es节点的配置信息
            Settings settings = Settings.builder().put("cluster.name", clusterName).build();

            // 实例化es的客户端对象
            TransportClient client = new PreBuiltTransportClient(settings);
            client.addTransportAddress(node);

            return client;
        } catch (UnknownHostException e) {
            log.error("ES连接失败，错误信息{}",e.getMessage());
            return null;
        }
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }
}
```
