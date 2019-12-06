* sql实现排序或者排名

> 如果字段可能重复，比如分数（score）就需要`count(distinct score)`

```sql
select (select count(id) from t_system_user where id <= u.id) as rank, u.* from t_system_user u order by id asc limit 10
```
