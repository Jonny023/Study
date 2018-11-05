* 通过条件查询指定类型所占百分比

```sql
SELECT
	CONCAT( ROUND( ( ( myCounts / allCounts ) * 100 ), 2 ), '%' ) 
FROM
	( SELECT SUM( COUNT ) myCounts FROM apptable WHERE TYPE = '腾讯开放平台' ) a,
	( SELECT SUM( COUNT ) allCounts FROM apptable ) b
```
