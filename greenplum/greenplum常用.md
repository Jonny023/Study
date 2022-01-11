# greenplum常用

> 关系：数据库(database).模式(schema).表名(table)  例如：demo.test.user，demo为数据库，test为模式也即schema，user为用户表

## 库

### 创建库

```sql
-- shell模式下
createdb -E UTF8 -T template0 --locale=en_US.utf8 <name>
createdb demo

create database demo WITH ENCODING 'UTF8';
```

### 连接库

```shell
\c demo
```

### 查看库

```sql
\l
\l+
select datname from pg_database;
```

### 修改库

> 修改库的时候不能连接库

```sql
SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE datname='demo' AND pid<>pg_backend_pid();
alter database demo rename to demo1;
```

### 删除库

```
drop database demo;
```

### 查看数据库编码

```sql\
SHOW SERVER_ENCODING;
```



## 模式【schema】

```sql
-- 创建schema
create schema a;

-- 修改schema名
alter schema b rename to a;名

-- 删除schema
drop schema a;

-- 级联删除schema
drop schema test cascade;

-- 选择schema
set search_path to '_schema,public';

-- 查看当前schema
select current_schema();

-- 查看所有schema
select * from information_schema.schemata;

\dn
\dn+

-- 赋权
grant all on SCHEMA tran to public;
```



## 表

### 创建表

```sql
-- 创建序列
CREATE SEQUENCE user_id_seq START 1;

-- 创建表，表【id自增】需要用到上面创建的序列，通过nextval(args)函数指定
CREATE TABLE test."user" (
	id int8 NOT NULL primary key default nextval('user_id_seq'),
	username varchar(20) NOT null,
	create_time timestamp
)
DISTRIBUTED by(id);

-- 重置序列
alter sequence user_id_seq restart with 1;

-- 查看序列【执行一次+1】
SELECT nextval('user_id_seq');


-- 创建表，【id不自增】
CREATE TABLE test.user1 (
	id int8 NOT NULL,
	username varchar(20) NOT NULL,
	create_time timestamp NULL,
	CONSTRAINT user1_pkey PRIMARY KEY (id)
)


-- 创建主键自增表【推荐】
CREATE TABLE public.test (
	id serial NOT NULL,
	name varchar(20) NOT NULL,
	CONSTRAINT pk_test_id PRIMARY KEY (id)
)
DISTRIBUTED BY (id);


-- 创建主键自增表2【推荐】
CREATE TABLE public.test (
	id serial PRIMARY KEY,
	name varchar(20) NOT NULL
)
```

### 查看表

```sql
-- 查看test模式（schema）下的表
SELECT table_name FROM information_schema.tables WHERE table_schema = 'test';

-- 命令行查看表，必须选择schema
SET search_path= test;
\dt

-- 查看表结构
\d user;

-- 查看建表语句
pg_dump -st test.user demo
```

### 添加表备注

```sql
COMMENT ON TABLE "demo"."test"."user" IS '用户表';
```

### 修改表名

```sql
alter table test."user1" rename to "user"
```

### 添加字段

```sql
-- 添加字段并设置默认值，int类型默认为int4
ALTER TABLE "demo"."test"."user" ADD COLUMN utype INT DEFAULT(1);
```

### 添加主键

```sql
-- 添加主键
ALTER TABLE "demo"."test"."user" ADD PRIMARY KEY ("id");
```

### 查看表字段

> 相当于`describe user`

```sql
SELECT column_name FROM information_schema.columns WHERE table_name ='user';
```

### 添加字段注释

```sql
COMMENT ON COLUMN "demo"."test"."user"."utype"  IS '类型 1-普通，2-高级';
```

### 修改字段名

```sql
alter table "user" rename create_time to creater_by
```

### 修改字段类型

```sql
-- 原本为varchar的age修改为int2
alter table "user" alter column age type int2 using to_number(age,'2');
```

### 删除非空约束

```sql
ALTER TABLE "demo"."test"."user" ALTER COLUMN realname drop not NULL;
```

### 删除列

```sql
alter table "demo"."test"."user" drop column utype;
```



## 导入导出（备份/恢复）

### 导出

#### pg_dump

```shell
# 导出整个库
# 【表结构和数据】
pg_dump demo -f /home/gpadmin/bak.sql
gpadmin=# \! pg_dump demo -f /home/gpadmin/bak.sql
gpadmin=# \! pg_dump demo > /home/gpadmin/bak.sql
pg_dump -h 127.0.0.1 -U gpadmin -p 5432 -W demo --inserts > bak.sql
# 导出到文件，用pg_restore恢复
pg_dump -h 127.0.0.1 -U gpadmin demo -Fc > dump.dmp


# 【单表结构和数据】，test是schema，demo是数据库
# 【方式1】非insert
pg_dump -t 'test."user"' demo > bak.sql

# 【方式2】含结构，插入语句是insert
pg_dump -h localhost -p 5432 -U gpadmin -W -d demo -t 'test."user"' --inserts > /home/gpadmin/bak.sql

# 【多表】
pg_dump -t 'test."user"' -t 'test."user1"' demo > bak.sql
pg_dump -h localhost -p 5432 -U gpadmin -W -d demo -t 'test."user"' -t 'test."user1"' --inserts > /home/gpadmin/bak.sql

pg_dump -h localhost -U gpadmin -p 5432 -W gpadmin demo -t user –inserts > bak.sql





# 【仅导出结构】
# 【整库】
pg_dump -h 127.0.0.1 -U gpadmin -p 5432 -W demo -s > bak.sql

# 【指定模式schema】
pg_dump -h 127.0.0.1 -U gpadmin -p 5432 -W demo -n test -s > bak.sql

# 【指定表】
pg_dump -h 127.0.0.1 -U gpadmin -p 5432 -W demo -n test -s -t test."user" > bak.sql



# 【仅导出数据】
pg_dump -h 127.0.0.1 -U gpadmin -p 5432 -W demo --inserts -a > bak.sql
```

##### 常用参数

- -h host，指定数据库主机名，或者IP
- -p port，指定端口号
- -U user，指定连接使用的用户名
- -W，按提示输入密码
- dbname，指定连接的数据库名称，实际上也是要备份的数据库名称。
- -a，–data-only，只导出数据，不导出表结构
- -c，–clean，是否生成清理该数据库对象的语句，比如drop table
- -C，–create，是否输出一条创建数据库语句
- -f file，–file=file，输出到指定文件中
- -n schema，–schema=schema，只转存匹配schema的模式内容
- -N schema，–exclude-schema=schema，不转存匹配schema的模式内容
- -O，–no-owner，不设置导出对象的所有权
- -s，–schema-only，只导致对象定义模式，不导出数据
- -t table，–table=table，只转存匹配到的表，视图，序列，可以使用多个-t匹配多个表
- -T table，–exclude-table=table，不转存匹配到的表。
- –inserts，使用insert命令形式导出数据，这种方式比默认的copy方式慢很多，但是可用于将数据导入到非PostgreSQL数据库。
- –column-inserts，导出的数据，有显式列名

#### pg_dumpall

> 相对于`pg_dump`只能备份单个库，`pg_dumpall`可以备份整个postgresql实例中所有的数据，包括角色和表空间定义。

```shell
pg_dumpall -h localhost -U gpadmin -p 5432 -W --inserts > /home/gpadmin/bak.sql
```



### 导入

#### psql

```shell
psql -d demo -f /home/gpadmin/bak.sql
```

#### pg_restore

> 经测试，这种方式不大靠谱，容易报错

```shell
# 需要通过导出文件的方式
#pg_dump -h 192.168.211.152 -Upgsql test -Fc > remote_test.dmp

pg_restore -h localhost -U gpadmin -C -d demo /home/gpadmin/dump.dmp
pg_restore -d remote remote_test.dmp
```

