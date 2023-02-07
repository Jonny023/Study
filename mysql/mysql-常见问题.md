# mysql常见问题

## mysql自增

[参考](https://segmentfault.com/a/1190000040256792)

* myisam自增id存到文件的，重启服务不会丢失，比如插入id为1-3的数据，删除id为3的记录，然后重启数据库服务
* innoDB
  * mysql5.7-自增值是存入内存中的，重启服务会获取max(id)+1作为自增值，重启插入数据id会从3开始
  * msyql8.0 会将其写入 redo log保存到引擎专用的系统表中。MySQL 正常关闭后重启：从系统表中获取计数器的值。MySQL 故障后重启：从系统表中获取计数器的值；从最后一个检查点开始扫描 redo log 中记录的计数器值；取这两者的最大值作为新值。

## 1.Bad handshake

### 1.1 报错

> 无法连接mysql，mysql错误日志如下

```shell
2021-12-03T06:01:51.478363Z 4310 [Note] Bad handshake
```

### 1.2 解决

> url链接添加配置

```shell
useSSL=false
```

### 1.3 查看数据库

```shell
show variables like '%ssl%';
```

### 1.4 参数解释

| 连接串                                      | 等价于                                      | 现象                     |
| ------------------------------------------- | ------------------------------------------- | ------------------------ |
| 不加useSSL                                  | `useSSL=true&verifyServerCertificate=false` | 有warnings，但可正常使用 |
| `useSSL=true`                               | `useSSL=true&verifyServerCertificate=true`  | 5.7.27正常，5.7.28异常   |
| `useSSL=false`                              | `useSSL=false&verifyServerCertificate=true` | 正常                     |
| `useSSL=true&verifyServerCertificate=false` |                                             | 正常                     |

### 1.5 修改配置

> 忽略ssl

```shell
[mysqld]
skip_ssl
```



* [参考地址](https://opensource.actionsky.com/20200514-mysql/)

