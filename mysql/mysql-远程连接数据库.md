> mysql远程连接

```shell
mysql -h 10.82.133.161 -u root -pSxfrx++12345
```

> 权限

```shell
grant select,insert,update,delete on db.* to 'user'@'%' identified by 'password';
grant all on db.* to 'user'@'%' identified by 'password' with grant option;
flush privileges;
```

