## mysql分页

```sql
-- 完整写法，从下标为2处查询1条
SELECT * FROM `demo` limit 1 offset 2

-- 简写 从下标为1处开始查询2条
SELECT * FROM demo LIMIT 1,2
```
