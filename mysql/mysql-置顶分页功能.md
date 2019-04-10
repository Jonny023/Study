## 置顶新闻只在第一页显示

> 增加一个Int类型的置顶标记位istop，默认istop=0。置顶新闻之间的顺序通过istop值表示，istop值越高新闻越靠前。

```mysql
SELECT
	* 
FROM
	[表名] 
ORDER BY
	istop DESC,
	createtime DESC 
	LIMIT 0,3
```

## 置顶新闻在每一页显示

```mysql
SELECT
	* 
FROM
	[表] 
WHERE
	istop <> 0 
ORDER BY
	istop DESC 
	LIMIT 3 --需要每页置顶的新闻数量 UNION ALL
SELECT
	* 
FROM
	[表] 
WHERE
	istop = 0 
ORDER BY
	createtime DESC 
	LIMIT 0,
	7 --正常分页
```
