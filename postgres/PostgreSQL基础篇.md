# PostgreSQL基础篇】

### 

* 查看数据存放路径

```sql
show data_directory;
select setting from pg_settings where name='data_directory';
```

* `data`目录结构

|序号| 目录名称     | 说明  |
|--| ------------ | ------------------------------------------------- |
|1| base         | 该目录包含数据库用户所创建的各个数据库，同时也包括postgres、template0和template1的pg_default tablespace |
| 2 | pg_xlog      | 该目录包含wal日志。默认为16M，编译安装时可指定大小： --with-wal-segsize=64（64M）格式：000000010000000000000008， 当占用比较大空间时，空间不足，导致数据库启动不了。可以把比较旧的xlog移动到别的目录。 wal_keep_segments=100  保留文件数。占用空间wal_keep_segments*16M |
| 3 | hgdb_log     | 该目录包含数据库日志。(目录名可自定义)                       |
| 4 | global       | 该目录包含集群范围的各个表和相关视图。 （ pg_database、 pg_tablespace ） |
| 5 | pg_clog      | 该目录包含事务提交状态数据。文件并不大，不需要特别维护。     |
| 6 | pg_multixact | 该目录包含多事务状态数据（等待锁定的并发事务）               |
| 7 | pg_notify    | 该目录包含LISTEN/NOTIFY状态数据。                            |
| 8 | pg_serial    | 该目录包含了已经提交的序列化事务的有关信息。                 |
| 9 | pg_snapshots | 该目录包含导出的快照。                                       |
| 10 | pg_stat_tmp  | 该目录包含统计子系统的临时文件。                             |
| 11 | pg_subtrans  | 该目录包含子事务状态数据。                                   |
| 12 | pg_tblspc    | 该目录包含表空间的符号链接。                                 |
| 13 | pg_twophase  | 该目录包含预备事务的状态文件。                               |
| 14 | pg_commit_ts | 该目录包含已提交事务的时间。                                 |
| 15 | pg_dynshmem  | 该目录包含动态共享内存子系统使用的文件。                     |
| 16 | pg_logical   | 该目录包含逻辑解码的状态数据。                               |
| 17 | pg_replslot  | 该目录包含复制槽数据。                                       |
| 18 | pg_stat      | 该目录包含统计子系统的永久文件。                             |
| 19 | PG_VERSION   | 包含版本信息。                                               |



### 一、数据类型

1、数值型数据类型

| Name    | Storage Size | Description                    | Range                                                        |
| ------- | ------------ | ------------------------------ | ------------------------------------------------------------ |
| int2    | 2 bytes      | small-range integer            | -32768 to +32767                                             |
| int4    | 4 bytes      | typical choice for integer     | -2147483648 to +2147483647                                   |
| int8    | 8 bytes      | large-range integer            | -9223372036854775808 to 9223372036854775807                  |
| decimal | variable     | user-specified precision,exact | up to 131072 digits before the decimal point; up to 16383 digits after the decimal point |
| float4  | 4 bytes      | variable-precision,inexact     | 6 decimal digits precision                                   |
| float8  | 8 bytes      | variable-precision,inexact     | 15 decimal digits precision                                  |
| serial2 | 2 bytes      | small autoincrementing integer | 1 to 32767                                                   |
| serial4 | 4 bytes      | autoincrementing integer       | 1 to 2147483647                                              |
| serial8 | 8 bytes      | large autoincrementing integer | 1 to 9223372036854775807                                     |

2、货币型（不推荐使用） 

| Name  | Storage Size | Description     | Range                                          |
| ----- | ------------ | --------------- | ---------------------------------------------- |
| money | 8 bytes      | currency amount | -92233720368547758.08 to +92233720368547758.07 |

3、字符型

| 名称       | 描述                       |
| ---------- | -------------------------- |
| varchar(n) | variable-length with limit |
| char(n)    | fixed-length, blank padded |
| text       | variable unlimited length  |

```sql
show lc_monetary;
set lc_monetary='Chinese_China.936';
select 1.1::money;

set lc_monetary='C';
select 1.1::money;
show lc_monetary;
```

4、日期、时间类型

### 3.1 列表

| 字符类型名称                           | 存储长度 | 描述                                                         |
| -------------------------------------- | -------- | ------------------------------------------------------------ |
| `timestamp [(p)] [without time zone ]` | 8 bytes  | 包括日期和时间，不带时区，简写timestamp                      |
| `timestamp [(p) ] with time zone`      | 8 bytes  | 包括日期和时间，带时区，简写timestamp                        |
| `date`                                 | 4 bytes  | 不含时间                                                     |
| `time [ (p)] [ without time zone ]`    | 8 bytes  | time of day (no date) 00:00:00 24:00:00                      |
| `time [ (p)] with time zone`           | 12 bytes | times of day only, with time zone 00:00:00+1459 24:00:00-1459 |
| `interval [fields ] [(p) ]`            | 12 bytes | time interval -178000000 years 178000000 years               |

### 二、函数

时间/日期常用函数

| 函数名称                     | 作用描述                                                     |
| ---------------------------- | ------------------------------------------------------------ |
| `current_date`               | 当前日期                                                     |
| `current_time`               | 当前时间                                                     |
| `extract(field FROM source)` | 可以从日期、时间数据类型中抽取年、月、日、时、分、秒信息 field可以是century、year、day、hour、minute、second等 source类型为timestamp、time、interval的表达式 |

### 三、查询

```sql
-- 时间戳求和分组
select
	sum(adsburiedp0_.ip_count) as ip_count ,
	sum(adsburiedp0_.pv) as pv5_7_,
	sum(adsburiedp0_.uv) as si6_7_,
	to_char(to_timestamp(adsburiedp0_.time / 1000), 'YYYY-MM-DD') as res_time
from
	ads_buried_point_web_1h adsburiedp0_
where
	to_char(to_timestamp(adsburiedp0_.time / 1000), 'YYYY-MM-DD HH24:MI:SS') like '2021-03-%'
group by
	res_time
```



## 注意

