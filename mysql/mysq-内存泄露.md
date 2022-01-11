```sql
show variables like '%max_connections%';

-- ip出现次数最多或存在内存泄露
SELECT count(*),user,db,substring(host, 1, instr(host, ":")-1) as ip,ANY_VALUE(command) FROM information_schema.processlist
group by user, db,substring(host, 1, instr(host, ":")-1) order by substring(host, 1, instr(host, ":")-1);
```


