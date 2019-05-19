# CentOS7 Oracle常用操作命令

> 监听

```bash
# 启动监听
lsnrctl start

# 停止监听
lsnrctl stop

# 查看监听状态
lsnrctl status
```

> 数据库连接

```bash
# 无需密码超级管理员连接
sqlplus /nolog
connect / as sysdba

sqlplus / as sysdba

# 指定用户连接
sqlplus scott/tiger@192.168.1.101/orcl

# 启动实例
SQL> startup

# 关闭实例
SQL> shutdown
```

> 查看操作

```bash
# 以管理员身份登录
conn scott/ as sysdba;

# 查看版本
select * from v$version;
```

