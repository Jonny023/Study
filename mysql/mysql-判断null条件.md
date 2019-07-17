# mysql判断null条件

```sql
update table set time=ifnull(time,"2018-08-17")
update table set time='2018-08-17' where time is null
```
