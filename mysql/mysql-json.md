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



### json数组

```sql
select json from test;

-- 第2个参数必须为json格式中的内容
select * from test where JSON_CONTAINS(json->'$[*].id', '"1001"', '$');

-- "$[1].id"
select JSON_SEARCH(json, 'all', '1002') from test ;

-- 精确匹配
select * from test where JSON_SEARCH(json, 'all', '1002') is not null;

-- 模糊匹配
select * from test where JSON_SEARCH(json, 'all', '100%') is not null;


select * from (select '{"id": "1001", "name": "张三"}' as json) temp where JSON_EXTRACT(json, '$.id') = '1001';

-- "$[0].id"
select json_search('[{"id": "1001", "name": "张三"},{"id": "1002", "name": "李四"}]', 'all', '1001%');

-- ["1001", "1002"]
select JSON_EXTRACT('[{"id": "1001", "name": "张三"},{"id": "1002", "name": "李四"}]', '$**.id');
```

