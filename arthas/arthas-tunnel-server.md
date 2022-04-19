# Arthas-tunnel-server

## 下载

```sh
wget https://arthas.aliyun.com/download/arthas-tunnel-server/latest_version?mirror=aliyun -O arthas-tunnel-server.jar

# 或者直接下载
https://github.com/alibaba/arthas/releases
https://arthas.aliyun.com/download/arthas-tunnel-server/latest_version?mirror=aliyun
```



## 启动

> arthas tunnel server的web端口是`8080`，arthas agent连接的端口是`7777`，通过Spring Boot的Endpoint，可以查看到具体的连接信息： http://127.0.0.1:8080/actuator/arthas ，登陆用户名是`arthas`，密码在arthas tunnel server的日志里可以找到

```sh
java -jar arthas-tunnel-server.jar

java -jar arthas-tunnel-server.jar --server.port=8888

# 查看端点
# 用户名：arthas, 密码：arthas tunnel server的日志里可以找到
http://127.0.0.1:8888/actuator/arthas

{
  "clientConnections": {},
  "version": "3.6.0",
  "properties": {
    "server": {
      "host": "0.0.0.0",
      "port": 7777,
      "ssl": false,
      "path": "/ws",
      "clientConnectHost": "172.18.0.1"
    },
    "embeddedRedis": null,
    "enableDetailPages": true,
    "enableIframeSupport": true
  },
  "agents": {}
}
```



## arthas

```sh
java -jar arthas-server.jar --tunnel-server 'ws://127.0.0.1:7777/ws'
```



# SpringBoot接入

## pom.xml

```xml
<dependency>
    <groupId>com.taobao.arthas</groupId>
    <artifactId>arthas-spring-boot-starter</artifactId>
    <version>3.6.0</version>
    <scope>runtime</scope>
</dependency>
```



## application.yml

```yaml
arthas:
  agent-id: app1
  app-name: app1
  tunnel-server: ws://192.168.56.101:7777/ws # arthas服务地址
```

