

* 查看表结构

```sql
describe user;
```

* 查看索引

```sql
show index from user;
show keys from user;
```

* 查看运行线程数

```sql
show processlist;
```

* 查看`sql`执行历史

```sql
show profiles;
```

* 通过`explain`查看`sql`执行效率

```bash
mysql> explain SELECT * FROM `user`;
+----+------------+-------+-----------+-----+-------------+------+---------+------+------+----------+-------+
| id | select_type| table | partitions| type|possible_keys| key  | key_len | ref  | rows | filtered | Extra |
+----+------------+-------+-----------+-----+-------------+------+---------+------+------+----------+-------+
|  1 | SIMPLE     | user  | NULL      | ALL | NULL        | NULL | NULL    | NULL |   13 |   100.00 | NULL  |
+----+------------+-------+-----------+-----+-------------+------+---------+------+------+----------+-------+
1 row in set (0.02 sec)
```

| 列              | 描述                                                         |
| --------------- | ------------------------------------------------------------ |
| `id`            | `select` 查询序列号。`id`相同，执行顺序由上至下；`id`不同，`id`值越大优先级越高，越先被执行 |
| `select_type`   | `simple`：简单查询，不包含子查询或 `union`<br/> `primary`:包含复杂的子查询，最外层查询标记为该值 <br/>`subquery`：在 `select` 或 `where` 包含子查询，被标记为该值 <br/>`derived`：在 `from` 列表中包含的子查询被标记为该值，`MySQL` 会递归执行这些子查询，把结果放在临时表 <br/>`union`：若第二个 `select` 出现在 `union` 之后，则被标记为该值。若 `union` 包含在 `from` 的子查询中，外层 `select` 被标记为 `derived`     <br/>`union result`：从 `union` 表获取结果的 `select` |
| `table`         | 显示该行数据是关于哪张表                                     |
| `partitions`    | 匹配的分区                                                   |
| `type`          | `system`：表只有一行记录，相当于系统表 <br/>`const`：通过索引一次就找到，只匹配一行数据 <br/>`eq_ref`：唯一性索引扫描，对于每个索引键，表中只有一条记录与之匹配。常用于主键或唯一索引扫描 <br/>`ref`：非唯一性索引扫描，返回匹配某个单独值的所有行。用于`=`、`<` 或 `>` 操作符带索引的列 `range`：只检索给定范围的行，使用一个索引来选择行。一般使用`between`、`>`、`<`情况 `index`：只遍历索引树 <br/>`ALL`：全表扫描，性能最差<br/>**注：前5种情况都是理想情况的索引使用情况。通常优化至少到range级别，最好能优化到 ref** |
| `possible_keys` | 指出 `MySQL` 使用哪个索引在该表找到行记录。如果该值为 `NULL`，说明没有使用索引，可以建立索引提高性能 |
| `key`           | 显示 `MySQL` 实际使用的索引。如果为 `NULL`，则没有使用索引查询 |
| `key_len`       | 表示索引中使用的字节数，通过该列计算查询中使用的索引的长度。在不损失精确性的情况下，长度越短越好，显示的是索引字段的最大长度，并非实际使用长度 |
| `ref`           | 显示该表的索引字段关联了哪张表的哪个字段                     |
| `rows`          | 根据表统计信息及选用情况，大致估算出找到所需的记录或所需读取的行数，数值越小越好 |
| `filtered`      | 返回结果的行数占读取行数的百分比，值越大越好                 |
| `extra`         | `using filesort`：说明 `MySQL` 会对数据使用一个外部的索引排序，而不是按照表内的索引顺序进行读取。出现该值，应该优化 `SQL`<br/>`using temporary`：使用了临时表保存中间结果，`MySQL` 在对查询结果排序时使用临时表。常见于排序 `order by` 和分组查询 `group by`。出现该值，应该优化 `SQL` <br/>`using index`：表示相应的 `select` 操作使用了覆盖索引，避免了访问表的数据行，效率不错<br/>`using where`：`where` 子句用于限制哪一行 `using join buffer`：使用连接缓存 <br/>`distinct`：发现第一个匹配后，停止为当前的行组合搜索更多的行<br/>**注：出现前 2 个值，SQL 语句必须要优化。** |



## 添加索引

#### 普通索引(`INDEX`)

> 语法：ALTER TABLE `表名字` ADD INDEX 索引名字 ( `字段名字` )

```sql
-- –直接创建索引
CREATE INDEX index_user ON user(title)
-- –修改表结构的方式添加索引
ALTER TABLE table_name ADD INDEX index_name ON (column(length))
-- 给 user 表中的 name字段 添加普通索引(INDEX)
ALTER TABLE `table` ADD INDEX index_name (name)
-- –创建表的时候同时创建索引
CREATE TABLE `table` (
    `id` int(11) NOT NULL AUTO_INCREMENT ,
    `title` char(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
    `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
    `time` int(10) NULL DEFAULT NULL ,
    PRIMARY KEY (`id`),
    INDEX index_name (title(length))
)
-- –删除索引
DROP INDEX index_name ON table
```



#### 主键索引(`PRIMARY key`)

> 语法：ALTER TABLE `表名字` ADD PRIMARY KEY ( `字段名字` )

```sql
-- 给 user 表中的 id字段 添加主键索引(PRIMARY key)
ALTER TABLE `user` ADD PRIMARY key (id);
```

#### 唯一索引(`UNIQUE`)

> 语法：ALTER TABLE `表名字` ADD UNIQUE (`字段名字`)

```sql
-- 给 user 表中的 creattime 字段添加唯一索引(UNIQUE)
ALTER TABLE `user` ADD UNIQUE (creattime);
```

#### 全文索引(`FULLTEXT`)

> 语法：ALTER TABLE `表名字` ADD FULLTEXT (`字段名字`)

```sql
-- 给 user 表中的 description 字段添加全文索引(FULLTEXT)
ALTER TABLE `user` ADD FULLTEXT (description);
```

#### 组合索引(多列索引)

> ALTER TABLE `table_name` ADD INDEX index_name ( `column1`, `column2`, `column3`)

```sql
-- 给 user 表中的 name、city、age 字段添加名字为name_city_age的普通索引(INDEX)
ALTER TABLE user ADD INDEX name_city_age (name(10),city,age); 
```



## 适合使用引用

#### 1) 主键自动创建唯一索引

#### 2) 频繁作为查询条件的字段

#### 3) 查询中与其他表关联的字段

#### 4) 查询中排序的字段

#### 5) 查询中统计或分组字段



## 不适合使用索引

#### 1) 频繁更新的字段

#### 2) `where` 条件中用不到的字段

#### 3) 表记录太少

#### 4) 经常增删改的表

#### 5) 字段的值的差异性不大或重复性高



## 索引创建和使用原则

#### 1) 单表查询：哪个列作查询条件，就在该列创建索引

#### 2) 多表查询：`left join` 时，索引添加到右表关联字段；`right join` 时，索引添加到左表关联字段

#### 3) 不要对索引列进行任何操作（计算、函数、类型转换）

#### 4) 索引列中不要使用 `!=`，`<>` 非等于

#### 5) 索引列不要为空，且不要使用 `is null` 或 `is not null` 判断

#### 6) 索引字段是字符串类型，查询条件的值要加''单引号,避免底层类型自动转换



## 索引失效情况

#### 1) 模糊查询时，以 `%` 开头

#### 2) 使用 `or` 时，如：字段1（非索引）`or` 字段2（索引）会导致索引失效。

#### 3) 使用复合索引时，不使用第一个索引列。

#### 4) 某些时候的`LIKE`也会使用索引。

`index(a,b,c)` ，以字段 `a`,`b`,`c` 作为复合索引为例：

| **语句**                                  | 索引是否生效                      |
| ----------------------------------------- | --------------------------------- |
| `where a = 1`                             | 是，字段 `a` 索引生效             |
| `where a = 1 and b = 2`                   | 是，字段 `a` 和 `b` 索引生效      |
| `where a = 1 and b = 2 and c = 3`         | 是，全部生效                      |
| `where b = 2 or c = 3`                    | 否                                |
| `where a = 1 and c = 3`                   | 字段 `a` 生效，字段 `c` 失效      |
| `where a = 1 and b > 2 and c = 3`         | 字段 `a`，`b` 生效，字段 `c` 失效 |
| `where a = 1 and b like 'xxx%' and c = 3` | 字段 `a`，`b` 生效，字段 `c` 失效 |



## 数据库表结构设计

#### 1) 使用可以存下数据最小的数据类型

#### 2) 使用简单的数据类型。`int` 要比 `varchar` 类型在`mysql`处理简单

#### 3) 尽量使用 `tinyint`、`smallint`、`mediumint` 作为整数类型而非 `int`

#### 4) 尽可能使用 `not null` 定义字段，因为 `null` 占用4字节空间

#### 5) 尽量少用 `text` 类型,非用不可时最好考虑分表

#### 6) 尽量使用 `timestamp` 而非 `datetime`

#### 7) 单表不要有太多字段，建议在 20 以内



[参考一](https://juejin.im/post/59d83f1651882545eb54fc7e#heading-7)

[参考二](https://juejin.im/entry/57d60ef80e3dd90069de1eb5)

