# 阿里巴巴的Arthas（阿尔萨斯）Java诊断利器

[官方文档](https://arthas.aliyun.com/doc)

* 启动自己的项目后，启动Arthas

```
https://alibaba.github.io/arthas/arthas-boot.jar
java -jar arthas-boot.jar

# 根据展示选择诊断的类，如下这里选择的DemoApplication
[INFO] arthas-boot version: 3.4.5
[INFO] Process 23932 already using port 3658
[INFO] Process 23932 already using port 8563
[INFO] Found existing java process, please choose one and input the serial number of the process, eg : 1. Then hit ENTER.
* [1]: 23932 com.example.demo.DemoApplication
  [2]: 19268 org.jetbrains.idea.maven.server.RemoteMavenServer36
  [3]: 14328
  [4]: 28584 org.jetbrains.jps.cmdline.Launcher
  [5]: 33528 org.jetbrains.jps.cmdline.Launcher
1
```

* 查找Controller类

```bash
sc *Controller
```

* 追踪

```bash
trace com.example.demo.DemoApplication hello
```

* 重置

```java
reset com.example.demo.DemoApplication
```

