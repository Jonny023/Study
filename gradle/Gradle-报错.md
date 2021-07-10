## 问题1

> 导入IDEA报错

```
Gradle sync failed: Another 'refresh project' task is currently running for the project
```

### 解决方法

```shell
File--Invalidate caches / Restart
```



## 问题2

```
Error:Unable to start the daemonprocess. 
This problem might be caused by incorrect configuration of the daemon. 
For example, an unrecognized jvm option is used. 
```

> 添加环境变量

```shell
_JAVA_OPTIONS: -Djava.net.preferIPv4Stack=true
```

