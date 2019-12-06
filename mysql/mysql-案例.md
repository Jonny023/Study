* 查询重复邮箱

```sql
select p.Email from Person p group by p.Email having count(p.Email) > 1 
```
