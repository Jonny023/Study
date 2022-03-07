# mysql优化

## explain执行计划说明

* 可通过`执行计划查看优化后的sql`，mysql8不用`explain extended`，直接执行`explain select * from table`，再通过`show warnings`就能看到了

```sql
explain extended select * from employee where id = 1;
show warnings;

-- /* select#1 */ select '1' AS `id`,'20' AS `salary`,'question' AS `content` from `test`.`employee` where 1

--  /* select#1 */ select `test`.`employee`.`id` AS `id`,`test`.`employee`.`salary` AS `salary`,`test`.`employee`.`content` AS `content` from `test`.`employee` where ((`test`.`employee`.`id` = 1) or (`test`.`employee`.`id` = 2))
```

### id

* id列的编号是select的序列号，有多少select就有几个id，并按select出现顺序递增

> 执行执行顺序

* id从大到小执行
* id相同从上到下执行
* id为NULL最后执行

### select_type

* simple: 简单查询，不包含子查询和union

* primary: 复杂查询最外层select

* subquery: 子查询

* derived: 临时表 （派生表）

  ```sh
  #  关闭mysql5.7新特性对衍生表的合并优化
  set session optimizer_switch='derived_merge=off';
  
  # 打开派生表优化
  set session optimizer_switch='derived_merge=on';
  ```

### table

​	* 查询的表

### type类型

> 关联类型最优到最差：`system>const>eq_ref>ref>range>index>ALL`

* `system` 对const进行结果进行二次筛选
* `const` 主键索引（结果小于等于1）
* `eq_ref` 主键联表（主键索引或唯一索引联表查询`t1.id = t2.id`）
* `ref` 普通索引（非唯一主键查询，如：`where name='张三'`或关联查询）
* `range` 范围查询（`between、in、<、>、>=`）
* `index` 全表索引扫描（这个表所有列都有索引，则执行计划type为index，若其中一列没有索引，则type为ALL）
* `ALL` 全表扫描（无索引）
* `NULL` mysql优化不需要访问表或索引

### possible_keys

> 可能会用到的索引

### key

> 查询中用到的索引

### key_len

> 索引的长度

#### 计算规则

* 字符串
  * `char(n)`: n字节长度, 最大长度是255个字符，如果是utf8编码方式， 那么char类型占3n个字节
    * 存储到数据库中是固定长度，如果长度达不到指定长度，会在字符后添加空格，查询比varchar快，属于空间换时间，长度是n
  * `varchar(n)`: 2n字节存储字符串长度，果果是utf-8，则长度为3n+2，最多存65532/3 = 21844个字符
    * 变长型字符，存多少字符就占用多少空间，不像char是固定长度
* 数值型
  * `tinyint(n)` 1字节
  * `smallint(n)` 2字节
  * `int(n)` 4字节
  * `bigint(n)` 8字节
  * `float(n)` 4字节
  * `double(n)` 8字节
  * `decimal(M,D)` 如果M>D，为M+2，否则为D+2
* 时间类型
  * `date(n)` 3字节
  * `timestamp(n)` 4字节
  * `datetime(n)` 8字节
* 如果字段允许为null需要1个字节记录是否为null，索引最大长度是768个字节，当字符串过长时，mysql会做一个类似左前缀的处理，将前半部分提取出来做索引

### ref

* 表查找用到的列或常量(等号右边的值)，如`const(常量)`、`字段名（t1.id）`

### rows

* 索引扫描行数

### Extra

* `Using index`: 使用覆盖索引（`查询列都有索引`）
* `Using where`: 使用where语句处理结果，查询结果未被索引覆盖
* `Using index condition`: 查询的列不完全被索引覆盖，where条件中是一个前导列的范围
* `Using temporary`: mysql需要创建一张临时表来处理查询，如：`explain select distinct question_title from question`，`question_title没有索引`
* `Using filesort`: 将用外部排序而不是索引排序，数据较小时从内存排序，否则在磁盘完成排序（`排序字段没有索引`）。
* `Select table optimized away`: 使用聚合函数（min、max）来访问存在索引的某个字段时



## 优化规则

* 1.`全值匹配`（where条件都有索引）
* 2.`最左前缀`（若索引了多列，要遵循最左前缀法。当查询条件存在联合索引时，要按联合索引的顺序进行where查询，如联合索引为：name_age_addr_idx，查询where需要满足`where name = 'xx' and age = 20 and addr = 'xxx'`）,反例：`where addr='xxx' and age=20`,就会导致索引无效
* 3.不在索引列上做（`计算、函数、（自动或手动）类型转换`），或导致索引实现而进行全表扫描
* 4.字符查询必须加单引号，如：`tel='10086'`，如果是`tel=10086`会进行`cast()`函数转换
  * 字符类型的字段，不要用number类型进行查询，如字符类型的电话号码，如果用tel=10086是不会走索引的，因为这里有个类型转换问题，会将数值转换为字符，但如果是数值类型，比如自增id，用id='10001'是会走索引，字符会隐式转换为int类型
* 5.范围右边的查询不会走索引，如：`select * from employee where content = '2' and id > 1 and salary = 40;`导致部分索引失效
* 6.尽量使用覆盖索引（索引列包含查询列），减少`select *` 语句

* 7.避免使用`!=或<>`查询
* 8.`is null`、`is not null`也无法使用索引
  
  * 优化：字段设置`not null`
* 9.`like`以通配符前缀开头`‘%xxx’`会导致索引失效
  * 解决
    * a）使用覆盖索引，查询字段必须是建立覆盖索引字段，查询列必须存在于索引中
    * b）使用全文索引

* 10.`少用or或in查询（not in、not exists）`，用他做查询时不一定走索引，mysql内部优化器会根据检索比例，表大小 等因素等多个因素整体评估是否使用索引

* 11.范围查询优化

  * 增加单值索引

    * 不走索引：`select * from student where score>=1 and score<=2000`

      * 优化：缩小范围

        ```sql
        select * from student where score>=1 and score<=1000;
        select * from student where score>=10001 and score<=2000;
        ```

        