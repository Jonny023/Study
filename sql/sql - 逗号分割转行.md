# mysql列转行

> 字段逗号分割转为一行一行的数据

## 源数据

| code | text  |
| ---- | ----- |
| 001  | a,b,c |
| 002  | d,e   |

## 转换后

> 需要借助mysql系统表mysql.help_topic

```sql
SELECT
	code,
	SUBSTRING_INDEX(SUBSTRING_INDEX(text, ',', b.help_topic_id + 1), ',', -1) txt
FROM
	`table` join mysql.help_topic b on b.help_topic_id < (LENGTH(text) - LENGTH(REPLACE(text, ',', '')) + 1);
```

* 原表与一个包含连续自增长字段的表进行join，得到字符串分隔后的索引值
* `LENGTH(text) - LENGTH(REPLACE(text, ',', '')) + 1`语句获得字符串逗号分隔之后得到的数据长度
* 再借助`SUBSTRING_INDEX(field,分隔符,index)`进行截取

### 效果

| code | text |
| ---- | ---- |
| 001  | a    |
| 001  | b    |
| 001  | c    |
| 002  | d    |
| 002  | e    |



# postgresql

```sql
select name, UNNEST(string_to_array(text, ',')) txt  from test
```

