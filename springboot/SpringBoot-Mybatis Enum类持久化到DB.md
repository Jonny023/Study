# Mybatis Enum类持久化到DB

## 1.定义枚举类

* `@EnumValue`存入数据库的值
* `@JsonValue`序列化json的值

```java
package com.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Result {

    SUCCESS("SUCCESS","成功")
    , FAIL("FAIL","失败");

    @EnumValue
    private String code;

    @JsonValue
    private String msg;

    Result(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
```

## 2.实体类

```java
@TableName("test")
public class Test extends Model {
    
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 操作类型
     */
    private Result result;
}
```



