 > 查看编码
 
 ```mysql
 show variables like "%character%";


1.修改表的编码格式

ALTER TABLE `table` DEFAULT CHARACTER SET utf8;

2.修改字段编码格式

ALTER TABLE `table` CHANGE `字段1` `字段2` VARCHAR(36) CHARACTER SET utf8 NOT NULL;

3.修改表中所有字段的编码格式

ALTER TABLE `table` CONVERT TO CHARACTER SET utf8;

-- 将表编码转换成另一种格式
ALTER TABLE `table` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
 ```
