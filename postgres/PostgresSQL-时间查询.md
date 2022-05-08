## 时间条件查询

> 字符日期（时间戳）进行查询


```sql
select * from tableName where "create_time"::timestamp >= to_timestamp('1651991991')
```
