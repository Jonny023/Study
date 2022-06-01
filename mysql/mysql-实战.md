### 例一

```sql
DROP TABLE IF EXISTS `score`;
CREATE TABLE `score`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `subject` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `score` int(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
-- ----------------------------
-- Records of score
-- ----------------------------
INSERT INTO `score` VALUES (1, '张三', '语文', 80);
INSERT INTO `score` VALUES (2, '张三', '数学', 90);
INSERT INTO `score` VALUES (3, '李四', '语文', 55);
INSERT INTO `score` VALUES (4, '李四', '数学', 58);
INSERT INTO `score` VALUES (5, '小明', '语文', 40);
INSERT INTO `score` VALUES (6, '小明', '英语', 20);
INSERT INTO `score` VALUES (7, '张三', '英语', 30);
INSERT INTO `score` VALUES (8, '李四', '英语', 80);
```

> 查出至少两门科目不及格（小于60）的学生姓名

```sql
SELECT DISTINCT
	a.`name` 
FROM
	`score` a 
WHERE
	a.`score` < 60 GROUP BY a.`name` HAVING COUNT( a.id ) >1
```

> 查询所有科目都不及格的学生姓名

```sql
SELECT DISTINCT
	a.`name` 
FROM
	`score` a
	LEFT JOIN score b ON a.`name` = b.`name` 
	AND b.`score` > 60 
WHERE
	b.`id` IS NULL
```

### 例二

```sql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for stu_score
-- ----------------------------
DROP TABLE IF EXISTS `stu_score`;
CREATE TABLE `stu_score`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `class` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `score` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stu_score
-- ----------------------------
INSERT INTO `stu_score` VALUES (1, '张三', '1', '80');
INSERT INTO `stu_score` VALUES (2, '李四', '1', '50');
INSERT INTO `stu_score` VALUES (3, '王五', '1', '77');
INSERT INTO `stu_score` VALUES (4, '周武', '1', '72');
INSERT INTO `stu_score` VALUES (5, '郑王', '2', '72');
INSERT INTO `stu_score` VALUES (6, '赵六', '2', '78');

SET FOREIGN_KEY_CHECKS = 1;

```

> 求70分以上并且人数大于2的班级

```sql
SELECT class FROM `stu_score` where score>70 having count(1)>2
```

### 例三

> 数据库

```sql
DROP TABLE IF EXISTS `mt_rvok`;
CREATE TABLE `mt_rvok`  (
  `userid` int(11) NOT NULL,
  `errorcode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sendtime` datetime(0) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

INSERT INTO `mt_rvok` VALUES (111, 'FAIL', '2019-02-26 12:53:56');
INSERT INTO `mt_rvok` VALUES (111, 'FAIL', '2019-02-26 12:54:10');
INSERT INTO `mt_rvok` VALUES (222, 'SUCC', '2019-02-26 12:54:51');
INSERT INTO `mt_rvok` VALUES (444, 'FAIL', '2019-02-26 12:55:11');
```

> 题目：按账号、发送日期（`yyyyMMdd`）分组统计`errorcode`值分别为`SUCC`及`FAIL`值的数量，最终结果如下

|账号|日期|SUCC数量|FAIL数量|
|--|--|--|--|
|||||

>  方式一

```sql
SELECT userid as 账号,date_format(sendtime, '%Y%m%d') as 日期,count(case errorcode when 'FAIL' then 1 end) as FAIL数量,count(case errorcode when 'SUCC' then 1 end) as SUCC数量 FROM `mt_rvok` group by userid
```

* 为啥不用`sum`？
	* 这里的`case when then end`没做处理，当`sun(null)`返回的是`null`，所以改成了`count`

> 方式二

```sql
select userid, date_format(sendtime, '%Y%m%d'), SUM(IF(errorcode='SUCC', 1, 0)) AS SUCC数量, SUM(IF(errorcode='FAIL', 1, 0)) AS FAIL数量 from mt_rvok group by userid, date_format(sendtime, '%Y%m%d')
```

### 日期分组查询

[日期每7天一组](https://blog.csdn.net/manitod/article/details/122087011)
