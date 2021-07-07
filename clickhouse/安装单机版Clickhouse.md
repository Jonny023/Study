# 安装单机版Clickhouse

[安装源](https://packagecloud.io/Altinity/clickhouse)

* 更新安装源，这个地址在安装源页面的`rpm`里面

```bash
curl -s https://packagecloud.io/install/repositories/Altinity/clickhouse/script.rpm.sh | sudo bash
```

* 安装服务端和客户端

```bash
yum install -y clickhouse-server clickhouse-client
```

* 启动服务

```bash
service clickhouse-server start

Start clickhouse-server service: Path to data directory in /etc/clickhouse-server/config.xml: /var/lib/clickhouse/
DONE
```

* 客户端连接测试

```bash
clickhouse-client

ClickHouse client version 20.8.3.18.
Connecting to localhost:9000 as user default.
Connected to ClickHouse server version 20.8.3 revision 54438.


# 查看数据库
show databases;
```

* 开启远程访问，去掉`<listen_host>::</listen_host>`的注释

```xml
# 编辑配置
vim /etc/clickhouse-server/config.xml

<listen_host>::</listen_host>

# 重启服务
service clickhouse-server restart
```

* 元数据存放路径`/var/lib/clickhouse/metadata`
* 默认用户名:`default`，密码为空，客户端连接端口：`8123`，服务端口：`9000`

* 设置密码`password`标签中输入密码

```xml
# 编辑配置
vim /etc/clickhouse-server/users.xml

# 设置为123456
<password>123456</password>
```

* 创建库，严格区分大小写

```sql
create database test_1;

# 指定引擎为Memory
create database test_1 engine = Memory

# Mysql引擎
crate database [IF NOT EXISTS] db_name [ON CLUSTER cluster] 
engine = MYSQL('host:port', ['database' | database], 'user', 'password')
```

* 创建表，用dbeaver连接的时候建表没有`ENGINE = Log`，导致无法建表

**表引擎在建表时必须指定，而数据库引擎在建库是不是必须指定**

```sql
CREATE TABLE `user` (
	uuid String,
	name String
) ENGINE = Log;

-- 修改数据
alter table sys_user update addr='香港', username='administrator' where id=1

--删除数据
alter table sys_user delete where id = 1;

--语法
ALTER TABLE [db.]table DELETE WHERE filter_expr;
ALTER TABLE [db.]table UPDATE column1 = expr1 [, ...] WHERE filter_expr;
```



```sql
-- 查看集群情况
select * from system.clusters;

-- 查看分区情况
select 
    partition,
    name,
    rows
from system.parts;

-- 查看当前所在数据库
select currentDatabase();
```



* 数据类型


| MySQL     | Hive      | CLickHouse（区分大小写）                  |
| --------- | --------- | ----------------------------------------- |
| byte      | TINYINT   | Int8                                      |
| short     | SMALLINT  | Int16                                     |
| int       | INT       | Int32                                     |
| long      | BIGINT    | Int64                                     |
| varchar   | STRING    | String                                    |
| timestamp | TIMESTAMP | DateTime                                  |
| float     | FLOAT     | Float32                                   |
| double    | DOUBLE    | Float64                                   |
| boolean   | BOOLEAN   | 无(可以用UInt8 类型，取值限制为 0 或 1。) |

* Log引擎

| 引擎      | 描述                                                         |
| --------- | ------------------------------------------------------------ |
| TinyLog   | 不能同时读写，没有索引（不支持insert into select table...），写是追加 |
| StripeLog | 有索引，并发读写                                             |
| Log       | 支持多线程，并发读写                                         |

* MergeTree引擎
  * 支持并发
  * 支持分区
  * 支持索引
  * 支持`mutation`(改、删操作)



# 权限

[参考](https://blog.csdn.net/vkingnew/article/details/107308936)

### 权限

```
readonly：读写设置权限均有此标签配置，有三种取值：
0：默认值，不进行任何取值
1：只拥有读权限（执行select，exists，show和describe权限）
2：拥有读写权限和设置权限
 
allow_ddl:DDL权限控制标签，有两种取值。
0：不允许DDL查询
1：默认值，允许DDL查询。
```

