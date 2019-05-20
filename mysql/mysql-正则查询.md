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

* 字符“^”匹配特定字符

```sql
SELECT * FROM fruits WHERE f_name REGEXP '^b';
```

* 字符'$‘特定字符结尾

```sql
SELECT * FROM fruits WHERE f_name REGEXP 'y$';
```


* 字符“.”代替字符串中的任意一个字符

```sql
SELECT * FROM fruits WHERE f_name REGEXP 'a.g';
```

* 星号“*”匹配前面的字符任意多次，包括0次。加号“+”匹配前面的字符至少一次

```sql
SELECT * FROM fruits WHERE f_name REGEXP '^ba*';
SELECT * FROM fruits WHERE f_name REGEXP '^ba+';
```

* 匹配指定字符串

```sql
SELECT * FROM fruits WHERE f_name REGEXP 'on';
SELECT * FROM fruits WHERE f_name REGEXP 'on|ap';
```

* 匹配指定字符串中的任意一个

```sql
SELECT * FROM fruits WHERE f_name REGEXP '[ot]';
SELECT * FROM fruits WHERE s_id REGEXP '[456]';
```

* “[^字符集合]”匹配不在指定集合中的任何字符

```sql
SELECT * FROM fruits WHERE f_id REGEXP '[^a-e1-2]';
SELECT * FROM fruits WHERE f_name REGEXP 'x{2,}';
SELECT * FROM fruits WHERE f_name REGEXP 'ba{1,3}';
```

* 在fruits表中，查询f_name字段以字母‘b'开头的记录

```sql
SELECT * FROM fruits WHERE f_name REGEXP '^b';
```

* 在fruits表中，查询f_name字段以“be”开头的记录

```sql
SELECT * FROM fruits WHERE f_name REGEXP '^be';
```

* 在fruits表中，查询f_name字段以字母‘t'结尾的记录

```sql
SELECT * FROM fruits WHERE f_name REGEXP 'y$';
```

* 在fruits表中，查询f_name字段以字符串“rry”结尾的记录

```sql
SELECT * FROM fruits WHERE f_name REGEXP 'rry$';
```

* 在fruits表中，查询f_name字段值包含字母‘a'与‘g'且两个字母之间只有一个字母的记录

```sql
SELECT * FROM fruits WHERE f_name REGEXP 'a.g';
```

* 在fruits表中，查询f_name字段值以字母‘b'开头，且‘b'后面出现字母‘a'的记录

```sql
SELECT * FROM fruits WHERE f_name REGEXP '^ba*';
```

* 在fruits表中，查询f_name字段值以字母‘b'开头，且‘b'后面出现字母‘a'至少一次的记录

```sql
SELECT * FROM fruits WHERE f_name REGEXP '^ba+';
```

* 在fruits表中，查询f_name字段值包含字符串“on”的记录

```sql
SELECT * FROM fruits WHERE f_name REGEXP 'on';
```

* 在fruits表中，查询f_name字段值包含字符串“on”或者“ap”的记录

```sql
SELECT * FROM fruits WHERE f_name REGEXP 'on|ap';
```

* 在fruits表中，使用LIKE运算符查询f_name字段值为“on”的记录

```sql
SELECT * FROM fruits WHERE f_name LIKE 'on';
```

* 在fruits表中，查找f_name字段中包含字母o或者t的记录

```sql
SELECT * FROM fruits WHERE f_name REGEXP '[ot]';
```

* 在fruits表，查询s_id字段中数值中包含4、5或者6的记录

```sql
SELECT * FROM fruits WHERE s_id REGEXP '[456]';
```

* 在fruits表中，查询f_id字段包含字母a到e和数字1到2以外的字符的记录

```sql
SELECT * FROM fruits WHERE f_id REGEXP '[^a-e1-2]';
```

* 在fruits表中，查询f_name字段值出现字符串‘x'至少2次的记录

```sql
SELECT * FROM fruits WHERE f_name REGEXP 'x{2,}';
```

* 在fruits表中，查询f_name字段值出现字符串“ba”最少1次，最多3次的记录

```sql
SELECT * FROM fruits WHERE f_name REGEXP 'ba{1,3}';
```
