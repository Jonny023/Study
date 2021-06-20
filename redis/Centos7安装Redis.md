# Centos7安装Redis（单机版）

[官网](https://redis.io/download)

#### 1. 安装gcc

```bash
yum install -y gcc
```

#### 2. 下载解压并安装

```bash
wget https://download.redis.io/releases/redis-6.2.4.tar.gz
tar xzf redis-6.2.4.tar.gz
cd redis-6.2.4
make
make install PREFIX=/usr/local/redis
```

#### 3. 单次启动，ctrl+c终断

```shell
cd /usr/local/redis/bin/
./redis-server
```

#### 4. 修改配置

```
cp /usr/local/redis-6.2.4/redis.conf /usr/local/redis/bin/
```

>  修改 `redis.conf` 文件，把 `daemonize no` 改为 `daemonize yes`

```shell
vi redis.conf
```

#### 5. 后台运行

```bash
./redis-server redis.conf
```

##### 5.1 配置开机自启

```shell
vi /etc/systemd/system/redis.service
```

>  添加配置到文件

```shell
[Unit]
Description=redis-server
After=network.target

[Service]
Type=forking
ExecStart=/usr/local/redis/bin/redis-server /usr/local/redis/bin/redis.conf
PrivateTmp=true

[Install]
WantedBy=multi-user.target
```

##### 5.2 启动服务

```shell
systemctl daemon-reload
systemctl start redis.service
systemctl enable redis.service
```

##### 5.3 创建软链接

```shell
ln -s /usr/local/redis/bin/redis-cli /usr/bin/redis
```

#### 6. 测试 redis

```shell
[root@MiWiFi-R4A-srv ~]# redis
127.0.0.1:6379> keys *
1) "a"
127.0.0.1:6379> ping
PONG
127.0.0.1:6379>
```

#### 7. 服务操作命令

```shell
#启动redis服务
systemctl start redis.service

#停止redis服务
systemctl stop redis.service

#重新启动服务
systemctl restart redis.service

#查看服务当前状态
systemctl status redis.service

#设置开机自启动
systemctl enable redis.service

#停止开机自启动
systemctl disable redis.service
```

#### 8.开启远程连接（按需配置）

> 推荐：开发时可以启用远程连接，生产一般用内网地址

##### 8.1  编辑配置

> `/usr/local/redis/bin/redis.conf`

```shell
# 允许网段[允许任意客户端访问]
bind 0.0.0.0

# 默认端口
port 6379

#修改这个为yes,以守护进程的方式运行，就是关闭了远程连接窗口，redis依然运行
daemonize yes

#将protected-mode模式修改为no
protected-mode no

#设置需要密码
requirepass password
```

##### 8.2关闭服务

> 上面8.1配置修改后通过`systemctl restart redis.service`客户端还是无法访问，可以通过下面的配置先停止掉服务，再次调用`systemctl start redis.service`启动服务

```shell
# 由于前面配置了软链接可以是用命令
./redis -p 6379 shutdown

# 或者通过
./redis-cli -p 6379 shutdown
```

##### 8.3 客户端连接

```shell
C:\Users\admin>redis-cli -h 192.168.5.232
192.168.5.232:6379> auth 123456
OK
192.168.5.232:6379>
```

* 参数说明
  * `-h` 主机ip地址
  * `-p` 端口，默认为`6379`
  * `auth` 密码认证