# json操作

```sql
-- {"u_name":"张三","age":20}
-- 获取name,age
SELECT *,JSON_EXTRACT(text, "$.u_name") name, JSON_EXTRACT(text, "$.age") age FROM demo;

-- where age = 20
SELECT *,JSON_KEYS(text) FROM demo where text -> '$.age' = 20;

-- 重命名属性名
-- 方式1【不破坏结构】
update demo set text = replace(text, '"u_name":', '"name":');

-- 方式2
update demo set text = json_insert(
	JSON_REMOVE(text, '$.u_name'), 
   '$.name', 
   JSON_EXTRACT(text, '$.u_name')
);

-- 方式3
update demo set text = json_insert(
	JSON_REMOVE(text, '$.u_name'), 
   '$.name', 
   text -> '$.u_name'
);
```

