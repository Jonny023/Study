# mysql-序号

## example1

> 不限版本

```sql
SELECT
	@rowNum := @rowNum + 1 as line,
	s.id,
	s.stu_name,
	s.score
FROM
	student s,
	(
	select
		@rowNum := 0) n order by line asc;
```

## example2

> mysql 8.0+

```sql
-- 行号【必须排序】
SELECT ROW_NUMBER() over (order by id asc) as rn, s.* FROM student s;

-- 结果
rn	id	stu_name	grade	score
1	15	小王	A	79
2	17	李四	B	60
3	19	张三	A	90
4	20	小美	A	88


-- 分组
-- 学生详情及所在班级平均成绩
SELECT avg(score) over (PARTITION by grade) as avg_score, s.* FROM student s;

-- 结果
avg_score	id	stu_name	grade	score
85.666667	1	张三	A	90.00
85.666667	1	小美	A	88.00
85.666667	1	小王	A	79.00
60.000000	1	李四	B	60.00

-- 阶梯累计
SELECT sum(score) over (order by id asc) as total_score, s.* FROM student s;

-- 结果
total_score	id	stu_name	grade	score
79	15	小王	A	79
139	17	李四	B	60
229	19	张三	A	90
317	20	小美	A	88
```

