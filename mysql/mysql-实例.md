#### 1.根据用户id查询同一部门下所有用户

```sql
SELECT
	j1.user_id 
FROM
	jc_user_dept j1
	left JOIN jc_user_dept j2 ON j2.dept_id = j1.dept_id 
	where j2.user_id =3
```
或者

```sql
SELECT
	j1.user_id 
FROM
	jc_user_dept j1,jc_user_dept j2  
	WHERE j2.dept_id = j1.dept_id  AND j2.user_id =3
```
