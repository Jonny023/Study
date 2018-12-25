> `sqlserver`语法不能用双引号，只能用单引号

```
select name from sys.objects where type='U' and name like '%文章' order by name
```
