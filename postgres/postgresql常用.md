# postgresql常用

* 主键自增    

```sql
-- 创建序列
CREATE SEQUENCE id START 1;

-- 重置序列
alter sequence id restart with 1;

-- 验证
SELECT nextval('id');

-- navicat指定主键默认值(需指定库名)
nextval('test.id'::regclass)

-- 建表语句:CONSTRAINT id PRIMARY KEY (id)指定主键序列
CREATE TABLE test.table (
  id int8 NOT NULL,
  update_time timestamp NOT NULL DEFAULT now(),
  CONSTRAINT id PRIMARY KEY (id)
);

CREATE SEQUENCE id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1;

-- 设置主键自增
alter table db.table alter column id set default nextval('id_seq');

```

* 每种类型获取最新一条数据

```sql
SELECT * FROM test.table where id in (
	select max(id) from test.table group by field
)
```

* 类似`mysql`的`group_concat`函数，将查询结果按照指定格式展示
  * 函数`string_agg(field, string)`
    * field - 字段
    * string - 定义分割字符
  * 函数`array_to_string(array_agg(field),string)`

```sql
SELECT string_agg(field,',') FROM test.table where id in (
	select max(id) from test.table group by field
)

-- 或者
SELECT array_to_string(array_agg(field),',') FROM test.table where id in (
	select max(id) from test.table group by field
)
```

* timestamp日期类型查询

```sql
-- 模糊匹配
SELECT * FROM test.table where to_char(start_time, 'yyyy-MM-dd') like '2021-01-24%'

-- 查询当天
SELECT * FROM test.table where start_time >= current_date
```

* int和日期类型计算比较

```sql
-- 当天6点
select current_date+interval '06:00'

-- 次日6点
select current_date+interval '1 day 06:00'

-- 一天前6点
select current_date+interval '-1 day 06:00'

-- 计算日期和int类型字段比较
where (day_num || 'day')::interval < ((current_date - create_datetime))::interval

-- timestamp
select now()::timestamp;

-- 日期减法比较
create_time < date_part('day',(now() - modify_datetime))

-- 获取当前月每一天字符
select
  to_char(m.day, 'yyyy-mm-dd') as day
from
  (select generate_series(cast(to_char(current_date, 'yyyy-mm') || '-01' as date), cast(cast(to_char(current_date, 'yyyy-mm') || '-01' as timestamp) + '1 MONTH' + '-1 d' as date), '1 d') as day) as m
order by day;

-- 获取当天到上月号数-1
select
  to_char ( b, 'YYYY-MM-DD' ) as time
from
  generate_series (current_date - interval '30 day', current_date, '1 days' ) as b
group by
  time order by time
  
-- 获取当月天数
select
  (day - interval '0 day') :: DATE as day
from
  generate_series(date_trunc('month', now()), date_trunc('month', now()) + interval '1 month - 1 day', interval '1 day') as day
  
-- 指定日期到当天的的yyyy-MM-dd列表
select daytime::date from generate_series(
      ('2020-06-01'),--查询开始日期（可根据需求调整） 
      (select now()::date),--查询结束日期（可根据需求调整） 
      '1 day'--间隔（可根据需求调整）
) s(daytime)


```

* `coalesce`函数，将`null`设置一个默认值，跟`mysql`的`ifnull`s功能一样

```sql
select coalesce(sum(field), 0) from table
```

* 问题
> User query might have needed to see row versions that must be removed
```bash
查询慢，等待时间过长，postgresql中断了，修改postgresql配置
```