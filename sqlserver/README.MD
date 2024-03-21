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

## 查看表是否被锁

```sql
-- 会话ID (session_id)、请求状态 (status)、等待类型 (wait_type)、等待时间 (wait_time) 和阻塞会话ID (blocking_session_id)
SELECT 
    OBJECT_NAME(p.object_id) AS TableName,
    r.session_id,
    r.status,
    r.wait_type,
    r.wait_time,
    r.blocking_session_id
FROM 
    sys.dm_exec_requests r
JOIN 
    sys.dm_tran_locks l ON r.session_id = l.request_session_id
JOIN 
    sys.partitions p ON l.resource_associated_entity_id = p.hobt_id
JOIN 
    sys.objects o ON p.object_id = o.object_id
WHERE 
    l.resource_database_id = DB_ID() -- Filter by current database
    AND l.resource_type = 'OBJECT' -- Looking for object-level locks
    AND r.wait_type IS NOT NULL -- The request is waiting for something



-- 查看表锁
select 0 as spid, blocked from (select * from sysprocesses where blocked > 0) a 
	where not exists(
		select * from (select * from sysprocesses where blocked > 0) b where a.blocked = spid
	)
union select spid, blocked from sysprocesses where blocked > 0

kill blocked
 

-- 释放被锁的表
KILL 0
```

## 坑

* sqlserver分页必须排序，否则语法错误



## 常用

### 创建数据库并指定编码

```sql
CREATE DATABASE xxx COLLATE Chinese_PRC_CI_AS;
```

### 编码（修改）

```sql
-- 默认编码为SQL_Latin1_General_CP1_CI_AS
-- sql查询的时候查询数据变成了??问号

-- 数据库的编码不对，修改下数据库的编码
ALTER DATABASE [数据库名] COLLATE Chinese_PRC_CI_AS

-- 不区分大小写
ALTER TABLE test.dbo.[user] ALTER COLUMN name varchar(100) COLLATE Chinese_PRC_CI_AS NULL;

-- 大小写敏感
ALTER TABLE test.dbo.[user] ALTER COLUMN code varchar(20) COLLATE Chinese_PRC_CS_AS;
```

### 查询时区分大小写

> sqlserver默认不区分字母大小写，若要区分大小写可以指定：`COLLATE Chinese_PRC_CS_AI_WS`

```sql
-- 默认不区分大小写
-- 区分大小写查询， COLLATE Chinese_PRC_CS_AI_WS可以放到查询条件左边或右边
SELECT * FROM goods WHERE code COLLATE Chinese_PRC_CS_AI_WS like 'a%';
SELECT * FROM goods WHERE code COLLATE Chinese_PRC_CS_AI_WS = 'B9040';

SELECT * FROM goods WHERE code LIKE 'B%' COLLATE Chinese_PRC_CS_AI_WS;
SELECT * FROM goods WHERE code = 'B9040' COLLATE Chinese_PRC_CS_AI_WS;
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


### 批量查询

```sql
SELECT 
	*
FROM stock ss 
WHERE EXISTS (
	SELECT 1
	FROM (
--	<foreach item="item" index="index" collection="values">
--	    VALUES (#{item.col1}, #{item.col2})
--	    <if test="index != values.size()-1">,</if>
--	</foreach>
		VALUES (6392298028455522304,6392969612160188416), (6392540937872613376, 6392969612160188416)
	) AS T(goods_id, warehouse_id)
	WHERE ss.goods_id = T.goods_id AND ss.warehouse_id = T.warehouse_id
)
```

### 多行合并到一起，类似mysql的concat功能，将多行的id拼接到一起用逗号分隔，如：1,2,3,4

```sql
SELECT STUFF((SELECT ',' + CAST(id AS VARCHAR(20)) FROM tab FOR XML PATH('')), 1, 1, '') AS id;
```
