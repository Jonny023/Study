# `exists`代替`in`查询

> 利用虚拟记录集实现

* 例一

```mysql
SELECT
	* 
FROM
	( SELECT 1 AS id UNION ALL SELECT 2 AS id ) q
	INNER JOIN `user` u ON u.id = q.id
```

* 例二

```mysql
SELECT
	u.* 
FROM
	`user` u 
WHERE
	EXISTS ( SELECT 1 FROM ( SELECT 1 AS id UNION ALL SELECT 2 AS id ) AS id WHERE id = u.id )
```
