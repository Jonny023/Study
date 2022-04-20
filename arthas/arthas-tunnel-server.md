# Arthas-tunnel-server

> arthas-boot是客户端，只能和启用java的服务端部署到一起，而且只能单次启动运行
> arthas-tunnel-server是一个服务，类似注册中心，可以在任意地方启动，但是需要在项目中配置arthas相关信息，并且服务端和你的java项目要能够连通，通过socket远程访问


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

## 查看WAITING线程
```sh
thread --state WAITING
```

## 启用增强
```sh
options unsafe true
```

## 监听

```sh
stack java.util.concurrent.ThreadPoolExecutor <init>
watch com.alibaba.druid.pool.DruidDataSource init

# 导出dump文件
heapdump --live /root/jvm.hprof
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

