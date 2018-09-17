> concat说明
* 数据拼接
* 可以无限拼接
* 可以配合like用

> 简单用法
```
select concat("123");
```

> 例子
```
select * from user where nickname like concat("%","李","%");
等同于
select * from user where nickname like "%李%";
```
