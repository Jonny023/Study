# 正则查询

* 匹配开头结尾

```sql
SELECT
	* 
FROM
	`tel` 
WHERE
	phone REGEXP '^[A-Z]{4}.[0-9]+.([A-Z]{,1})([0-9]+$)|^[A-Z]{4}.[0-9]+$'
```
