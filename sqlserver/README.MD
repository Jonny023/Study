> `sqlserver`语法不能用双引号，只能用单引号

```sql
-- 查看表
select name from sys.objects where type='U' and name like '%文章' order by name

-- 根据表备注查询
select * from sys.extended_properties WHERE CAST(value AS NVARCHAR(MAX))  like N'%员工%';
select * from sys.extended_properties where CONVERT(nvarchar(1000), value) like N'%公司%';
```

## 坑

* sqlserver分页必须排序，否则语法错误
