## sqlserver 笔记

### 日期类型置空报错

> 如果只加`@TableField(updateStrategy = FieldStrategy.IGNORED)`会报错，不允许从数据类型 varbinary 到 date 的隐式转换。请使用 CONVERT 函数来运行此查询，需要设置jdbcType = JdbcType.DATE

```java
/**
 * 创建日期
 */
@TableField(updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DATE)
private Date createDate;
```

### 查询大小写敏感

> apply()方法传入的是sql，有sql注入风险，还需要注意apply方法会在sql中添加` AND xxxx`并不是直接拼接
```java
//针对查询
wrapper.apply("code COLLATE Chinese_PRC_CS_AI_WS = {0}", "A010");
wrapper.apply("code = {0}{1}", "A010", "COLLATE Chinese_PRC_CS_AI_WS");
```

> apply其他用法

```java
// 添加排序
queryWrapper.apply("ORDER BY create_time DESC");

// 添加分组
queryWrapper.apply("GROUP BY type"); 

// 添加limit
queryWrapper.apply("LIMIT 10");

// sqlserver使用top限定返回行数
queryWrapper.apply("top 10");

// 内连接
queryWrapper.apply("INNER JOIN user ON user.id = order.user_id");

queryWrapper.apply("FOR UPDATE").apply("LOCK IN SHARE MODE");
```
