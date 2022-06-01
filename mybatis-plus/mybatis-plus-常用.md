# mybatis-plus常用

## 配置

```yaml
mybatis-plus:
  configuration: # MyBatis 原生支持的配置
    # 是否将sql打打印到控制面板
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    # 是否开启自动驼峰命名规则（camel case）映射
    mapUnderscoreToCamelCase: true
```

## 打印sql

```yaml
# 方式一
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl 
    
# 方式二 application.yml 中增加配置，指定 mapper 文件所在的包
logging:
  level:
    com.baomidou.example.mapper: debug
```

## 注解

```java
//允许字段设置null值（将原先的值置空为null）
@TableField(strategy=FieldStrategy.IGNORED)

//主键NONE,AUTO-自增,ASSIGN_ID-雪花算法
@TableId(value = "id", type = IdType.NONE)
private String id;

//非持久化
@TableField(exist=false)

//表名
@TableName(value = "user")

//逻辑删除
@TableLogic
private Boolean deleted;

//自动赋值，日期可以自动赋值，userId需要自己实现MetaObjectHandler接口
//自动赋值3.5.1版本日期类无需扩展即可赋值，低版本不确定是否需要手动赋值
@TableField(fill = FieldFill.INSERT_UPDATE)
private LocalDateTime createTime;
```

## 自动赋值

* 示例

```java

package com.example.mybatis.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

/**
 * @author test
 */
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private LocalDateTime birthday;

    private String addr;

    private Double score;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;

    @TableLogic
    private Boolean deleted;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getUpdator() {
        return updator;
    }

    public void setUpdator(Long updator) {
        this.updator = updator;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", birthday=" + birthday +
                ", addr='" + addr + '\'' +
                ", score=" + score +
                ", createTime=" + createTime +
                ", deleted=" + deleted +
                ", updator=" + updator +
                '}';
    }
}
```

* 扩展实现

```java
package com.example.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Component
public class ExtMetaObjectHandler implements MetaObjectHandler {

    private Long userId = 1L;

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "updator", Long.class, userId);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updator", Long.class, userId);
    }
}
```
