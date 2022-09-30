## Redisson连接超时问题

> 2022-09-30 14:22:32:741 [ERROR] [nioEventLoopGroup-2-9] o.r.c.h.ErrorsLoggingHandler -Exception occured. Channel: [id: 0x65684c83, L:/192.168.0.174:56704 - R:r127.0.0.1:6379]
java.io.IOException: 远程主机强迫关闭了一个现有的连接。

* 解决方法，配置心跳检测时间为1s，也就是1000

```java
Config config = new Config();
config.useSingleServer()
		.setAddress(address)
		.setPingConnectionInterval(1000)
		.setPassword(password);
config.setThreads(thread);
//json格式序列化
config.setCodec(JsonJacksonCodec.INSTANCE);
config.setEventLoopGroup(new NioEventLoopGroup());
return Redisson.create(config);
```
