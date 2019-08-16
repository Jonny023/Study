# 安装

* 下载地址：[https://github.com/MicrosoftArchive/redis/releases](https://github.com/MicrosoftArchive/redis/releases)

* 配置环境，添加到`path`中

* 修改`redis`下面的`redis.windows-service.conf`文件，在`# requirepass foobared`下面添加
  * `requirepass 123456` 设置默认密码为`123456`
  
* 用`redis-server redis.windows-service.conf`这个配置启动
   * 安装为服务`redis-server --service-install redis.windows.conf`
   
* 启动服务

```
redis-server --service-start
```

* 停止服务

```
redis-server --service-stop
```

* 连接测试`redis`

```
# 连接reids
redis-cli

# 登录
auth 123456
```

* 查看所有数据

```
keys *
```

* 获取某一数据

```
get key
```

* 清空数据

```
flushall
```

* 查看版本

```
redis-cli
auth 123456
info

```

* 远程连接

```bash
redis-cli -h 192.168.1.145 -p 6379
```


