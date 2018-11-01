### 问题描述
+ `Oracle`数据库使用`case when then end`别名后，`group by`不能使用这个别名，但是`order by`可以

### 使用函数
+ `TO_CHAR( SYSDATE, 'yyyy' )`作用是获取系统时间年份
+ `EXTRACT( year FROM DATE_CREATED )`作用是获取表字段年份

### 示例代码
```
SELECT
	COUNT( * ) AS rs,
CASE

		WHEN TO_CHAR( SYSDATE, 'yyyy' ) - TO_CHAR( NAT_DATE, 'yyyy' ) < 18 THEN
		'17岁及其以下' 
	END n
FROM
	PERSON_INFO 
WHERE
	EXTRACT( year FROM DATE_CREATED ) = '2018' 
	AND person_status = 0
GROUP BY
	CASE

		WHEN TO_CHAR( SYSDATE, 'yyyy' ) - TO_CHAR( NAT_DATE, 'yyyy' ) < 18 THEN
		'17岁及其以下' 
	END
ORDER BY
	n
```

> 目前解决方案是分组时直接使用`case when then end`完整代码，若有大佬有其他解决方案，请联系我，Thanks!
