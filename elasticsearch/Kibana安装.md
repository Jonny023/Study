# Kibana安装

[前往下载](https://www.elastic.co/cn/downloads/kibana)

[linux64版tar.gz](https://artifacts.elastic.co/downloads/kibana/kibana-7.14.0-linux-x86_64.tar.gz)

1.下载上传并解压

```shell
tar -xvf kibana-7.14.0-linux-x86_64.tar.gz
/usr/local/kibana-7.14.0-linux-x86_64
```

2.修改配置

```bash
vim config/kibana.yml

# 去掉注释部分
server.port: 5601
server.host: "0.0.0.0"
#elasticsearch.url: "http://localhost:9200"
elasticsearch.hosts: ["http://localhost:9200"]
kibana.index: ".kibana"
```

3.创建用户和组

```shell
groupadd elk
useradd elk -d /home/elk -s /bin/sh -g elk

# 权限
chown -R elk:elk /usr/local/kibana-7.14.0-linux-x86_64 && chmod -R 755 /usr/local/kibana-7.14.0-linux-x86_64
```

4.创建服务脚本

> vim /etc/systemd/system/kibana.service

```shell
[Unit]
Description=kibana service
Requires=network-online.target
After=network-online.target

[Service]
Type=simple
LimitNOFILE=65536
ExecStart=/usr/local/kibana-7.14.0-linux-x86_64/bin/kibana 
ExecStop=/usr/local/kibana-7.14.0-linux-x86_64/bin/kibana
User=elk
Group=elk
Restart=always
TimeoutSec=600

[Install]
WantedBy=multi-user.target
```

5.防火墙白名单

```shell
firewall-cmd --zone=public --add-port=5601/tcp --permanent
firewall-cmd --reload
```

6.启动服务

```shell
systemctl daemon-reload
systemctl start kibana.service
systemctl status kibana.service
systemctl stop kibana.service

# 是否开机自启
systemctl is-enabled kibana.service
```

7.测试

```shell
http://localhost:5601/
```

