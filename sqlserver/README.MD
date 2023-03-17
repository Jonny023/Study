> `sqlserver`语法不能用双引号，只能用单引号

```sql
-- 查看表
select name from sys.objects where type='U' and name like '%文章' order by name

-- 根据表备注查询，系统表value字段为SQL_VARIANT类型，不能直接like查询，（感觉有点类似java的Object对象）
-- 中文需要转换为字符模糊查询，N'%xxx%'里面的N表示查询的字符为Unicode字符，而不是ASCII字符
select * from sys.extended_properties WHERE CAST(value AS NVARCHAR(MAX))  like N'%员工%';
select * from sys.extended_properties where CONVERT(nvarchar(1000), value) like N'%公司%';
```

## 坑

* sqlserver分页必须排序，否则语法错误
