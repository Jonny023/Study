# Sqlserver

* 更新`datetime`或`datetime2`类型为当前系统时间：`SYSUTCDATETIME()`

```sql
update student set create_time=SYSUTCDATETIME() where id = 8
```

## 时间相关

```sql
-- 按年
select year(create_time) from student
-- 按月
select month(create_time) from student
-- 按日
select day(create_time) from student
-- 按周
select datename(week,create_time)w from student
-- 按季度
select datename(quarter,create_time)w from student


-- 方法二 https://www.cnblogs.com/hao-1234-1234/p/7652014.html
-- 按年 yyyy yy
select DATEPART(yyyy, SYSUTCDATETIME())
-- 按月 mm m
select DATEPART(mm, SYSUTCDATETIME())
-- 按日 dd d
select DATEPART(dd, SYSUTCDATETIME())
-- 季度 qq q
select DATEPART(qq, SYSUTCDATETIME())
-- 按周 ww wk
select DATEPART(wk, SYSUTCDATETIME())
-- 按季度
select DATEPART(dy, SYSUTCDATETIME())
-- 一年的天数 dy y
select DATEPART(y, SYSUTCDATETIME())

-- 查看当前时间
select SYSUTCDATETIME() 

-- 小时
select DATEPART(hh, SYSUTCDATETIME())
```

