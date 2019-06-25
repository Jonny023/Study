## 无法插入数据

* 修改mysql安装目录下的`my.ini`，若没有可以新建

> 保存提示`incorrect string value`

```
[mysqld]
character-set-server=utf8mb4

[mysql]
default-character-set=utf8mb4
```

* 数据库修改之前的表

```mysql
alter table teacher convert to character set utf8mb4 collate utf8mb4_bin;
```
