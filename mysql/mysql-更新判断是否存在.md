* 有时候修改数据需要查询是否已经存在某记录

```sql
-- 方式1
SELECT
	count( 1 ) 
FROM
	oa_form f 
WHERE
	NOT EXISTS ( SELECT * FROM oa_form of WHERE of.FORMNAME = '加班1' AND of.FORMID = 'c4e4100aea8f4d0b92efce3f50725b3f1' and of.COMPANY_ID = '819cf32c0e5e441cb5964e616c9e8c47' ) 
	LIMIT 1
  
--方式二
SELECT
	count(1)
FROM
	oa_form of 
WHERE
	of.FORMNAME = '加班' 
	AND of.FORMID != 'c4e4100aea8f4d0b92efce3f50725b3f' 
	AND of.COMPANY_ID = '819cf32c0e5e441cb5964e616c9e8c47'
 
```
