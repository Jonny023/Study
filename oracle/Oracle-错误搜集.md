# 常见错误搜集

* 出现`invalid identifier`一般是类与数据库字段没对应上

```java
ORA-00904: "THIS_"."USERNAME": invalid identifier
```

# Oracle数据插入报错

* 错误提示


```bash
java.sql.SQLIntegrityConstraintViolationException: ORA-02290: 违反检查约束条件 (ROOT.SYS_C0011115)
```

* 打开`Navicat`--右键选中表--设计表--检查--去掉选中状态

![](https://javaweb-community.oss-cn-beijing.aliyuncs.com/2019/0514/30b3c0fdb2fc4e3e8852db5168671e35.png)
