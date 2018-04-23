前往[官网](https://dev.mysql.com/downloads/installer/)下载zip包，解压到本地

1、配置环境变量

//path中加入mysql所在位置
```
path     F:\dev tools\mysql-5.7.21\bin
```
2、进入mysql解压文件目录创建my.ini文件
```
//F:\dev tools\mysql-5.7.21

//my.ini中写入,在当前目录下创建一个data文件夹，存放数据
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
5、输入mysql -u root -p，第一次没密码，直接回车
```
mysql -u root -p
```
6、修改root密码
```
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
8、重启mysql
```
net stop mysql
net start mysql
```
