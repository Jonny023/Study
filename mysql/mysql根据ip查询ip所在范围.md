> 数据库sql

```sql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ip_data
-- ----------------------------
DROP TABLE IF EXISTS `ip_data`;
CREATE TABLE `ip_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `begin_ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `end_ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `begin_ip`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ip_data
-- ----------------------------
INSERT INTO `ip_data` VALUES (1, '192.168.1.0', '192.168.1.100', '北京一区');
INSERT INTO `ip_data` VALUES (2, '192.168.1.101', '192.168.1.255', '北京二区');
INSERT INTO `ip_data` VALUES (3, '192.168.2.1', '192.168.2.200', '重庆一区');
```

![](https://javaweb-community.oss-cn-beijing.aliyuncs.com/2018/1220/508e8eaea5374f91b58d58bad65aa2a0.png)

# 求：`192.168.1.25`所在范围？

## 方案一

* 也只能简单判断有序的ip段

```sql
SELECT
    * 
FROM
    `ip_data` 
WHERE
    begin_ip < '192.168.1.25' 
    AND end_ip < '192.168.1.25'
```

## 方案二

* 只能判断有序ip

> 实现效果

`SELECT * FROMip_dataWHERE begin_ip BETWEEN "192.168.1.0" and "192.168.1.200"`

```sql
SELECT
    * 
FROM
    `ip_data` 
WHERE
    INET_ATON( "192.168.1.25" ) BETWEEN INET_ATON( begin_ip ) 
    AND INET_ATON( end_ip )
```

## 方案三（推荐）

```sql
SELECT
	* 
FROM
	`ip_data` a 
WHERE
	SUBSTRING_INDEX( a.`begin_ip`, '.', 3 ) = SUBSTRING_INDEX( '192.168.1.58 
		
	', '.', 3 ) 
	AND SUBSTRING_INDEX( a.`end_ip`, '.', 3 ) = SUBSTRING_INDEX( '192.168.1.58 
		
	', '.', 3 ) 
	AND SUBSTRING_INDEX( a.`begin_ip`, '.', - 1 ) + 0 <= SUBSTRING_INDEX( '192.168.1.58 
		
		', '.', - 1 ) + 0 AND SUBSTRING_INDEX( a.`end_ip`, '.', - 1 ) + 0 >= SUBSTRING_INDEX( '192.168.1.58 
	
	', '.', - 1 ) +0
```
