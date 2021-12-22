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
select datename(week,create_time) from student
-- 按季度
select datename(quarter,create_time) from student


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


-- 星期
SELECT
CASE (DATEPART(dw, GETDATE()) + @@DATEFIRST) % 7
WHEN 1 THEN 'Sunday'
WHEN 2 THEN 'Monday'
WHEN 3 THEN 'Tuesday'
WHEN 4 THEN 'Wednesday'
WHEN 5 THEN 'Thursday'
WHEN 6 THEN 'Friday'
WHEN 0 THEN 'Saturday'
END AS DayOfWeekName;

SET LANGUAGE N'简体中文'
SELECT DATENAME(weekday, GETDATE()) as DayOfWeekName;
```

* style数字在转换时间时的含义如下

  ```sql
  select CONVERT(varchar(100), GETDATE(), 100) 
  ```


| Style(2位表示年份) | Style(4位表示年份) | 输入输出格式                       | 输出示例                   |
| ------------------ | ------------------ | ---------------------------------- | -------------------------- |
| -                  | 0 or 100           | mon dd yyyy hh:miAM(或PM)          | Dec 21 2021  2:17PM        |
| 1                  | 101                | mm/dd/yy                           | 12/21/2021                 |
| 2                  | 102                | yy.mm.dd                           | 2021.12.21                 |
| 3                  | 103                | dd/mm/yy                           | 21/12/2021                 |
| 4                  | 104                | dd.mm.yy                           | 21.12.2021                 |
| 5                  | 105                | dd-mm-yy                           | 21-12-2021                 |
| 6                  | 106                | dd mon yy                          | 21 Dec 2021                |
| 7                  | 107                | mon dd,yy                          | Dec 21, 2021               |
| 8                  | 108                | hh:mm:ss                           | 14:20:38                   |
| -                  | 9 or 109           | mon dd yyyy hh:mi:ss:mmmmAM(或PM)  | Dec 21 2021  2:20:50:450PM |
| 10                 | 110                | mm-dd-yy                           | 12-21-2021                 |
| 11                 | 111                | yy/mm/dd                           | 2021/12/21                 |
| 12                 | 112                | yymmdd                             | 20211221                   |
| -                  | 13 or 113          | dd mon yyyy hh:mi:ss:mmm(24小时制) | 21 Dec 2021 14:21:39:653   |
| 14                 | 114                | hh:mi:ss:mmm(24小时制)             | 14:21:50:277               |
| -                  | 20 or 120          | yyyy-mm-dd hh:mi:ss(24小时制)      | 2021-12-21 14:22:00        |
| -                  | 21 or 121          | yyyy-mm-dd hh:mi:ss:mmm(24小时制)  | 2021-12-21 14:22:13.503    |
|                    | 23                 | yyyy-mm-dd                         | 2021-12-21                 |
|                    | 8 or 24            | hh:mi:ss                           | 14:27:17                   |
|                    | 126                | yyyy-mm-ddThh:mi:ss.mmm            | 2021-12-21T14:23:51.383    |

```sql
-- 处理前：2021-12-21 14:34:24  处理后：2021/12/21 14:33:26
select REPLACE(CONVERT(varchar(20), GETDATE(), 120), '-', '/') 

-- yyyy-mm-dd hh:mm
select SUBSTRING(CONVERT(varchar(20), GETDATE(), 120),0, len(CONVERT(varchar(20), GETDATE(), 120))-2) 
```
