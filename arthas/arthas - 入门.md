# arthas（阿尔萨斯）

[参考](https://blog.csdn.net/asia1752/article/details/119391408)

### [下载](https://arthas.aliyun.com/download/latest_version?mirror=aliyun)

```sh
# 命令行输入
curl -0 https://arthas.aliyun.com/arthas-boot.jar --output arthas-boot.jar

# 补充信息-------------------ubuntu 操作系统，在没有curl的情况，可执行命令：
apt-get update
apt install curl
```



### 用arthas-boot启动

在文件夹里有`arthas-boot.jar`，直接用`java -jar`的方式启动：

```sh
java -jar arthas-boot.jar

# 启动arthas   注意：启动前已经要有java进程运行，否则无法进入
java -jar arthas-boot.jar --telnet-port 9998 --http-port -9999
# 说明：建议增加参数 --telnet-port  和 --http-port   目的：避免端口冲突
```

打印帮助信息：

```sh
java -jar arthas-boot.jar -h
```

* web界面：http://localhost:3658/



### 查看dashboard

```sh
dashboard
```



### 查看线程

```sh
thread

Threads Total: 92, NEW: 0, RUNNABLE: 42, BLOCKED: 0, WAITING: 15, TIMED_WAITING
: 20, TERMINATED: 0, Internal threads: 15
ID NAME                GROUP     PRIORI STATE %CPU   DELTA TIME   INTER DAEMON
1  main                main      5      RUNNA 101.84 0.218 3:31.4 false false
2  Reference Handler   system    10     WAITI 0.0    0.000 0:0.01 false true
3  Finalizer           system    8      WAITI 0.0    0.000 0:0.01 false true
4  Signal Dispatcher   system    9      RUNNA 0.0    0.000 0:0.00 false true
5  Attach Listener     system    5      RUNNA 0.0    0.000 0:0.03 false true


# 查看main线程
thread pid
thread 1

[arthas@25500]$ thread 1
thread 1
"main" Id=1 RUNNABLE
    at java.io.FileOutputStream.writeBytes(Native Method)
    at java.io.FileOutputStream.write(FileOutputStream.java:326)
    at java.io.BufferedOutputStream.flushBuffer(BufferedOutputStream.java:82)
    at java.io.BufferedOutputStream.flush(BufferedOutputStream.java:140)
    at java.io.PrintStream.write(PrintStream.java:482)
    at sun.nio.cs.StreamEncoder.writeBytes(StreamEncoder.java:221)
    at sun.nio.cs.StreamEncoder.implFlushBuffer(StreamEncoder.java:291)
    at sun.nio.cs.StreamEncoder.flushBuffer(StreamEncoder.java:104)
    at java.io.OutputStreamWriter.flushBuffer(OutputStreamWriter.java:185)
    at java.io.PrintStream.write(PrintStream.java:527)
    at java.io.PrintStream.print(PrintStream.java:669)
    at java.io.PrintStream.println(PrintStream.java:806)
    at com.example.springbootdemo.SpringBootDemoApplication.run(SpringBootDemoApplication.java:18)
    at com.example.springbootdemo.SpringBootDemoApplication.main(SpringBootDemoApplication.java:12)
    
    
#参数说明
-n 指定最忙的前N个线程并打印堆栈
-b  找出当前阻塞其他线程的线程
-i  指定cpu占比统计的采样间隔，单位毫秒
--state WAITING  查看所有处于等待状态的线程
```



### 反编译

```java
jad com.example.springbootdemo.SpringBootDemoApplication

ClassLoader:
+-sun.misc.Launcher$AppClassLoader@18b4aac2
  +-sun.misc.Launcher$ExtClassLoader@667a738

Location:
/D:/project/spring-boot-demo/target/classes/

       /*
        * Decompiled with CFR.
        */
       package com.example.springbootdemo;

       import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
       import org.springframework.boot.SpringApplication;
       import org.springframework.boot.autoconfigure.SpringBootApplication;

       @SpringBootApplication(exclude={DruidDataSourceAutoConfigure.class})
       public class SpringBootDemoApplication {
           static int i = 0;

           public static void main(String[] args) {
/*11*/         SpringApplication.run(SpringBootDemoApplication.class, args);
/*12*/         SpringBootDemoApplication.run();
           }

           static void run() {
               while (true) {
/*18*/             System.out.println("hello------------" + i);
/*19*/             ++i;
               }
           }
       }

Affect(row-cnt:2) cost in 1100 ms.
```



### watch

> watch demo.MathGame 方法名  返回值

```sh
watch com.example.springbootdemo.SpringBootDemoApplication run primeFactors returnObj
```



### 查看被锁的进程

```sh
thread -b
```



### 占用cpu最多

> 查看占用cpu最多的10个进程

```sh
thread -n 10
```



### 火焰图

> 打印火焰图, 有一个默认转储的路径. windows不支持

```jvm
profiler start
profiler stop
```

### jvm

```sh
jvm
sysprop
sysenv
vmoption
```

