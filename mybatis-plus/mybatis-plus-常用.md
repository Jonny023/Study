# mybatis-plus常用

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
```
