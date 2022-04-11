# Clickhouse 实战

### ck代理

[ckproxy](https://github.com/Vertamedia/chproxy)

## 远程连接

> 注意：连接端口`8123`是`http`，`9000`才是tcp，用`client`连接时要用`9000`

```shell
clickhouse-client -h 192.168.1.10 --port 9000 -d database -m -u collect --password Abc123

clickhouse-client -h 127.0.0.1 --port 9000 -d test -u default --password=123456
```

### 命令行参数

- `--host, -h` -– 服务端的 host 名称, 默认是 'localhost'。 您可以选择使用 host 名称或者 IPv4 或 IPv6 地址。
- `--port` – 连接的端口，默认值： 9000。注意 HTTP 接口以及 TCP 原生接口是使用不同端口的。
- `--user, -u` – 用户名。 默认值： default。
- `--password` – 密码。 默认值： 空字符串。
- `--query, -q` – 非交互模式下的查询语句.
- `--database, -d` – 默认当前操作的数据库. 默认值： 服务端默认的配置 （默认是 `default`）。
- `--multiline, -m` – 如果指定，允许多行语句查询（Enter 仅代表换行，不代表查询语句完结）。
- `--multiquery, -n` – 如果指定, 允许处理用逗号分隔的多个查询，只在非交互模式下生效。
- `--format, -f` – 使用指定的默认格式输出结果。
- `--vertical, -E` – 如果指定，默认情况下使用垂直格式输出结果。这与 '--format=Vertical' 相同。在这种格式中，每个值都在单独的行上打印，这种方式对显示宽表很有帮助。
- `--time, -t` – 如果指定，非交互模式下会打印查询执行的时间到 'stderr' 中。
- `--stacktrace` – 如果指定，如果出现异常，会打印堆栈跟踪信息。
- `-config-file` – 配置文件的名称。

### clickhouse-client 使用的主要参数：

| 参数               | 说明                              |
| :----------------- | :-------------------------------- |
| -C --config-file   | 指定客户端使用的配置文件          |
| -h --host          | 指定 clickhouse-server 的 IP 地址 |
| --port             | 指定 clickhouse-server 的端口地址 |
| -u --user          | 用户名                            |
| --password         | 密码                              |
| -d --database      | 数据库名称                        |
| -V --version       | 查看客户端版本                    |
| -E --vertical      | 查询结果按照垂直格式显示          |
| -q --query         | 非交互模式下传入的 SQL 语句       |
| -t --time          | 非交互模式下显示执行时间          |
| --log-level        | 客户端日志级别                    |
| --send_logs_level  | 指定服务端返回日志数据的级别      |
| --server_logs_file | 指定服务端日志保存路径            |

## 导入数据

### 导入

```shell
cat /data/account.csv | clickhouse-client - h127.0.0.1 --database=testdb --query="INSERT INTO account FORMAT CSVWithNames"

clickhouse-client --multiquery < /home/xxx.sql
```



## 不同语言客户端

* [客户端地址](https://clickhouse.tech/docs/en/interfaces/third-party/client-libraries/)

## [clickhouse文档](https://cloud.tencent.com/document/product/1299/49847)

### 数据类型

| 类别                 | 名称                                 | 类型标识                                     | 数据范围或描述                                               |
| :------------------- | :----------------------------------- | :------------------------------------------- | :----------------------------------------------------------- |
| 整数                 | 单字节整数                           | Int8                                         | [-128，127]                                                  |
| 双字节整数           | Int16                                | [-32768，32767]                              |                                                              |
| 四字节整数           | Int32                                | [-2147483648，2147483647]                    |                                                              |
| 八字节整数           | Int64                                | [-9223372036854775808，9223372036854775807]  |                                                              |
| 无符号单字节整数     | UInt8                                | [ 0，255]                                    |                                                              |
| 无符号双字节整数     | UInt16                               | [ 0，65535]                                  |                                                              |
| 无符号四字节整数     | UInt32                               | [ 0，4294967295]                             |                                                              |
| 无符号八字节整数     | UInt64                               | [ 0，18446744073709551615]                   |                                                              |
| 浮点数               | 单精度浮点数                         | Float32                                      | 浮点数有效数字6 - 7位                                        |
| 双精度浮点数         | Float64                              | 浮点数有效数字15 - 16位                      |                                                              |
| 自定义浮点           | Decimal32(S)                         | 浮点数有效数字 S，S 取值范围[1，9]           |                                                              |
| Decimal64(S)         | 浮点数有效数字 S，S 取值范围[10，18] |                                              |                                                              |
| Decimal128(S)        | 浮点数有效数字 S，S 取值范围[19，38] |                                              |                                                              |
| 字符型               | 任意长度字符                         | String                                       | 不限定字符串长度                                             |
| 固定长度字符         | FixedString(N)                       | 固定长度的字符串                             |                                                              |
| 唯一标识 UUID 类型   | UUID                                 | 通过内置函数 generateUUIDv4 生成唯一的标志符 |                                                              |
| 时间类型             | 日期类型                             | Date                                         | 存储年月日时间，格式 yyyy-MM-dd                              |
| 时间戳类型（秒级）   | DateTime(timezone)                   | Unix 时间戳，精确到秒                        |                                                              |
| 时间戳类型（自定义） | DateTime(precision, timezone)        | 可以指定时间精度                             |                                                              |
| 枚举类型             | 单字节枚举                           | Enum8                                        | 取值范围为[-128，127]，共256个值   type Enum('show'=1,'hide'=2) |
| 双字节枚举           | Enum16                               | 取值范围为[-32768，32767]，共65536个值       |                                                              |
| 数组类型             | 数组类型                             | Array(T)                                     | 表示由 T 类型组成的数组类型，不推荐使用嵌套数组              |

## 角色(role)

```sql
/* superman user*/
CREATE USER IF NOT EXISTS  admin ON CLUSTER user_logs IDENTIFIED WITH plaintext_password BY '123456';
GRANT ON CLUSTER user_logs ALL ON *.* TO admin WITH GRANT OPTION;

/* read_only role */
CREATE  ROLE IF NOT EXISTS  read_only_roles ON CLUSTER user_logs;
GRANT ON CLUSTER user_logs SELECT ON test_logs.* TO read_only_roles;

CREATE USER IF NOT EXISTS  web ON CLUSTER user_logs IDENTIFIED WITH plaintext_password BY '123456' DEFAULT ROLE read_only_roles;



/*  read_write_apps_roles role */
CREATE  ROLE IF NOT EXISTS  read_write_apps_roles ON CLUSTER user_logs ;
GRANT ON CLUSTER user_logs SELECT,INSERT ON test_logs.* TO read_write_apps_roles; 
CREATE USER IF NOT EXISTS  collect ON CLUSTER user_logs IDENTIFIED WITH plaintext_password BY '123456' DEFAULT ROLE read_write_apps_roles; 


/*delete user or role */
/*

REVOKE ON CLUSTER user_logs ALL ON test_logs.* from read_only_roles

REVOKE ON CLUSTER user_logs ALL ON test_logs.* from read_write_apps_roles


drop role IF  EXISTS read_only_roles ON CLUSTER user_logs ;

drop role IF  EXISTS read_write_apps_role ON CLUSTER user_logs ;

drop user IF  EXISTS web  ON CLUSTER user_logs  

drop user IF  EXISTS collect  ON CLUSTER user_logs  

*/

/*  test  role and user */

/*
CREATE SETTINGS PROFILE IF NOT EXISTS test_profile  ON CLUSTER user_logs settings readonly=0 READONLY


CREATE  ROLE IF NOT EXISTS  test_roles ON CLUSTER user_logs settings profile 'test_profile';

CREATE USER IF NOT EXISTS  test_user ON CLUSTER user_logs IDENTIFIED WITH no_password DEFAULT ROLE test_roles 

GRANT ON CLUSTER user_logs ALL ON test_logs.* TO test_roles;

drop role IF  EXISTS test_roles ON CLUSTER user_logs ;

DROP SETTINGS PROFILE test_profile ON CLUSTER user_logs

drop user IF  EXISTS test_user  ON CLUSTER user_logs  
*/
```

## 表（table）

```sql
create database if NOT exists test_logs ON CLUSTER user_logs;

use test_logs;

/*
drop database if  exists test_logs ON CLUSTER user_logs;
*/

 
/*test*/
create table IF NOT EXISTS test_logs.test_basics ON CLUSTER user_logs (
si FixedString(32) , 
event_time DateTime ,
pv UInt32 ,
uv UInt32 ,
ip_counts UInt32 ,
devices UInt32,
vv UInt32,
avg_visit_times UInt64 
) ENGINE = ReplicatedMergeTree()
PARTITION BY toYear(event_time)
ORDER BY (si,event_time);
 
create table IF NOT EXISTS test_logs.test_record ON CLUSTER user_logs (
si FixedString(32) , 
event_time DateTime ,
pv UInt32 ,
uv UInt32 ,
vv UInt32,
devices UInt32,
ip_counts UInt32 ,
avg_visit_times UInt64
) ENGINE =  Distributed(user_logs, test_logs,test_logs.test_basics, rand());

/*add  xxx_field*/

ALTER TABLE test_logs.test_record ON CLUSTER user_logs  ADD Column IF NOT EXISTS xxx_field UInt64;

```

## 函数

### JSON函数

> `visitParamExtractString` 获取`json`字符串中某个`key`的`value`值

```sql
select visitParamExtractString('{"name":"zhangsan"}', 'name')

-- results
visitParamExtractString('{"name":"zhangsan"}', 'name')
zhangsan
```

### 最小值、最大值

```sql
arrayReduce('min',groupArray(event_time))
arrayReduce('max',groupArray(event_time))
```

### 日期(DateTime64)

```sql
-- 格式化为年月日的数值，类型为UInt32，也就是int类型
toYYYYMMDD(now())

-- 年与日时分秒toYYYYMMDDhhmmss(),类型为UInt64,long类型
toYYYYMMDDhhmmss(now())

-- 格式化为年月日小时
formatDateTime(create_time,'%Y-%m-%d %H:00:00')

-- 年月日
toDate(create_time)

--参考：https://blog.csdn.net/anyitian/article/details/116664687
-- 统计日期相差天数（year,month,week,day,hour,minute,second）
select dateDiff('day',  toDateTime(min(event_time)), now()) from tableName where toYear(event_time) >= 2021;
```

### ip函数(IPv4)

```sql
select * from table where IPv4NumToString(ip) like '10%';
```

### 去除空格

```sql
trimBoth(JSONExtractString(json,'mc_package','topic_id')) != ''
```

# 实战

## 1.数据补全

> 补全数据类型必须为`日期`或`数值`

```sql
-- 补全年月日
select
	toDate(event_time) as day,
	count(1) as count
from
	demo where toDate(event_time) between '2021-07-01' and '2021-07-20'
group by day order by day asc
with fill
from
	toDate('2021-07-01') to toDate('2021-07-20') STEP dateDiff('day',
    now(),
    now() + INTERVAL 1 day)
    
-- 补全数值
select
	toUInt16(formatDateTime(event_time, '%Y')) as day,
	count(1) as count
from
	demo where toDate(event_time) between '2021-06-01' and '2021-12-20'
group by day order by day asc
with fill
from
	1998 to 2021 STEP 1
```

