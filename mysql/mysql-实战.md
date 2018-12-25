### 表及数据sql语句

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
