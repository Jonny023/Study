# 空值处理

```sql

SUM(COALESCE(nullif("age",'0'),'0')::DECIMAL) as "age"
```
