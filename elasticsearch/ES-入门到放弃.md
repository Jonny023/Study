# ES新手入门

* 引入依赖

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

* 添加配置

```yaml
spring:
  data:
    elasticsearch:
      cluster-name: my-es #默认为elasticsearch
      cluster-nodes: 192.168.1.249:9300 #配置es节点信息，逗号分隔，如果没有指定，则启动ClientNode
      properties:
        path:
          logs: d:/elasticsearch/log #elasticsearch日志存储目录
          data: d:/elasticsearch/data #elasticsearch数据存储目录
```

## 具体代码

* 实体类

```java
package com.entity.es;

import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 *  indexName 索引名称 可以理解为数据库名 必须为小写 不然会报
 *  type类型 可以理解为表名
 */
@Document(indexName = "curio",type = "file")
public class FileEs implements Serializable {

    private Integer id;

    // 资源名称
    private String resName;

    // 资源描述
    private String resDesc;

    public FileEs() {
    }

    public FileEs(Integer id, String resName, String resDesc) {
        this.id = id;
        this.resName = resName;
        this.resDesc = resDesc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getResDesc() {
        return resDesc;
    }

    public void setResDesc(String resDesc) {
        this.resDesc = resDesc;
    }
}

```

* dao层

```java
package com.dao.es;

import com.entity.es.FileEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface FileEsDao extends ElasticsearchRepository<FileEs,Integer> {

}
```

* service层

```java
package com.service.es;

import com.utils.PageUtils;
import com.entity.es.FileEs;

public interface FileEsService {

    FileEs save(FileEs bean);

    PageUtils search();
}

```

* service实现

```java

```


## 遇到的错误

* 序列化为json字符串时报错
```
cannot deserialize from Object value (no delegate- or property-based Creator)
```

* 解决方法是给实体类添加一个空的构造器
