#### 在mybatis中，数据库字段为json数据，在mybatis程序中想要将它返回为对象

> test表如下数据

| id   | content                                         |
| ---- | ----------------------------------------------- |
| 1    | [{"id":1,"name":"张三"},{"id":2,"name":"李四"}] |


## com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler这个转换器为mybatisplus默认实现，还有json和fastjson的，需要可自行添加依赖使用

## 方式1

```xml
<resultMap id="myMap" type="com.example.domain.Test">
    <result property="id" column="id"/>
    <result property="content" column="content" typeHandler="com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler"/>
</resultMap>

<select id="list" resultMap="myMap">
    select * from test
</select>
```



## 方式2

```java
package com.example.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.example.handler.JSONHandler;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Map;

@Data
@TableName("test")
public class Test {

    @TableId
    private Long id;

    @TableField(jdbcType = JdbcType.VARCHAR, typeHandler = JacksonTypeHandler.class)
    private List<Map<String, Object>> content;
}
```

> 返回结果

```json
[{"id":1,"content":[{"id":1,"name":"张三"},{"id":2,"name":"李四"}]}]
```


## 若自定义转换器，需添加配置

> application.yml配置

```yaml
mybatis-plus:
  type-handlers-package: com.example.handler
```
