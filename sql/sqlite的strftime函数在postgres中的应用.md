## sqlite的strftime函数在postgres中的应用



### sqlite

```sql
SELECT strftime('%Y-%m-%d %H:%M:%S', '2022-10-22 18:22:31') as time
union all
SELECT strftime('%Y/%m/%d %H:%M:%S', '2022-10-22 18:22:31') as time
union all
SELECT strftime('%Y-%m-%d %H:%M:%S', 'a.b.c.d') as time;
```

> 输出结果

| time             |
| ---------------- |
| 2022/10/22 18:22 |
| 2022/10/22 18:22 |
| NULL             |



### postgres

```sql
SELECT
	CASE 
	  WHEN t LIKE '____-__-__ __:__:__' THEN TO_CHAR(t::timestamp, 'yyyy-mm-dd hh24:mi:ss') 
	  ELSE null
	END AS t
from public.aaa order by id;
```

> 输出结果

| t                |
| ---------------- |
| NULL             |
| 2022/12/31 10:59 |
| NULL             |
| NULL             |

