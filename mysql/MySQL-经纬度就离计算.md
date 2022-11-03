## mysql经纬度距离计算

```sql
-- longitude经度值必须在（-180，180]范围内。正值位于本初子午线以东。
-- latitude纬度值必须在[-90，90]范围内。正值位于赤道以北。
-- 计算当前位置与目标位置的距离（单位米）
select
	id, address, st_distance_sphere(point(longitude,latitude), point(180,45)) as distance
FROM
	tableName
```
