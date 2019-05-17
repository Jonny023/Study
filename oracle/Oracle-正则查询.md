## 正则查询

```sql
SELECT
	max( this_.pay_order_no ) AS y0_ 
FROM
	pay_basic this_ 
WHERE
	REGEXP_LIKE (
	this_.pay_order_no, '^4')
```
