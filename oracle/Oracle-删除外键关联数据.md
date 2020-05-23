* 查询外键所在表

```sql
-- 查询外键所在表
select table_name from dba_constraints where constraint_name='user_id' and  constraint_type = 'R';
```
