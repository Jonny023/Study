# left join、right join、inner join

> `left join`和`right join`建议用`where`来筛选

```mysql
select b.* from B b left join A a on a.id = b.xxId where a.xxx = xxx

select b.* from B b right join A a on a.id = b.xxId where a.xxx = xxx
```

> `inner join`内连接查询两边同时有的，可直接用`and`

```mysql
select b.* from B b inner join A a on a.id = b.xxId and a.xxx = xxx
```
