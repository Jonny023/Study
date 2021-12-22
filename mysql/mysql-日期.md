# mysql日期

## 日期函数

```sql
-- 字符转日期
select str_to_date('2021/12/21', '%Y/%m/%d') as day;

-- 年
select date_format(now(), '%Y') as year;
select year(now()) as year;

-- 月
select date_format(now(), '%m') as month;
select month(now()) as month;

-- 日
select date_format(now(), '%d') as day;
select day(now()) as day;

-- 周
select date_format(now(), '%u') as week;
select week(now()) as day;

-- 季
select quarter(now()) as quarter;

-- 时分秒
select hour(now()) as h, minute(now()) as m, second(now()) as s;

-- yyyy-MM-dd HH:mm:ss
select date_format(now(), '%Y-%m-%d %H:%i:%s') as day;

-- 星期
select dayofweek(curdate())-1 as dw
```

