# JVM工具

[参考](https://blog.csdn.net/weixin_43849415/article/details/110631837)

```sh
# 查询段落
awk '/daas/' RS="\n\n" ORS="\n\n" gc.log

sed -n '/daas/,/^$/p' gc.log

-n ，禁止自动打印图案空间
-p ，打印当前模式空间
/daas/ ，以Pa0模式开头的段落
/^$/ ，以空行结尾的段落
^ ，开始行
$ ，行尾


# 查看当前用户
whoami

# 查看java进程号
jps -mlv
jcmd -l

# 查看堆
jmap -heap pid
```

### jmap

> docker 运行出现Operation not permitted

```
oot@aee59a674f20:~# jmap 1
Attaching to process ID 1, please wait...
ERROR: ptrace(PTRACE_ATTACH, ..) failed for 1: Operation not permitted
Error attaching to process: sun.jvm.hotspot.debugger.DebuggerException: Can't attach to the process: ptrace(PTRACE_ATTACH, ..) failed for 1: Operation not permitted
sun.jvm.hotspot.debugger.DebuggerException: sun.jvm.hotspot.debugger.DebuggerException: Can't attach to the process: ptrace(PTRACE_ATTACH, ..) failed for 1: Operation not permitted
	at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal$LinuxDebuggerLocalWorkerThread.execute(LinuxDebuggerLocal.java:163)
	at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal.attach(LinuxDebuggerLocal.java:278)
	at sun.jvm.hotspot.HotSpotAgent.attachDebugger(HotSpotAgent.java:671)
	at sun.jvm.hotspot.HotSpotAgent.setupDebuggerLinux(HotSpotAgent.java:611)
	at sun.jvm.hotspot.HotSpotAgent.setupDebugger(HotSpotAgent.java:337)
	at sun.jvm.hotspot.HotSpotAgent.go(HotSpotAgent.java:304)
	at sun.jvm.hotspot.HotSpotAgent.attach(HotSpotAgent.java:140)
	at sun.jvm.hotspot.tools.Tool.start(Tool.java:185)
	at sun.jvm.hotspot.tools.Tool.execute(Tool.java:118)
	at sun.jvm.hotspot.tools.PMap.main(PMap.java:72)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at sun.tools.jmap.JMap.runTool(JMap.java:201)
	at sun.tools.jmap.JMap.main(JMap.java:130)
Caused by: sun.jvm.hotspot.debugger.DebuggerException: Can't attach to the process: ptrace(PTRACE_ATTACH, ..) failed for 1: Operation not permitted
	at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal.attach0(Native Method)
	at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal.access$100(LinuxDebuggerLocal.java:62)
	at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal$1AttachTask.doit(LinuxDebuggerLocal.java:269)
	at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal$LinuxDebuggerLocalWorkerThread.run(LinuxDebuggerLocal.java:138)

```

#### 解决方法

> 命令添加--cap-add=SYS_PTRACE参数

* docker命令

```sh
docker run -itd -p 8080:8090 -p 9998:9998 --cap-add=SYS_PTRACE --name hello test
```

* docker-compose

```yaml
version: '2'

services:
 mysql:
 ...
 api:
 ...
 cap_add:
  - SYS_PTRACE
```

#### 效果

```sh
root@90d151316140:~# jmap -heap 1
Attaching to process ID 1, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.265-b01

using thread-local object allocation.
Mark Sweep Compact GC

Heap Configuration:
   MinHeapFreeRatio         = 40
   MaxHeapFreeRatio         = 70
   MaxHeapSize              = 482344960 (460.0MB)
   NewSize                  = 10485760 (10.0MB)
   MaxNewSize               = 160759808 (153.3125MB)
   OldSize                  = 20971520 (20.0MB)
   NewRatio                 = 2
   SurvivorRatio            = 8
   MetaspaceSize            = 21807104 (20.796875MB)
   CompressedClassSpaceSize = 1073741824 (1024.0MB)
   MaxMetaspaceSize         = 17592186044415 MB
   G1HeapRegionSize         = 0 (0.0MB)

Heap Usage:
New Generation (Eden + 1 Survivor Space):
   capacity = 9502720 (9.0625MB)
   used     = 4904472 (4.677268981933594MB)
   free     = 4598248 (4.385231018066406MB)
   51.61124393857759% used
Eden Space:
   capacity = 8454144 (8.0625MB)
   used     = 4904232 (4.677040100097656MB)
   free     = 3549912 (3.3854598999023438MB)
   58.00979969113372% used
From Space:
   capacity = 1048576 (1.0MB)
   used     = 240 (2.288818359375E-4MB)
   free     = 1048336 (0.9997711181640625MB)
   0.02288818359375% used
To Space:
   capacity = 1048576 (1.0MB)
   used     = 0 (0.0MB)
   free     = 1048576 (1.0MB)
   0.0% used
tenured generation:
   capacity = 20971520 (20.0MB)
   used     = 12167480 (11.603813171386719MB)
   free     = 8804040 (8.396186828613281MB)
   58.019065856933594% used

 12579 interned Strings occupying 1117136 bytes.
```

#### dump

```sh
jmap -dump:format=b,file=log.dat pid

# 这里的端口和上面docker run中的9998对应
# 执行可以通过ip:9998端口进行web访问
jhat -port 9998 /tmp/dump.dat
```



### jstat

> 打印gc

```sh
# 每250毫秒打印一次GC信息，共10次
jstat -gcutil -t pid 250ms 10
# 每2秒打印一次GC信息，共10次
jstat -gcutil -t pid 2s 10

root@90d151316140:~#  jstat -gcutil -t 1 250ms 10
Timestamp S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT   
200.2   0.01   0.00  24.01  58.02  92.88  90.21    138    0.787     1    0.075    0.862
200.6   0.01   0.00  34.05  58.02  92.88  90.21    138    0.787     1    0.075    0.862
200.9   0.01   0.00  42.04  58.02  92.88  90.21    138    0.787     1    0.075    0.862
201.1   0.01   0.00  60.03  58.02  92.88  90.21    138    0.787     1    0.075    0.862
201.4   0.01   0.00  76.01  58.02  92.88  90.21    138    0.787     1    0.075    0.862
201.6   0.01   0.00  88.01  58.02  92.88  90.21    138    0.787     1    0.075    0.862
201.9   0.00   0.01   4.00  58.02  92.89  90.21    139    0.790     1    0.075    0.865
202.2   0.00   0.01  22.01  58.02  92.89  90.21    139    0.790     1    0.075    0.865
202.4   0.00   0.01  34.00  58.02  92.89  90.21    139    0.790     1    0.075    0.865
202.7   0.00   0.01  52.04  58.02  92.89  90.21    139    0.790     1    0.075    0.865

root@5298b6dd7132:~# jstat -gc 1 250 4
 S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU      YGC     YGCT    FGC    FGCT     GCT   
1024.0 1024.0  0.1    0.0    8256.0   495.0    20480.0    11890.4   32640.0 30381.1 4224.0 3815.1    450    1.822   1      0.127    1.949
1024.0 1024.0  0.1    0.0    8256.0   1322.1   20480.0    11890.4   32640.0 30381.1 4224.0 3815.1    450    1.822   1      0.127    1.949
1024.0 1024.0  0.1    0.0    8256.0   2477.1   20480.0    11890.4   32640.0 30381.1 4224.0 3815.1    450    1.822   1      0.127    1.949
1024.0 1024.0  0.1    0.0    8256.0   3471.2   20480.0    11890.4   32640.0 30381.1 4224.0 3815.1    450    1.822   1      0.127    1.949
```

#### 说明

[参考](https://blog.csdn.net/u010648555/article/details/81089323)

```sh
堆内存 = 年轻代 + 年老代 + 永久代
年轻代 = Eden区 + 两个Survivor区（From和To）

S0C、S1C、S0U、S1U：Survivor 0/1区容量（Capacity）和使用量（Used）
EC、EU：Eden区容量和使用量
OC、OU：年老代容量和使用量
PC、PU：永久代容量和使用量
YGC、YGT：年轻代GC次数和GC耗时
FGC、FGCT：Full GC次数和Full GC耗时
GCT：GC总耗时

# gcutil
S0：幸存1区当前使用比例
S1：幸存2区当前使用比例
E：伊甸园区使用比例
O：老年代使用比例
M：元数据区使用比例
CCS：压缩使用比例
YGC：年轻代垃圾回收次数
FGC：老年代垃圾回收次数
FGCT：老年代垃圾回收消耗时间
GCT：垃圾回收消耗总时间

# gc
S0C：第一个幸存区的大小
S1C：第二个幸存区的大小
S0U：第一个幸存区的使用大小
S1U：第二个幸存区的使用大小
EC：伊甸园区的大小
EU：伊甸园区的使用大小
OC：老年代大小
OU：老年代使用大小
MC：方法区大小
MU：方法区使用大小
CCSC:压缩类空间大小
CCSU:压缩类空间使用大小
YGC：年轻代垃圾回收次数
YGCT：年轻代垃圾回收消耗时间
FGC：老年代垃圾回收次数
FGCT：老年代垃圾回收消耗时间
GCT：垃圾回收消耗总时间
```



### jstack

```sh
# 打印堆栈
jstack pid
jstack pid > log.txt

# 十进制转16进制
printf "%x\n" pid

jstack pid | grep 54ee

# 导出堆栈到文件
jstack pid > log.txt
```



### Java VisualVM远程

#### 参数

```
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote 
-Dcom.sun.management.jmxremote.port=9004 
-Dcom.sun.management.jmxremote.authenticate=false 
-Dcom.sun.management.jmxremote.ssl=false 
-Djava.net.preferlPv4Stack=true 
-Djava.rmi.server.hostname=192.168.5.106"
```

#### Dockerfile

> docker一般用的openjdk的java环境，openjdk:8-jdk-alpine里面的java工具不完整，导致有些java命令无法正常使用

```dockerfile
#FROM openjdk:8-jdk-alpine
FROM openjdk:8u265-jdk
WORKDIR /root
ADD spring-study-0.1.jar /root/app.jar
EXPOSE 8090
EXPOSE 9998
EXPOSE 9004
ENTRYPOINT ["java", "-jar", \
"-Djava.rmi.server.hostname=192.168.5.106", \
"-Dcom.sun.management.jmxremote.rmi.port=9004", \
"-Dcom.sun.management.jmxremote=true", \
"-Dcom.sun.management.jmxremote.port=9004", \
"-Dcom.sun.management.jmxremote.authenticate=false", \
"-Dcom.sun.management.jmxremote.ssl=false", \
"-Dcom.sun.management.jmxremote.local.only=false", \
"-Djava.security.egd=file:/dev/./urandom", \
"/root/app.jar"]
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]
```

* 通过JDK自带的`Java VisualVM`工具可以远程连接192.168.5.106:9004访问了
