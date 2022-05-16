# 空值处理

```sql
-- 无法处理空字符串
SUM(COALESCE(nullif("age",'0'),'0')::DECIMAL) as "age"

-- 空字符串
SUM(COALESCE(nullif((case when "age" = '' then '0' else "age" end),'0'),'0')::DECIMAL) as "age"
```
