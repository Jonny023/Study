# docker安装单机版clickhouse

## 1.下载镜像

```shell
docker pull yandex/clickhouse-server:21.12.3.32
```

## 2.启动容器

### 2.1 准备

```shell
# 创建配置目录
mkdir -p /opt/clickhouse/data
mkdir -p /opt/clickhouse/config
mkdir -p /opt/clickhouse/log

# 先启动一次
docker run -d --name ck-server -e TZ=Asia/Shanghai -p 8123:8123 -p 9001:9000 -p 9009:9009 yandex/clickhouse-server:21.12.3.32

# 停止容器
docker stop ck-server

# 把容器中的目录拷贝到宿主机，为了做数据和配置挂载
docker cp ck-server:/etc/clickhouse-server/config.xml /opt/clickhouse/config/config.xml
docker cp ck-server:/etc/clickhouse-server/users.xml /opt/clickhouse/config/users.xml

# 删除刚刚启动的容器
docker rm -f ck-server
```

### 2.2 远程访问

> 编辑配置`vim /opt/clickhouse/config/config.xml`
>
> 启用远程访问：网上说去掉`<listen_host>::</listen_host>`的注释，我测试时不行，可能版本差异，需要配置`<listen_host>0.0.0.0</listen_host>`才能启动，配置`127.0.0.1`也能启动
>
> 时区：`<timezone>Asia/Shanghai</timezone>`

```xml
<!-- 启用远程访问 -->
<listen_host>0.0.0.0</listen_host>
<timezone>Asia/Shanghai</timezone>
```

### 2.3 配置密码

#### 2.3.1 生成sha256密码

> 密码配置在`/opt/clickhouse/config/users.xml`中，配置的密码需要加密，这里用sha256

```shell
# 指定密码
PASSWORD=$(base64 < /dev/urandom | head -c8); echo "你的密码"; echo -n "这里替换为你的密码" | sha256sum | tr -d '-'

# 自动生成
# c8 指定密码长度为8为
PASSWORD=$(base64 < /dev/urandom | head -c8); echo "$PASSWORD"; echo -n "$PASSWORD" | sha256sum | tr -d '-'
```

#### 2.3.2 配置密码

> 将上面生成的密码到`/opt/clickhouse/config/users.xml`配置中

```xml
<!--<password></password>-->
<password_sha256_hex>8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92</password_sha256_hex>
```

### 2.4 重新启动

* --privileged=true 防止权限不足，因为容器中的root用户在宿主主机中只是一个普通用户，相当于以管理员身份运行
* -e TZ=Asia/Shanghai 指定时区，默认为UTC，少8小时，国内用的都是东八区`UTC+8`=`AsiaShanghai`
* --restart=always 开机自启

> 容器启动后想要设置开机自启：`docker update --restart=always 容器id/name `，关闭：将always改成no

```shell
docker run -itd --name ck-server \
--ulimit nofile=262144:262144 \
--privileged=true \
-e TZ=Asia/Shanghai \
-v /opt/clickhouse/data:/var/lib/clickhouse:rw \
-v /opt/clickhouse/config:/etc/clickhouse-server \
-v /opt/clickhouse/log:/var/log:rw \
-p 8123:8123 -p 9001:9000 -p 9009:9009 \
yandex/clickhouse-server:21.12.3.32
```

## 3.客户端

```shell
# 进入容器
docker exec -it ck-server bash

# 连接
# --user | -u 用户
# --host | -h 主机
# --password 密码
# --port 端口
# --database | -d 数据库
clickhouse-client
clickhouse-client --host 127.0.0.1 --password 123456

# 查看版本
select version();

# 查看库
show databases;
```

## 4.权限及访问控制

[参考地址](https://www.cnblogs.com/gentlescholar/p/15043217.html)

> clickhouse支持两种形式的权限管理，配置文件和sql语句（官方叫SQL-driven）

### 4.1 sql创建

#### 4.1.1 创建角色

```sql
CREATE ROLE [IF NOT EXISTS | OR REPLACE] name
    [SETTINGS variable [= value] [MIN [=] min_value] [MAX [=] max_value] [READONLY|WRITABLE] | PROFILE 'profile_name'] [,...]
```

#### 4.1.2 创建账号

```sql
CREATE USER [IF NOT EXISTS | OR REPLACE] name [ON CLUSTER cluster_name]
    [IDENTIFIED [WITH {NO_PASSWORD|PLAINTEXT_PASSWORD|SHA256_PASSWORD|SHA256_HASH|DOUBLE_SHA1_PASSWORD|DOUBLE_SHA1_HASH}] BY {'password'|'hash'}]
    [HOST {LOCAL | NAME 'name' | REGEXP 'name_regexp' | IP 'address' | LIKE 'pattern'} [,...] | ANY | NONE]
    [DEFAULT ROLE role [,...]]
    [SETTINGS variable [= value] [MIN [=] min_value] [MAX [=] max_value] [READONLY|WRITABLE] | PROFILE 'profile_name'] [,...]
```

#### 4.1.3 实际应用

```sql
-- 创建角色
CREATE ROLE IF NOT EXISTS test01_role;

-- 创建账号
CREATE USER test01 IDENTIFIED WITH sha256_password BY '123456';

-- 给角色test01_role授权test库select权限
GRANT SELECT ON test.* TO test01_role;

-- 给角色test01_role授权所有库增删改查权限
GRANT SELECT,UPDATE,INSERT,DELETE ON *.* TO test01_role; //给角色授权

-- 绑定用户角色
grant test01 to test01;
```



### 4.2 配置文件

> 在配置文件user.xml中配置default用户，标签users的子标签`default`即为`用户名`，可以配置多个
>
> clickhouse 20.4之后的版本开始支持基于`RBAC`的访问控制管理，添加users标签后无需重启，用户就能生效

* `default`为用户名，默认为default，可以自定义任意名字

* `password`是明文密码，还可以配置`password_sha256_hex`标签，通过sha256加密的密码

* `networks`是网络列表，只有主机或ip在网络列表中的才可以连接clickhouse

  * `host`指定主机，如：`<host>test01</host>`，可以用主机名或ip

  * `host_regexp`主机名正则，如：`^example\d\d-\d\d-\d\.host\.ru$`

  * `ip`IP地址或带掩码的IP地址，如：

    * `<ip>::/0</ip>`或`<ip>0.0.0.0</ip>`，任意ip都能连接，例：213.180.204.3, 10.0.0.1/8, 10.0.0.1/255.255.255.0, 2a02:6b8::3, 2a02:6b8::3/64, 2a02:6b8::3/ffff:ffff:ffff:ffff::

    * 仅限本地网络访问

      ```xml
      <!--经测试，如果配置了::1，客户端也能连接-->
      <!--<ip>::1</ip>-->
      <ip>127.0.0.1</ip>
      ```

  * `profile`clickhouse角色，在`users.xml`配置文件中的`profiles`标签下配置

* `quota`配额，分配用户资源，在`quotas`标签下配置

* `access_management`默认为0，设置为1标识开启RBAC权限控制

* `allow_databases`限定只能访问指定的数据库

```xml
<users>
    <default>
        <password>1234</password>
        <networks incl="networks" replace="replace">
            <host>ch01</host>
            <host>ch02</host>
        </networks>
        <profile>default</profile>
        <quota>default</quota>
        <access_management>1</access_management>
    </default>
    <root>
        <password>root</password>
        <networks incl="networks" replace="replace">
            <ip>::/0</ip>
        </networks>
        <profile>default</profile> 
        <quota>default</quota>
        <allow_databases>
            <database>test</database>
        </allow_databases>
        <!-- 如果需要通过sql的grant授权，必须设置为1 -->
        <access_management>1</access_management>
    </root>
</users>
```

**查询权限管理**

查询可以分为以下几种类型：

- 读：SELECT，SHOW，DESCRIBE，EXISTS
- 写：INSERT，OPTIMIZE
- DDL：CREATE，ALTER，RENAME，ATTACH，DETACH，DROP TRUNCATE
- 设置：SET，USE
- KILL

---



**`readonly`** ：读权限、写权限和设置权限，由此标签控制，它有三种取值：

- `0`不进行任何限制（默认值）；
- `1`只拥有读权限（只能执行SELECT、EXISTS、SHOW和DESCRIBE）；
- `2`拥有读权限和设置权限（在读权限基础上，增加了SET查询）。

当设置readonly=1后，用户将无法在当前会话中更改readonly和allow_ddl设置；也可以通过约束来限制更改权限。

**`allow_ddl`**：DDL权限由此标签控制，它有两种取值：

- 当取值为`0`时，不允许DDL查询；
- 当取值为`1`时，允许DDL查询（默认值）

如果当前会话的allow_ddl = 0，则无法执行SET allow_ddl = 1

**注意**：KILL QUERY可以在任何设置上执行，**readonly**和**allow_ddl**需要定义在用户**profiles**中。

```xml
    <profiles>   --在profiles里设置
        <normal> --只读，不能DDL
            <readonly>1</readonly>
            <allow_ddl>0</allow_ddl>
        </normal>

        <normal_1> --读且能set，不能DDL
            <readonly>2</readonly>
            <allow_ddl>0</allow_ddl>
        </normal_1>

        <normal_2> --只读，即使DDL允许
            <readonly>1</readonly>
            <allow_ddl>1</allow_ddl>
        </normal_2>

        <normal_3> --读写，能DDL
            <readonly>0</readonly>
            <allow_ddl>1</allow_ddl>
        </normal_3>
    </profiles>

    <users>
        <test>
            <password>123456</password>
            <networks incl="networks" replace="replace">
                <ip>::/0</ip>
            </networks>
            <profile>normal_3</profile> --用户引用相关profile
            <quota>default</quota>
        </test>
    </users>
```

说明：在`profiles`里设置相应的权限角色，再在users里引用，继承这些参数的限制

**还可以通过文件追加的方式加载配置**

> 在配置文件（`users.xml`）里添加`include_from`标签声明追加文件的存放位置

```xml
<include_from>/etc/clickhouse-server/metrika.xml</include_from>
```

> `networks`标签中的`incl`属性的值为`metrika.xml`中的`yandex`的子标签

```xml
# cat /etc/clickhouse-server/metrika.xml 
<yandex>
    <networks>
        <ip>192.168.163.133</ip>
    </networks>
    <networks_admin>
        <ip>192.168.163.132</ip>
    </networks_admin>
</yandex>


# cat /etc/clickhouse-server/users.xml
...
    <users>
        <default>
            <password></password>
            <networks incl="networks_admin" repace="replace" />
            <profile>default</profile>
            <quota>default</quota>
        </default>

        <web>
            <password></password>
            <networks incl="networks">
                <ip>192.168.163.131</ip>
            </networks>
            <profile>default</profile>
            <quota>default</quota>
        </web>
    </users>
...

最终运行起来的时候生效的结果如下：
# cat /var/lib/clickhouse/preprocessed_configs/users.xml 
...
    <users>
        <default>
            <password/>
            <networks repace="replace"> 
                <ip>192.168.163.132</ip>   ---已经replace继承过来了
            </networks>
            <profile>default</profile>
            <quota>default</quota>
        </default>

        <web>
            <password/>
            <networks>
                <ip>192.168.163.131</ip>
                <ip>192.168.163.133</ip>  ---已经默认追加过来了
            </networks>
            <profile>default</profile>
            <quota>default</quota>
        </web>
    </users>
...
```



## 问题

> 1.Configuration file '/etc/clickhouse-server/config.xml' isn't readable by user with id '101'

```shell
chown -R 101 /opt/clickhouse/data
chown -R 101 /opt/clickhouse/config
chown -R 101 /opt/clickhouse/log
```

