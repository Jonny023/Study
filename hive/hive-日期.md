# hive日期

```sql
-- 当前时间：2021-12-21 15:01:21.583
select current_timestamp;
-- 时间戳：1640070156
select unix_timestamp();
-- 2021-12-21
select CURRENT_DATE;

-- 字符转日期
select unix_timestamp('2021-12-21 12:20:21','yyyy-MM-dd HH:mm:ss') as time;
-- 2021-12-21
select to_date('2021-12-21 15:07:06');

-- 时间戳格式化
select from_unixtime(unix_timestamp(),'yyyy-MM-dd HH:mm:ss') as time;
select DATE_FORMAT(current_timestamp,'yyyy-MM-dd HH:mm:ss') as time;

-- 年
select date_format(current_timestamp, 'yyyy') as year;
select year(current_timestamp) as year;

-- 月
select date_format(current_timestamp, 'mm') as month;
select month(current_timestamp) as month;

-- 日
select date_format(current_timestamp, 'dd') as day;
select day(current_timestamp) as day;

-- 周
select weekofyear(current_timestamp) as day;

-- 星期
select pmod(datediff(current_timestamp, '1920-01-01')-3, 7) as xq;

-- 季
select ceil(month(current_date)/3) as quarter;
select lpad(ceil(month(current_date)/3), 2, 0) as quarter;

-- 时分秒
select hour(current_timestamp) as h, minute(current_timestamp) as m, second(current_timestamp) as s;

-- 当月第一天
select trunc(current_date, 'MM');
-- 当月最后一天
select last_day(current_date);
-- 当年第一天
select trunc(current_date, 'YYYY');
-- 当年最后一天
select last_day(add_months(trunc(current_date, 'YY'), 11));
```

