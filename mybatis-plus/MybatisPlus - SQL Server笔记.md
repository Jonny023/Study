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
