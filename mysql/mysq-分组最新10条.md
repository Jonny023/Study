# 查询最新10条数据

* db

```sql

CREATE TABLE `demo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4


INSERT INTO demo (id, name, create_time, update_time) VALUES(1, 'A', '2021-11-22 19:20:10', '2021-11-22 19:25:10');
INSERT INTO demo (id, name, create_time, update_time) VALUES(2, 'B', '2021-11-22 19:20:10', '2021-11-22 19:30:10');
INSERT INTO demo (id, name, create_time, update_time) VALUES(3, 'C', '2021-11-22 19:20:10', '2021-11-22 19:27:10');
INSERT INTO demo (id, name, create_time, update_time) VALUES(4, 'A', '2021-11-22 19:20:10', '2021-11-22 19:20:10');
INSERT INTO demo (id, name, create_time, update_time) VALUES(5, 'B', '2021-11-22 19:20:10', '2021-11-22 19:25:10');
INSERT INTO demo (id, name, create_time, update_time) VALUES(6, 'C', '2021-11-22 19:20:10', '2021-11-22 21:20:10');
INSERT INTO demo (id, name, create_time, update_time) VALUES(7, 'D', '2021-11-22 19:20:10', '2021-11-22 20:20:10');
INSERT INTO demo (id, name, create_time, update_time) VALUES(8, 'A', '2021-11-22 19:20:10', '2021-11-22 19:20:14');

```

* 查询sql

```sql 
-- timestampdiff(minute, p1, p2) 计算分钟数
SELECT t1.name, any_value(timestampdiff(minute,t1.create_time, t1.update_time)) as deal_time FROM demo t1 
left join demo t2 on t1.id > t2.id and t1.name = t2.name group by t1.name, t1.id
having count(1) < 10
order by t1.create_time desc
```





### 查询最新一条记录

> 根据关联条件查询最新的一条记录，适用于最近登录、最近操作日志记录查询

```sql
-- 通过关联条件查询没有比当前记录时间更大的记录，则获取，若主表数据时间重复了，会导致重复数据
SELECT * FROM temp a WHERE NOT EXISTS(
	SELECT 1 from temp b WHERE a.name = b.name AND b.create_time > a.create_time 
);

-- 通过count(1)条件筛选，这种方式也可能出现重复数据，若create_time有两条一样记录的话
SELECT * FROM temp a WHERE (
	SELECT COUNT(1) from temp b WHERE a.name = b.name AND b.create_time > a.create_time 
) < 1;

-- 通过窗口函数筛选最新一条记录，若出现一样的记录则不会重复
-- 前两种方式请自行斟酌，若同一个条件查询记录不会出现两条相同create_time的记录则可以直接用，若果记录时间都相同还是建议用下面这种窗口函数实现
SELECT * FROM (
	SELECT ROW_NUMBER() OVER(PARTITION BY name ORDER BY create_time DESC) rk,t.* FROM temp t
) tmp WHERE tmp.rk = 1
```

