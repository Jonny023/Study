# 下载地址

[前往下载](http://www.openoffice.org/zh-cn/download/)

* 安装步骤无限下一步

> 先看一下，服务有没有启动，若没启动
* 若有记录，代表服务已经启动

```bash
netstat -ano | findstr 8100
```

> 安装完成后，若服务未启动，打开`CMD`，进入到安装目录下

```bash
cd /d "c:\Program Files (x86)\OpenOffice 4\program"
```

> 关闭服务

```bash
# 查看pid，最后面的数字就是pid
# 如TCP	127.0.0.1:8100	0.0.0.0:0	LISTENING	4680，这里4680就是pid
netstat -ano | findstr 8100

# 强制结束
taskkill -pid 4680 -f
```

> 重启服务

```
# 进入程序目录
cd /d "c:\Program Files (x86)\OpenOffice 4\program"

# 启动服务
soffice -headless -accept="socket,host=127.0.0.1,port=8100;urp;" -nofirststartwizard  
```
