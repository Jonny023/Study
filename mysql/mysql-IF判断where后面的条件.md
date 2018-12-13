## 说明：WHERE  IF(条件,  true执行条件, false执行条件 )

> 例子
```sql
SELECT
	* 
FROM
	`demo` 
WHERE
IF
	(true, `addr` <> "京包xxx上线", `addr` = "京包xxx上线" )
```
