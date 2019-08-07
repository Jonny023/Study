前往[官网](https://dev.mysql.com/downloads/installer/)下载zip包，解压到本地

1、配置环境变量

> `path`中加入`mysql`所在位置
```
path     F:\dev tools\mysql-5.7.21\bin
```
2、进入mysql解压文件目录创建my.ini文件

> `F:\dev tools\mysql-5.7.21`，在目录下`my.ini`（没有`ini`的文件自己创建）中写入,在当前目录下创建一个`data`文件夹，存放数据
```
[mysqld]
basedir=F:\dev tools\mysql-5.7.21
datadir=F:\dev tools\mysql-5.7.21\data 
port=3306
#不用密码登录
skip-grant-tables
# 允许最大连接数
max_connections=200
# 服务端使用的字符集默认为8比特编码的latin1字符集
character-set-server=utf8
# 创建新表时将使用的默认存储引擎
default-storage-engine=INNODB
sql_mode=NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES
```
3、以管理员身份进入mysql解压目录，通过mysqld -install安装服务，出现Service successfully installed.表示成功
```
F:\dev tools\mysql-5.7.21>cd bin

F:\dev tools\mysql-5.7.21\bin>mysqld -install
Service successfully installed.

//接下来执行mysqld  --initialize初始化数据库
mysqld  --initialize

//若出错可以卸载重新安装
mysqld remove
```
4、启动服务
```
net start mysql
```
5、输入`mysql -u root -p`，第一次没密码，直接回车
```sql
mysql -u root -p
```
6、修改`root`密码
```sql
//查看数据库
show databases;

//打开mysql库
use mysql;

//修改密码
update user set authentication_string = password("root") where user="root";
```
7、最后把mysql解压目录下my.ini里面的skip-grant-tables注释掉
```
#skip-grant-tables
```
8、重启`mysql`
```
net stop mysql
net start mysql
```
9、查看权限

```sql
show grants;
```

10、修改用户权限及密码

```sql
GRANT ALL ON *.* TO 'root'@'%' IDENTIFIED BY 'root';
FLUSH PRIVILEGES;
```

# 远程连接不上的原因
* 防火墙
* 权限不足

11、错误提示(提示必须重置密码才能连接)

```
You must reset your password using ALTER USER statement before executing this statement.
```

> 解决方法

```bash
SET PASSWORD = PASSWORD('root');
ALTER USER 'root'@'localhost' PASSWORD EXPIRE NEVER;
flush privileges;

// 或者
alter user user() identified by "123456";
flush privileges;
```
