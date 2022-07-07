## nacos windows

[码云下载](https://gitee.com/mirrors/Nacos/releases)



```sh
# 下载zip

# 进入根目录
mvn -Prelease-nacos -Dmaven.test.skip=true clean install -U

# 进入脚本目录
cd distribution/target/nacos-server-2.1.0/nacos/bin

# 官网
https://nacos.io/zh-cn/docs/quick-start.html

# 启动
startup.cmd -m standalone


# 也可以在桌面创建bat脚本，如nacos-server.cmd或者nacos-server.bat
cd distribution/target/nacos-server-2.1.0/nacos/bin
startup.cmd -m standalone
```

[访问地址](http://localhost:8848/nacos)

> 用户名和密码都为nacos