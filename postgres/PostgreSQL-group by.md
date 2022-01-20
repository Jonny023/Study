# PostgreSQL group by 

[参考1](https://www.jianshu.com/p/239b8a3e9e72)

[参考2](https://stackoverflow.com/questions/3800551/select-first-row-in-each-group-by-group)

> group by如果字段未聚合会报错，可以通过这种方法解决
>
> null值排序：order by field asc/desc nulls first/last

```sql
-- 开窗函数（窗口函数/分析函数）
with temp as (
	select row_number() over (partition by name order by "_ID" desc) as rn, * from public."test"
)
select * from temp where rn = 1 order by "_ID"


-- 子查询
SELECT temp.*
FROM   (
   select row_number() over (partition by name order by "_ID" desc) as rn, * from public."test"
) temp
WHERE  rn = 1 order by temp."_ID";


-- 联表查询
SELECT a."_ID",
         a.age, 
         a.name
    FROM public."test" a
    JOIN (SELECT b.name,
          MAX("_ID") AS id
          FROM public."test" b
          GROUP BY b.name) y ON y.id = a."_ID" order by "_ID"
          

-- distinct 如果要进行者排序必须包一层
select * from (
	SELECT DISTINCT ON ("name") *
	FROM public."test"
) temp ORDER BY "_ID"
```

