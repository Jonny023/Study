# 月、周、日统计

```sql
-- 按小时显示
select timestamp,count(*) as test from browserinfo group by date_format(timestamp, '%Y-%m-%d %H ');


-- 查询今天,昨天,近7天,近30天,本月,上一月
-- 当天
select * from `browserinfo` where date_format(`timestamp`,'%Y-%m-%d') = date_format(now(),'%Y-%m-%d'); 

-- 当天,效果同上一条
select * from `browserinfo` where to_days(date_format(`timestamp`,'%Y-%m-%d')) = to_days(now()); 

-- 当天,效果同上两条
select * from `browserinfo` where to_days(`timestamp`) = to_days(now()); 

-- 昨天
select * from `browserinfo` where to_days(now())-to_days(`timestamp`) <= 1; 

-- 最近七天
select * from `browserinfo` where date_sub(curdate(), INTERVAL 7 DAY) <= date(`timestamp`); 

-- 最近三十天
select * from `browserinfo` where date_sub(curdate(), INTERVAL 30 DAY) <= date(`timestamp`); 

-- 本月
select * from `browserinfo` where date_format(`timestamp`, '%Y%m') = date_format(curdate() , '%Y%m'); 

-- 上个月
select * from `browserinfo` where period_diff(date_format(now() , '%Y%m') , date_format(`timestamp`, '%Y%m')) =1;
```

## [mysql 按时间段统计（年，季度，月，天，时）](http://www.cnblogs.com/daniel-yi/p/3186251.html)

### 1.按年汇总：

```sql
select sum(mymoney) as totalmoney, count(*) as sheets from mytable group by date_format(col, '%Y');
```

### 2.按月汇总：

```sql
select sum(mymoney) as totalmoney, count(*) as sheets from mytable group by date_format(col, '%Y-%m');
```

### 3.按季度汇总：

```sql
select sum(mymoney) as totalmoney,count(*) as sheets from mytable group by concat(date_format(col, '%Y'),FLOOR((date_format(col, '%m')+2)/3));

select sum(mymoney) as totalmoney,count(*) as sheets from mytable group by concat(date_format(col, '%Y'),FLOOR((date_format(col, '%m')+2)/3));
```

### 4.按小时：

```sql
select sum(mymoney) as totalmoney,count(*) as sheets from mytable group by date_format(col, '%Y-%m-%d %H ');
```

### 5.本年度:

```sql
SELECT * FROM mytable WHERE year(FROM_UNIXTIME(my_time)) = year(curdate())
```

### 6.查询数据附带季度数:

```sql
SELECT id, quarter(FROM_UNIXTIME(my_time)) FROM mytable;
```

### 7.本季度:

```sql
SELECT * FROM mytable WHERE quarter(FROM_UNIXTIME(my_time)) = quarter(curdate());
```

### 8.本月统计:

```sql
select * from mytable where month(my_time1) = month(curdate()) and year(my_time2) = year(curdate())
```

### 9.本周统计:

```sql
select * from mytable where month(my_time1) = month(curdate()) and week(my_time2) = week(curdate())
```

### 10.N天内记录:

```sql
WHERE TO_DAYS(NOW())-TO_DAYS(时间字段)<=N
```

