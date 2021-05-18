# SpringBoot无主键QueryDSL查询数据重复

### 问题

* 实体类无主键，启动报错，需要在某个字段上添加`@Id`注解，应用场景：视图查询，表数据只做插入、查询，无需更新（类似日志）
* 表中无主键，在任意字段上添加`@Id`后，数据记录重复，全部都是一样的结果，以下操作可以解决记录重复



> 将实体类中组合不重复的字段单独抽离到一个类中，必须实现序列化接口`Serializable`

```java
package com.example.springbootclickhousenoneid.entity;

import java.io.Serializable;
import java.util.Date;

public class DwsWebSummaryCountsKey implements Serializable {
  private String si;
  private Date eventTime;
}
```

> 实体类添加注解`@IdClass(DwsWebSummaryCountsKey.class)`，且必须添加`@Id`(加载任意字段都行)

```java
package com.example.springbootclickhousenoneid.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "dws_web_summary_counts")
@IdClass(DwsWebSummaryCountsKey.class)
public class DwsWebSummaryCounts implements Serializable {

  private static final long serialVersionUID = 5478600893211632322L;

  @Id
  @Column(name = "si")
  private String si;

  @Column(name = "event_time")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date eventTime;

  @Column(name = "pv")
  private Integer pv;

  @Column(name = "uv")
  private Integer uv;

  @Column(name = "ip_counts")
  private Integer ipCounts;

}
```

> 查询(数据不重复了)

```java
package com.example.springbootclickhousenoneid.controller;

import com.example.springbootclickhousenoneid.base.ResultDTO;
import com.example.springbootclickhousenoneid.entity.DwsWebSummaryCounts;
import com.example.springbootclickhousenoneid.entity.QDwsWebSummaryCounts;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

  @Resource
  private JPAQueryFactory jpaQueryFactory;

  @GetMapping("/")
  public ResultDTO index() {
    QDwsWebSummaryCounts entity = QDwsWebSummaryCounts.dwsWebSummaryCounts;
    JPAQuery<DwsWebSummaryCounts> query = jpaQueryFactory.selectFrom(entity);
    List<DwsWebSummaryCounts> list = query.fetch();
    return ResultDTO.ok(list);
  }
}
```

