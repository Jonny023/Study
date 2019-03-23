# 查询指定库拥有某字段的表

```mysql
-- AND TABLE_NAME NOT LIKE 'vw%'   注释：排除视图
SELECT DISTINCT TABLE_NAME FROM information_schema.COLUMNS WHERE COLUMN_NAME = 'columnName' AND TABLE_SCHEMA='dbName' AND TABLE_NAME NOT LIKE 'vw%';
```

# 查询指定数据库所有的表名

```mysql
select table_name from information_schema.tables where table_schema='dbName' and table_type='base table';
```

# 查询指定数据库没有某字段的所有表

```
select table_name from information_schema.tables where table_schema='dbName' and table_type='base table'
AND TABLE_NAME NOT IN(
    SELECT DISTINCT TABLE_NAME  FROM information_schema.COLUMNS WHERE COLUMN_NAME = 'culumnName' AND TABLE_SCHEMA='dbName' AND TABLE_NAME NOT LIKE 'vw%'
);
```
