# 空值处理

```sql
-- 无法处理空字符串
SUM(COALESCE(nullif("age",'0'),'0')::DECIMAL) as "age"

-- 空字符串
SUM(COALESCE(nullif((case when "age" = '' then '0' else "age" end),'0'),'0')::DECIMAL) as "age"

-- age为text类型
-- 解决null和空字符报错问题
SELECT sum(case when trim(age) = '' or age is null then 0 else trim(age)::numeric end) FROM test;
SELECT sum(coalesce(nullif(trim(age), ''), '0')::numeric) FROM test;

-- 当age为数值类型时用上面这个方法会报错
-- 兼容版本，将数值类型转换为text
SELECT sum(coalesce(nullif(trim(age::text), ''), '0')::numeric) FROM test;
```
