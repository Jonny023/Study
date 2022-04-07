# 窗口函数及删除重复记录

### 无法使用窗口

[参考](https://www.gbase8.cn/5235)

> 解决无法使用窗口函数的方法

```sql
-- 方式1 sql命令窗口执行
set allow_experimental_window_functions = 1;

-- 方式2 连接参数添加--allow_experimental_window_functions = 1
./clickhouse client --allow_experimental_window_functions = 1

-- 方式3 修改配置文件
$ cat /etc/clickhouse-server/users.d/allow_experimental_window_functions.xml
<?xml version="1.0"?>
<yandex>
   <profiles>
       <default>
           <allow_experimental_window_functions>1</allow_experimental_window_functions>
       </default>
   </profiles>
</yandex>

-- 方式4 查询语句后面跟上参数settings allow_experimental_window_functions = 1
SELECT
    x,
    sum(x) OVER (PARTITION BY intDiv(x, 5))
FROM
(
    SELECT arrayJoin(range(10)) AS x
) settings allow_experimental_window_functions = 1;
```



### 表数据重复问题

> 表记录没有唯一标识，数据行记录重复（一模一样）
>
> 通过中间表，将过滤后的数据存入中间表，删除原表满足条件的数据，再将中间表的数据插入到原表中

```sql
-- 查询重复的所有记录
select * from data_basics where (si, event_time) in (select si, event_time from data_basics group by si,event_time having count(si) > 1);

select rank() over (order by a.event_time) as row_num, a.* from data_basics a where (a.si, a.event_time) in (select si, event_time from data_basics group by si,event_time having count(si) > 1);

-- 序号递增
select row_number() over (order by a.event_time) as row_num, a.* from data_basics a where (a.si, a.event_time) global in (select si, event_time from data_basics group by si,event_time having count(si) > 1);

-- 并列排序
select row_number() over (partition by a.si,a.event_time) as row_num, a.* from data_basics a where (a.si, a.event_time) global in (select si, event_time from data_basics group by si,event_time having count(si) > 1);

-- 查询每组序号最大值
select max(temp.row_num) from (select row_number() over (partition by a.si,a.event_time) as row_num, a.* from data_basics a where (a.si, a.event_time) global in (select si, event_time from data_basics group by si,event_time having count(si) > 1)) temp group by temp.si,temp.event_time having count(*) > 1;

-- 查询最终记录
select si,event_time,pv,uv,ip_counts,devices,vv,avg_visit_times from (select row_number() over (partition by a.si,a.event_time) as row_num, a.* from data_basics a where (a.si, a.event_time) global in (select si, event_time from data_basics group by si,event_time having count(si) > 1)) x 
where (si, event_time, row_num) in (select si,event_time,max(temp.row_num) from (select row_number() over (partition by a.si,a.event_time) as row_num, a.* from data_basics a where (a.si, a.event_time) global in (select si, event_time from data_basics group by si,event_time having count(si) > 1)) temp group by temp.si,temp.event_time having count(*) > 1);

-- 将最大值存入到中间表
insert into a_basics(si,event_time,pv,uv,ip_counts,devices,vv,avg_visit_times) select si,event_time,pv,uv,ip_counts,devices,vv,avg_visit_times from (select row_number() over (partition by a.si,a.event_time) as row_num, a.* from data_basics a where (a.si, a.event_time) global in (select si, event_time from data_basics group by si,event_time having count(si) > 1)) x 
where (si, event_time, row_num) in (select si,event_time,max(temp.row_num) from (select row_number() over (partition by a.si,a.event_time) as row_num, a.* from data_basics a where (a.si, a.event_time) global in (select si, event_time from data_basics group by si,event_time having count(si) > 1)) temp group by temp.si,temp.event_time having count(*) > 1);

-- 查询si组成数组
select groupUniqArray(si) from a_basics;
select groupUniqArray(event_time) from a_basics;

-- 删除原表记录
alter table data_basics delete where 
si in ('2e035e6067005ef3a2fe30aa9f42304c','bd1e41afdbdd76318c175a9f5219807b','34130cd716945e9222ee4a50c7027d29','ddab31cd8267c90d3d3d9906b04f2aba','9e20839a5f62e77f4ea1be5bb43c9bdd','d13f293cf15c3a46e1a5cc299dc6c667','13d1f8bcb47a96b20ce0fdd81f55e685','1e06d97d56d9828c63ebc3d2421d6555')
and event_time in ('2022-04-06','2022-04-05');

-- 合并
optimize table data_basics final;

-- 迁移数据
insert into data_basics(si,event_time,pv,uv,ip_counts,devices,vv,avg_visit_times) select si,event_time,pv,uv,ip_counts,devices,vv,avg_visit_times from a_basics;

-- 删除中间表
drop table a_basics;
```

## 问题

* clickhouse集群分片存储，若为删除数据则需要在每个分片分别执行，因为每个分片存储的数据不一样

* 相关文档

  https://clickhouse.com/docs/en/operations/settings/settings/
  https://clickhouse.com/docs/zh/sql-reference/statements/select/
  http://148.70.9.159/blog/post/admin/9095d553efa4
  集群：https://www.kubesre.com/archives/clickhousefen-pian-ji-qun-shi-zhan
  https://www.yixuebiancheng.com/article/92122.html
  https://zhuanlan.zhihu.com/p/381343285

* 多行模式

  * ```sh
    clickhouse-client -m -h 192.168.1.2 -d db_name --port 9000 -u xxx --password 123456
    ```

    * -m 多行默认，换行符不会执行命令，只有输入分号后才会执行