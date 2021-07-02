# Springboot项目部署

### 创建用户和组

```shell
useradd  demo
usermod -s /sbin/nologin demo
```

### 项目文件

```
mkdir -p /usr/local/project
```

### `run.sh`脚本

> 内存大小根据实际情况分配

```shell
#!/bin/sh
source /etc/profile
bin=`dirname "${BASH_SOURCE-$0}"`
bin=`cd "$bin"; pwd`
execJar=`find "$bin"  -name "*.jar"`
myjava=`which java`
$myjava -XX:+UseG1GC -Xmx2g -Xms2g  -jar $execJar
```

### 创建服务`demo.service`

```shell
[Unit]
Description=Demo Server
Requires=network-online.target
After=network-online.target

[Service]
Type=simple
User=demo
Group=demo
Restart=always
RestartSec=10
WorkingDirectory=/usr/local/project
ExecStart=/usr/local/project/run.sh
LimitCORE=infinity
LimitNOFILE=65536
LimitNPROC=65536
[Install]
WantedBy=multi-user.target
```

### 服务脚本拷贝

```shell
cp /usr/local/project/demo.service /etc/systemd/system
```

### 权限

```shell
chown -R demo:demo /usr/local/project  &&  chmod  -R 755 /usr/local/project
```

### 服务（开机自启、启动、停止、状态）

```shell
systemctl enable demo.service
systemctl start demo.service
systemctl stop demo.service
systemctl status demo.service
```

# 

# 问题！！！

### 启动服务报错`203`

> 分析：编码问题

```shell
vim /usr/local/project/run.sh
# 输入:set ff查看编码，如果是dos，这需要通过
:set ff=unix
# 设置为unix编码，也可以通过notepad++右下角的编码修改为unix
```

