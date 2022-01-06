# clickhouse DDL DML DQL

[参考1](https://blog.csdn.net/weixin_40104766/article/details/121259801)

## 1.DDL

> ATTACH 也可以建库，但是metadata目录下不会生成.sql文件，一般用于metadata元数据sql文件被删除后，恢复库表结构使用

```sql
CREATE/ATTACH DATABASE demo ENGINE = Ordinary;
```

| 数据库引擎 | 说明                                                         |
| ---------- | ------------------------------------------------------------ |
| Atomic     | `新版本新增的默认db引擎`，避免CREATE/DROP/RENAME TABLE锁，支持non-blocking DROP/RENAME表，支持原子交换表名EXCHANGE TABLES t1 AND t2 |
| Ordinary   | `旧版本默认引擎`，在绝大多数情况下我们都会使用默认的存储引擎，无须指定声明。在此数据库引擎下可以使用任意类型的表引擎 |
| Dictionary | 字典引擎，此类数据库会自动为所有数据字典创建他们的数据表     |
| Memory     | 内存引擎，用于存放临时数据。此类数据库下的数据表只会驻留在内存中，不会涉及任务磁盘操作，当服务重启数据会被清除 |
| Lazy       | 日志引擎，此类数据库引擎下只能使用Log系列的表引擎            |
| MySQL      | MySQL引擎，此类数据库下会自动拉取远端的MySQL中的数据，并为他们创建MySQL中标引擎的数据表 |



### 1.1 库

#### 1.1.1 查看库

```sql
-- 查看所有数据库
show databases;

-- 数据库相关信息
SELECT * FROM system.databases;
```

#### 1.1.2 创建库

```sql
-- 创建数据库
create database if not exists demo;
create database if not exists demo engine = Ordinary;
```

#### 1.1.3 切换库

```sql
-- 切换数据库
use demo;
```

#### 1.1.4 删除库

```sql
-- 删除数据库
drop database if exists demo;
```

#### 1.1.5 建库语句

```sql
-- 查看创建db语句
show create database demo;
```

#### 1.1.6 重命名

> `RENAME`仅支持[Atomic](https://clickhouse.com/docs/zh/engines/database-engines/atomic/)数据库引擎，[更多](https://clickhouse.com/docs/zh/sql-reference/statements/rename/)

```sql
rename database demo to test
```



### 1.2 表

> 目前只有`MergeTree`、`Merge`和`Distributed`这三类表引擎支持`ALTER`查询

#### 1.2.1 查看表

```sql
-- 查看所有表
show tables;

-- 查看制定库所有表
show tables from default;

-- 查看表结构
desc dataset;

-- 查看分区
SELECT partition FROM system.parts p where table = 'dataset';
```

#### 1.2.2 创建表

```sql
-- 创建表
CREATE TABLE IF NOT EXISTS dataset (
  create_time DateTime comment '创建时间',
  id UInt32,
  content String
) ENGINE=MergeTree
PARTITION BY toYYYYMM(create_time)
ORDER BY (id, create_time);

-- ReplacingMergeTree支持主键，但不具备约束性
-- 在MergeTree基础之上加入去重功能，但仅在合并分区时
-- ReplacingMergeTree([ver])若未指定ver，则合并式保留组内最后一条数据
-- 若指定了ver参数，则保留ver取值最大的那一行
-- optimize table dataset FINAL;
-- select * from dataset;
CREATE TABLE IF NOT EXISTS dataset (
  create_time DateTime,
  id UInt32,
  content String
) ENGINE=ReplacingMergeTree(id)
PARTITION BY toYYYYMM(create_time)
ORDER BY (id, create_time)
PRIMARY KEY id;
```

#### 1.2.3 删除表

```sql
-- 删除表
drop table if exists dataset;
```

#### 1.2.4 表命名

> 重命名：rename table a to b，将表a命名为b
>
> 迁移库：rename table a.x to b.x 将x表从a库迁移到b库

```sql
-- 把一个表从a库迁移到b库【重命名】
rename table demo.dataset to demo1.dataset;	
```

#### 1.2.5 表字段

> 语法: ALTER TABLE [$db_name.]$table_name [ON CLUSTER cluster] ADD|DROP|CLEAR|COMMENT|MODIFY COLUMN 

```sql
-- 【添加备注】
ALTER TABLE dataset COMMENT COLUMN IF EXISTS id '主键id';

-- 【添加字段】
ALTER TABLE $table_name ADD COLUMN [IF EXISTS] $column_name [type] [default_expr] [codec] [AFTER $pre_column_name]
-- 添加字段到指定列后
ALTER TABLE dataset ADD COLUMN update_time DateTime AFTER content;

-- 修改【字段类型】
alter table dataset modify column update_time DateTime;

-- 【删除字段】
alter table dataset drop column update_time;

-- 【修改字段名】
ALTER TABLE dataset RENAME COLUMN update_time TO modify_time;
```

#### 1.2.6 复制表

```sql
-- 复制表结构
create table demo engine = MergeTree order by create_time as dataset;
```

#### 1.2.7 清空表

```sql
TRUNCATE TABLE IF EXISTS demo;
```



### 1.3 视图

#### 1.3.1 普通视图

```sql
-- 创建视图【普通视图，只作为查询映射】
CREATE VIEW IF NOT EXISTS dataset AS SELECT * from dataset;
```

#### 1.3.2 删除视图

```sql
drop view if exists dataset;
```

## 2. DML

### insert

```sql
insert into dataset(id,create_time,content) values(1,'2021-01-05 00:00:00','你好');

-- 插入记录
insert into dataset(id,create_time,content) values(1,now(),'你好');
```

### update

```sql
-- 修改记录
ALTER TABLE dataset UPDATE content='hello' WHERE id=1;
```

### delete

```sql
-- 删除记录【按条件】
alter table dataset delete where create_time < now();
-- 【按分区】
alter table dataset drop PARTITION '202201';
```



## 3. DQL

```sql
-- 查看返回数据类型
select toTypeName(now());
```



## 4.导入导出

### 4.1 表结构导入导出

> FORMAT有多中格式：TSV、TSVRaw、JSON等，只有TSVRaw格式导出后才能正常执行，其他方式带有`\n`换行符号，需要手动替换才能正常执行。更多格式[请参考](https://clickhouse.com/docs/zh/interfaces/formats/)

```shell
# 【===推荐===】
# 【导出】
clickhouse-client -uroot --password root -d test -q "SHOW CREATE TABLE dataset1 FORMAT TSVRaw" > bak.sql
# 【导入】
clickhouse-client -uroot --password root -d test --multiquery < bak.sql


# 【以下导出方方式都不推荐，因为需去掉换行符才能执行】
# 导出表结构
clickhouse-client -uroot --password=root --query="SHOW CREATE TABLE test.dataset1" --format=TSV > datasetdump.tsv
# 导入
clickhouse-client -uroot --password root -d test --multiquery < datasetdump.tsv


# 导出表结构
echo 'show create table dataset' | curl localhost:8123?database=test -uroot:root -d @- > test_trunc.sql
# 执行sql
clickhouse-client -uroot --password root --database="test" --multiquery < /root/test_trunc.sql
```



### 4.2数据导入导出

```shell
# 【推荐用法】数据导入导出
# 这种方式导出会将列名一并导出，防止数据错乱
# 导出
clickhouse-client -u root --password root --query="select * from test.dataset FORMAT Native" > xxx.native
# 导入
clickhouse-client  -u root --password root --query="INSERT INTO test.dataset FORMAT Native" < xxx.native



# 【导出】
clickhouse-client -u root --password root --query="select * from test.dataset" > ~/test_data.tsv
SELECT * FROM dataset INTO OUTFILE '/root/result.sql';

# 调用api
# 导出数据
echo 'select * from dataset' | curl localhost:8123?database=test -uroot:root -d @- > test_data.sql

# 【导入】
cat ~/test_data.tsv | clickhouse-client -u root --password root --query="insert into test.dataset FORMAT TSV"


# PrettyCompactNoEscapes|XML|JSON|TSVRaw|TSV|Native
# 查看数据
watch -n1 "clickhouse-client -uroot --password root -d test --query='SELECT * FROM dataset limit 10 FORMAT PrettyCompactNoEscapes'"
```

## 5.HTTP客户端

> [详情](https://clickhouse.com/docs/zh/interfaces/http/)

```shell
echo 'SELECT * from dataset' | curl 'http://localhost:8123/?database=test&user=root&password=root' --data-binary @-
```

