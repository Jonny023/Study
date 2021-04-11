# PostgreSQL安装篇

[下载地址](https://www.enterprisedb.com/download-postgresql-binaries)

##### 1、环境变量

> 在解压的根目录也就是`pgsql`目录下创建一个`data`目录，用来存放数据，并创建一个`bat`脚本

```bash
setx PGHOME "C:\dev_tools\pgsql"
setx PGHOST localhost
setx PGLIB %PGHOME%\lib
setx PGDATA "C:\dev_tools\pgsql\data"
setx Path %Path%;%PGHOME%\bin
```

##### 2、进入根目录`pgsql`，在`dos`环境下执行刚刚创建的脚本

```bash
start pg_start.bat
```

##### 3、初始化数据库，并设置超管密码(超级用户口令)

```bash
initdb.exe -D "C:\dev_tools\pgsql\data" -E UTF-8 --locale=chs -U postgres -W
```

##### 4、若需远程访问数据库，则修改配置

###### 4.1	编辑`data\postgresql.conf`，设置

```bash
listen_addresses = '*'
```

###### 4.2 编辑信任连接`data\pg_hba.conf`，设置`ipv4`

```bash
host all all 0.0.0.0/0 md5
```

##### 5、注册为服务

```bash
pg_ctl register -N PostgreSQL -D C:\dev_tools\PostgreSQL
```

##### 6、时区设置为：`Asia/Shanghai`

##### 7、启动服务

```bash
# 单次启动
pg_ctl start

# 服务启动
net start PostgreSQL
```

##### 8、连接数据库

> 默认用户名为：`postgres`

```bash
psql -U postgres
```

