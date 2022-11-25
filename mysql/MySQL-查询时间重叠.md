## 查询时间重叠

```java
SELECT
	count(1)
FROM
	holiday
WHERE
	xxx = 8
	AND xxx = 2
	AND end_time >= '2022-11-13 08:00'
	AND start_time <= '2022-11-13 14:46'
```
