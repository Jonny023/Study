# 清空表记录后如何让id从1开始自增

```sql
-- 方式一
truncate table table_name

-- 方式二
alter table table_name auto_increment=1;
```
