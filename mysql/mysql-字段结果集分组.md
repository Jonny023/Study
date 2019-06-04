## 字段结果集组合分组

> 表

```sql
DROP TABLE IF EXISTS `parts`;
CREATE TABLE `parts`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parts_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `parts_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of parts
-- ----------------------------
INSERT INTO `parts` VALUES (1, '齿轮', '发动机');
INSERT INTO `parts` VALUES (2, '皮带', '发动机');
INSERT INTO `parts` VALUES (3, '皮带', '变速箱');
INSERT INTO `parts` VALUES (4, '轮胎', '制动器');
```

> sql语句

```sql
SELECT
	parts_type,
	GROUP_CONCAT( `parts_name` ) 
FROM
	`parts` 
GROUP BY
	parts_type
```

> 运行结果

|parts_type|parts_name|
|--|--|
|制动器|轮胎|
|发动机|齿轮,皮带|
|变速箱|皮带|
