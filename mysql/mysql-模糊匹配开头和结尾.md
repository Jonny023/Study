## 表

```sql
CREATE TABLE `demo`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `addr` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of baoming
-- ----------------------------
INSERT INTO `demo` VALUES (1, '京包xxx上线');
INSERT INTO `demo` VALUES (2, '京包aaa上线');
INSERT INTO `demo` VALUES (3, '京包ccc下线');
```

## sql语句

```sql
SELECT
	SUM( CASE WHEN LEFT ( `addr`, 2 ) = '京包' AND RIGHT ( `addr`, 2 ) = '下线' THEN 1 ELSE 0 END ) 
FROM
	test
```

```sql
SELECT
	a._begin,
	a._end,
	a.addr,
	COUNT( * ) 
FROM
	(
	SELECT
		a.*,
		LEFT ( a.`addr`, 2 ) _begin,
		RIGHT ( a.`addr`, 2 ) _end 
	FROM
		`demo` a 
	) a 
GROUP BY
	a._begin,
	a._end
```

### 列出满足条件的数据的id

```sql
SELECT
	a._begin,
	a._end,
	a.addr,
	COUNT( * ),
	GROUP_CONCAT( id ) 
FROM
	(
	SELECT
		a.*,
		LEFT ( a.`addr`, 2 ) _begin,
		RIGHT ( a.`addr`, 2 ) _end 
	FROM
		`demo` a 
	) a 
GROUP BY
	a._begin,
	a._end
```

### 统计并列出满足条件的数据id和addr拼接

```sql
SELECT
	a._begin,
	a._end,
	a.addr,
	COUNT( * ),
	GROUP_CONCAT( id, '-', a.`addr` ) 
FROM
	(
	SELECT
		a.*,
		LEFT ( a.`addr`, 2 ) _begin,
		RIGHT ( a.`addr`, 2 ) _end 
	FROM
		`demo` a 
	) a 
GROUP BY
	a._begin,
	a._end
```
