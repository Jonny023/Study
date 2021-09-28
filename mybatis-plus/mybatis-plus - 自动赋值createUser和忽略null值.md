# mybatis-plus - 自动赋值createUser和忽略null值

* model类

```java
@TableName("mandril_diagram")
public class MandrilDiagram extends Model {
    
    //更新时如果为null忽略
	@TableField(updateStrategy = FieldStrategy.IGNORED)
    private String name;
    
    //新增时赋值
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    //修改时生效
    @TableField(updateStrategy = FieldStrategy.IGNORED, fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
```

* 赋值配置类

```java
package com.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.util.UserUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Component
public class MybatisObjectHandler implements MetaObjectHandler {

    @Resource
    private UserUtil userUtil;

    @Override
    public void insertFill(MetaObject metaObject) {
        setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        setFieldValByName("updateTime", LocalDateTime.now(), metaObject);

        setFieldValByName("createUser", userUtil.getUsername(), metaObject);
        setFieldValByName("createRealName", userUtil.getRealName(), metaObject);
        setFieldValByName("updateUser", userUtil.getUsername(), metaObject);
        setFieldValByName("updateRealName", userUtil.getRealName(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        setFieldValByName("updateUser", userUtil.getUsername(), metaObject);
        setFieldValByName("updateRealName", userUtil.getRealName(), metaObject);
    }
}
```

