# 通过dos命令结束进程

> 查看占用端口

```bash
netstat -ano|findstr 8100
```

> 查看指定端口的进程

```bash
tasklist | findstr 80
```

> 结束进程
* `taskkill -PID 进程号 -F `
* `-F 强制执行`

```bash
taskkill -PID 4680 -F
```

### 说明 

|协议|端口|网段| |PID|
|--|--|--|--|--|
|TCP  |  127.0.0.1:8100     |    0.0.0.0:0      |        LISTENING   |    4680|
