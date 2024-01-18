# Sqlserver

## 获取上一记录、下一记录

```sql
-- LAG和LEAD函数,分别获取上一条和下一条记录的值进行比较
SELECT 
  id, 
  name,
  LAG(name) OVER(ORDER BY id) AS prev_name,
  LEAD(name) OVER(ORDER BY id) AS next_name,
  LAG(age) OVER(ORDER BY id) AS prev_age,
  LEAD(age) OVER(ORDER BY id) AS next_age
FROM [user]

-- 通过游标获取上一记录、当前记录
DECLARE @id int, @name varchar(50)
DECLARE @prev_id int, @prev_name varchar(50)
DECLARE user_cursor CURSOR SCROLL FOR SELECT id, name FROM [user] ORDER BY id DESC 

OPEN user_cursor
FETCH NEXT FROM user_cursor INTO @id, @name

WHILE @@FETCH_STATUS = 0  
BEGIN

  -- compare current row with prev row
  IF @id <> @prev_id
     PRINT '当前记录id:'+CAST(@id as varchar(10)) + ', 上一记录id:'+CAST(@prev_id as varchar(10))

  SET @prev_id = @id
  SET @prev_name = @name

  FETCH NEXT FROM user_cursor INTO @id, @name
END
CLOSE user_cursor
DEALLOCATE user_cursor


-- 通过游标获取上一记录、当前记录
DECLARE @id int, @name varchar(50)
DECLARE @prev_id int, @prev_name varchar(50)
drop table #temp
SELECT t.* INTO #temp from (SELECT 1 AS id, 'xxx1' as name UNION ALL SELECT 2 AS id, 'xxx2' as name UNION ALL SELECT 3 AS id, 'xxx3' as name) t
DECLARE @count int
DECLARE @index int = 1
SELECT @count = count(1) from #temp
DECLARE user_cursor CURSOR SCROLL FOR SELECT * from #temp

OPEN user_cursor
FETCH NEXT FROM user_cursor INTO @id, @name

WHILE @@FETCH_STATUS = 0
BEGIN

  -- compare current row with prev row
--  IF @id <> @prev_id
--  BEGIN
--     PRINT '当前记录id:'+CAST(@id as varchar(10)) + ', 上一记录id:'+CAST(@prev_id as varchar(10))     
--  END
  
  IF (@index = @count) BEGIN
  	PRINT '当前为最后一条，当前记录id:'+CAST(@id as varchar(10)) + ', 上一记录id:'+CAST(@prev_id as varchar(10))
  END
  ELSE BEGIN
  	PRINT '当前记录id:'+CAST(@id as varchar(10)) + ', 上一记录id:'+CAST(@prev_id as varchar(10))
  END
  print cast(@index as varchar(10)) + ':' + cast(@count as varchar(10))

  SET @prev_id = @id
  SET @prev_name = @name
  SET @index += 1;

  FETCH NEXT FROM user_cursor INTO @id, @name
END
CLOSE user_cursor
DEALLOCATE user_cursor
```

## 执行sql

```sql
-- 简单执行sql
DECLARE @sql varchar(MAX) = 'insert into tab (col) select col from test'
EXEC (@sql)

-- 执行指定sql或表达式，类似eval()函数
DECLARE @expr nvarchar(max) = N'SELECT ROUND((1+100)/10,2)+200-40 AS total'
EXECUTE sp_executesql @expr

-- 执行sql并将结果赋值给变量
DECLARE @total decimal(10,2)
DECLARE @expr nvarchar(max) = N'SELECT @total=ROUND((1+100)/10,2)+200-40'
EXEC sp_executesql @expr, N'@total decimal(10,2) OUTPUT', @total = @total OUTPUT
PRINT @total
```


## 更新时间

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

## java jdbc in查询参数超过2100个的问题

> jdbc + mybatis限制#{param}参数为2100个，超过会报错，解决方法可以不同过#{}预编译，通过${}进行sql拼接


## 触发器

> 可以通过创建触发器来触发默写事件，比如一个表insert时触发要不要同时在领一张表中插入数据

```sql
-- 表结构
drop table student
drop table sc
create table student (
	sno varchar(20) not null
)
create table sc (
	sno varchar(20) not null,
	cno varchar(20) not null,
	degree int not null
)


go
-- 删除触发器
drop trigger student_sc_insert1

-- 创建触发器
create trigger student_sc_insert1
on sc
after insert

as
begin
	if exists(select 1 from sc c left join student s on s.sno = c.sno where s.sno is null)
	begin
	  -- insert into student(sno) select c.sno from sc c left join student s on s.sno = c.sno where s.sno is null
	  -- inserted 是一个临时表，它包含了插入操作中的所有记录，inserted代表if里面查询里面的所有数据
	  insert into student(sno) select sno from inserted
	end
end
go
insert into sc(sno,cno,degree) values('s20070301','c02',78)
```

## 类型转换

```sql
-- 转换decimal
select cast(1 as decimal(10,2))

-- 转换为字符
select cast(1111 as varchar(max))
```

## 去除尾0

```sql
-- ### 表示保留小数位数
select FORMAT(1.0120, '0.###')
```

## 明细数据分组小计

* 这种方式可以解决分组数据错乱问题
```sql
    -- 分组写入小计
    ;WITH cte AS (
	SELECT *, ROW_NUMBER() OVER(ORDER BY id) AS rn
	FROM #tmp_report_hr_emp_comm_customer_detail WHERE task_id = @p_task_id AND tenant_id = @p_tenant_id AND row_data_type = 100
    )
     INSERT INTO #tmp_report_hr_emp_comm_customer_detail (
	tenant_id, task_id, schema_id, employee_id,row_data_type,
	customer_id,bill_cat_id, bill_id, bill_no, bill_time, goods_id,schema_name,
	sale_amount, sale_gross_profit_amount, sale_quantity, sale_settle_amount, comm_amount
     )
    SELECT
	c.tenant_id,
	c.task_id,
	c.schema_id,
	c.employee_id,
	0 AS row_data_type,
	c.customer_id,
	c.bill_cat_id,
	c.bill_id,
	c.bill_no,
	c.bill_time,
	c.goods_id,
	c.schema_name,
	c.sale_amount,
	c.sale_gross_profit_amount,
	c.sale_quantity,
	c.sale_settle_amount,
	c.comm_amount
    FROM cte c
    WHERE rn IS NOT NULL
    UNION ALL
    SELECT
	@p_tenant_id AS tenant_id, @p_task_id AS task_id,
	schema_id,
	employee_id,
	1 AS row_data_type,
	MAX(c.customer_id) AS customer_id,
	MAX(c.bill_cat_id) AS bill_cat_id,
	MAX(c.bill_id) AS bill_id,
	MAX(c.bill_no) AS bill_no,
	MAX(c.bill_time) AS bill_time,
	MAX(c.goods_id) AS goods_id,
	'小计' AS schema_name,
	SUM(c.sale_amount) AS sale_amount,SUM(sale_gross_profit_amount) AS sale_gross_profit_amount, SUM(sale_quantity) AS sale_quantity, SUM(sale_settle_amount) AS sale_settle_amount, SUM(comm_amount) AS comm_amount
    FROM cte AS c
    GROUP BY c.schema_id, c.employee_id
    ORDER BY c.schema_id,c.employee_id, row_data_type;
```

> 这种方式有问题（分组小计错乱）
```sql
;with cte as (
    select *, row_number() over(order by id) as rn
    from report_hr_emp_comm_customer_detail WHERE task_id = 1747422519452418050 AND row_data_type = 0
)
select
    case when grouping(c.rn) = 1 then '小计' else c.schema_name end as schema_name,customer_name,employee_name,
    sum(c.sale_amount) as sale_amount
from cte as c
group by grouping sets ((c.schema_name,customer_name,employee_name), (c.schema_name,customer_name,employee_name, c.rn))
order by c.schema_name,customer_name,employee_name;

-- having grouping(c.schema_name) = 0表示只需要数据明细和小计数据，having grouping(c.schema_name) = 1表示合计
;with cte as (
    select *, row_number() over(order by id) as rn
    from report_hr_emp_comm_customer_detail WHERE task_id = 1747422519452418050 AND row_data_type = 0
)
select
    case when grouping(c.rn) = 1 then '小计' else c.schema_name end as schema_name,
    customer_name,employee_name ,
    sum(c.sale_amount) as sale_amount
from cte as c
group by rollup ((c.schema_name, c.customer_name, employee_name ), c.rn)
having grouping(c.schema_name) = 0
order by c.schema_name,c.customer_name,employee_name;
```
