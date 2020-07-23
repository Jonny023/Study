# group by 不能用的情况

```sql
SELECT
	any_value ( o.id ) as id,
	any_value ( o.company_id ) as company_id,
	o.product_id,
	any_value ( o.`status` ) as status,
	any_value ( o.group_id ) as group_id,
	any_value ( o.sort ) as sort,
	any_value ( o.staff_status ) as staff_status,
	any_value ( s.`name` ) as name,
	any_value ( s.icon ) as icon,
	any_value ( f.FORMID ) AS form_id 
FROM
	oa_form f
	LEFT JOIN oa_company_product o ON f.product_id = o.product_id
	LEFT JOIN sys_product s ON s.product_id = o.product_id 
WHERE
	s.`status` = 1 
	AND o.STATUS = 1 
	AND o.company_id = '3f88a2e28c7f4119897eb9976fac55ac' 
	AND o.group_id = 'a1d1fc15091b4244be7017fcc2ae6eb9' 
	AND f.logic_delete = 0 
GROUP BY
	o.PRODUCT_ID 
ORDER BY
	any_value(o.sort)
```
