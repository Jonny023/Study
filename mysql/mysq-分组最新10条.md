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