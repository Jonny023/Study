## mysql多列操作

### 多列操作

```sql
-- add支持多列，change/drop需要在每列前添加关键字，逗号隔开，'column'可有可无

-- 添加多列
alter table test add (c1 char(1),c2 char(1));	-- 正确，add支持多列
alter table test add column (c1 char(1),c2 char(1));	-- 正确
alter table test add c1 char(1),add c2 char(1);		-- 正确

-- 修改多列
alter table test change c1 c3 char(1),change c2 c4 char(1);		-- 正确
alter table test change column c1 c3 char(1),change column c2 c4 char(1);	-- 正确
-- name关键字作为字段名，重命名需要加反引号(`)
alter table table_name change `name` field_name varchar(50);

-- 删除多列
alter table test drop c1,drop c2;	-- 正确
alter table test drop column c1,drop column c2;	-- 正确
```

