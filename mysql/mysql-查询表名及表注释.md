> 查询表名及注释

```sql
SELECT
	table_name 表名,
	table_comment 表说明 
FROM
	information_schema.TABLES 
WHERE
	table_schema = '数据库名'
```
