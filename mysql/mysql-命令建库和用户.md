> 命令创建用户及数据库

```sql
-- 创建数据库设置编码utf8mb4
CREATE DATABASE `database` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户
create user 'user'@'%' IDENTIFIED BY 'password';

-- 指定数据库访问权限
grant all privileges on database.* to user;

-- 刷新系统权限表
flush privileges;

--远程访问
mysql -h ip -u database -ppassword
```
