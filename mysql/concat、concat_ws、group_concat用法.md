## concat函数
* 数据拼接，拼接字符
* 可以有N多个参数
* 可以配合like使用

语法
```sql
concat(str1, str2,...)
```

> 简单用法
```sql
select concat("123");
```

> 例子
```sql
select * from user where nickname like concat("%","李","%");
等同于
select * from user where nickname like "%李%";
```

## concat_ws()函数
* 分隔多个字符
* 第一个参数指定分隔符。需要注意的是分隔符不能为null，如果为null，则返回结果为null。
* 可以有N多个参数

> 语法
```sql
concat_ws(separator, str1, str2, ...)
```

> 例子
```sql
select concat_ws("|",1,2,3,4);

结果:
1|2|3|4

-- 查询，类似or
SELECT
	* 
FROM
	menu
WHERE
	menu_name REGEXP concat_ws( "|", "菜单管理", "系统工具", "用户管理" );
```

## group_concat函数
功能：将group by产生的同一个分组中的值连接起来，返回一个字符串结果。
语法：group_concat( [distinct] 要连接的字段 [order by 排序字段 asc/desc  ] [separator '分隔符'] )

> 例子
```sql
找出相同角色标识的id
SELECT
	config_attribute,
	GROUP_CONCAT( id ) 
FROM
	`requestmap` 
GROUP BY
	config_attribute
```

> 结果：

| config_attribute | GROUP_CONCAT(id) |
| :----------------: | :---------------- |
| permitAll | 2,3,4,5,6,7,8,9,10,11,1 |
| ROLE_ADMIN | 19,20,25,26,27,28,29,3 |
