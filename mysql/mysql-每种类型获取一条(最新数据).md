* 每种类型获取一条最新数据

```sql

select * from book where id in
(select min(id) from book group by title);
```
