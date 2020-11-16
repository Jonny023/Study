> 命令创建用户及数据库

```sql
-- 创建数据库设置编码utf8mb4
CREATE DATABASE `gd_dev` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户
create user 'gd_dev'@'%' IDENTIFIED BY 'gd_dev1116';

-- 指定数据库访问权限
grant all privileges on gd_dev.* to gd_dev;

-- 刷新系统权限表
flush privileges;
```
