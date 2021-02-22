# mysql特殊字符查询

* 查询`\`的内容，如：“c:\upload\demo.txt”，要实现模糊查询`%\upload\%`效果,用`\\\`表示`\`

```sql
SELECT
	content
FROM
	demo
where
	content like '%upload\\\%'
```
