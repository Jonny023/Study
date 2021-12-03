# mysql常见问题

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

