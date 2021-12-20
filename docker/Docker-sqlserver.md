# docker sqlserver

* 拉取镜像

```shell
docker pull mcr.microsoft.com/mssql/server:2019-latest
```

* 创建数据持久化目录

```shell
mkdir sqlserver2019 && chmod 777 sqlserver2019
```

* 运行
  * ro - 只读，防止被容器修改

```shell
sudo docker run -itd -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=Daas123456' -p 1433:1433 \
--restart=always \
--name sqlserver2019 \
--user $(id -u) \
-e "TZ=Asia/Shanghai" \
-v /etc/localtime:/etc/localtime:ro \
-v /data/docker/volumes/data:/var/opt/mssql/data \
-v /data/docker/volumes/log:/var/opt/mssql/log \
-v /data/docker/volumes/secrets:/var/opt/mssql/secrets \
mcr.microsoft.com/mssql/server:2019-latest
```

* 报错

> 原因是权限不够，如果是登录的管理员，用`--user $(id -u)`以管理员身份运行

```
To learn more visit https://go.microsoft.com/fwlink/?linkid=2099216.
2021-12-20 01:49:38.95 Server      Setup step is copying system data file 'C:\templatedata\master.mdf' to '/var/opt/mssql/data/master.mdf'.
2021-12-20 01:49:39.02 Server      ERROR: Setup FAILED copying system data file 'C:\templatedata\master.mdf' to '/var/opt/mssql/data/master.mdf':  5(Access is denied.)
ERROR: BootstrapSystemDataDirectories() failure (HRESULT 0x80070005)
```

```shell
sudo chmod 777 -R VOLUME_DIRECTORY
```

* 进入容器

```
docker exec -it sqlserver2019 bash
```

* 环境变量

> 配置好环境变量任意地方都能用`sqlcmd`命令

```shell
echo 'export PATH="$PATH:/opt/mssql-tools/bin"' >> ~/.bashrc
source ~/.bashrc
```

* 连接

```sql
/opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P "Daas123456"
sqlcmd -U sa -P Daas123456

# 查看所有数据库
SELECT Name from sys.Databases
go

# 连接数据库
use test
go

# 查看所有表
select name from sys.tables
go

# 查看表结构
sp_help student
go

# 查看表所有列
sp_columns student
go

# 字符集
ALTER TABLE  表名   
ALTER COLUMN 字段   VARCHAR(100)  COLLATE Chinese_PRC_CI_AS
go

ALTER DATABASE testDB COLLATE Chinese_PRC_CI_AS;  
GO

# 创建数据库表时指定编码
CREATE SCHEMA dbo;
go

CREATE TABLE test.dbo.student (
	id int IDENTITY(0,1) NOT NULL,
	stu_name varchar(100) COLLATE Chinese_PRC_CI_AS NULL,
	age int NULL,
	grade varchar(100) COLLATE Chinese_PRC_CI_AS NULL,
	CONSTRAINT student_PK PRIMARY KEY (id)
);
go
```

* 默认排序规则：`SQL_Latin1_General_CP1_CI_AS`，插入中文会乱码。创建数据库时指定规则`Chinese_PRC_CI_AS`，支持中文，不乱码
  * `_BIN` 二进制排序
  * `CI`(`CS`) 是否区分大小写，`CI`不区分，`CS`区分
  * `_AI`(`AS`) 是否区分重音，`AI`不区分，`AS`区分
  * _`KI`(`KS`) 是否区分假名类型,`KI`不区分，`KS`区分_
  * `WI`(`WS`) 是否区分宽度`WI`不区分，`WS`区分

  ```basic
  区分大小写:如果想让比较将大写字母和小写字母视为不等，请选择该选项。
  区分重音:如果想让重音和非重音字母视为不等，请选择该选项。如果选择该选项，
  比较还将重音不同的字母视为不等。
  区分假名:如果想让比较将片假名和平假名日语音节视为不等，请选择该选项。
  区分宽度:如果想让比较将半角字符和全角字符视为不等，请选择该选项。
  ```

# 时区问题

> 如果容器已经启动，要解决时区问题

```shell
# 启动参数添加
-e "TZ=Asia/Shanghai"
```

