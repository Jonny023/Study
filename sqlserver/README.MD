> `sqlserver`语法不能用双引号，只能用单引号

## 系统表查询

```sql
-- 查看表
select name from sys.objects where type='U' and name like '%文章' order by name

-- 根据表备注查询，系统表value字段为SQL_VARIANT类型，不能直接like查询，（感觉有点类似java的Object对象）
-- 中文需要转换为字符模糊查询，N'%xxx%'里面的N表示查询的字符为Unicode字符，而不是ASCII字符
select * from sys.extended_properties WHERE CAST(value AS NVARCHAR(MAX))  like N'%员工%';
select * from sys.extended_properties where CONVERT(nvarchar(1000), value) like N'%公司%';
```

## sql server执行计划

```sql
SET SHOWPLAN_ALL ON;
select 1 as col;
SET SHOWPLAN_ALL OFF;
```

## 坑

* sqlserver分页必须排序，否则语法错误



## 常用

### 创建数据库并指定编码

```sql
CREATE DATABASE xxx COLLATE Chinese_PRC_CI_AS;
```



### 日期

```sql
select GETDATE() as '当前日期',
CONVERT(varchar(11),GETDATE(),112)+REPLACE(CONVERT(varchar(12),GETDATE(),108),':','') as yyyyMMddHHmmss,
DateName(year,GetDate()) as '年',
-- 英文月
DateName(month,GetDate()) as '月',
DateName(day,GetDate()) as '日',
DateName(dw,GetDate()) as '星期',
DateName(week,GetDate()) as '周数',
DateName(hour,GetDate()) as '时',
DateName(minute,GetDate()) as '分',
DateName(second,GetDate()) as '秒',
-- 年月日无符号分隔
CONVERT(varchar(11),GETDATE(),112) as ymd,
-- 时分秒
CONVERT(varchar(12),GETDATE(),108) as Hms,
year(getdate()),month(getdate()),day(getdate())
```



### 分页

```sql
-- 默认编码为SQL_Latin1_General_CP1_CI_AS
-- sql查询的时候查询数据变成了??问号

# 数据库的编码不对，修改下数据库的编码
ALTER DATABASE [数据库名] COLLATE Chinese_PRC_CI_AS

ALTER TABLE test.dbo.[user] ALTER COLUMN name varchar(100) COLLATE Chinese_PRC_CI_AS NULL;


-- 方式1
select * from (
　　　　select *, ROW_NUMBER() OVER(Order by id ) AS sort_id from [user]
　　) as b
where b.sort_id between 0 and 20
---where RowId BETWEEN 当前页数-1*条数 and 页数*条数---   


-- 方式2 （sqlserver 2012+推荐）
-- 分页下标从0开始，sqlserver2012及以上版本才支持offset这种分页方式
select id, name from [user] order by id offset 0 rows fetch next 5 rows only 

-- 方式3
-- mssql2012以下版本
select top 3 * from [user]
where id not in (select top 2 id from [user])
--where id not in (select top 条数*页数  id  from [user])  

```

### varchar(5) 与 nvarchar(5)区别

> 编码统一为中文：Chinese_PRC_CI_AS

* varchar(5) 一个汉字两个字节，如果输入英文或数字可以输入5个字符，如果是中文，最多只能存2个【汉字】
* nvarchar(5) 限制长度无论任何字符，都限制为5个

