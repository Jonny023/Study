## Java问题排查

* cpu
* 磁盘
* 内存

```
jps -mlvV
```



### cpu

```shell
# 查看进程
top

# 查看占用率较高的pid
top -H -p pid

# 将占用高的pid转16进制得到nid
printf '%x\n' 23571

# jstack分析
jstack pid | grep 'nid' -C5 -color

# 生成日志
jstack 23571 > jstat.log

# waiting很多则有问题
cat jstack.log | grep "java.lang.Thread.State" | sort -nr | uniq -c

# 查看gc
jstat -gc pid
jstat –gcutil pid

# 查看gc,5s采样一次(单位:ms)
jstat -gc pid 5000

[root@vm01 test]# jstat -gc 23571 5000
S0C     S1C    S0U  S1U   EC   EU     OC      OU MC MU CCSC CCSU YGC  YGCT    FGC    FGCT     GCT 
1024.0 1024.0 773.2 0.0 8256.0 3162.7 20480.0 10898.6   31920.0 29512.0 4272.0 3783.3     34    0.100   1      0.029    0.129
# S0C/S1C、S0U/S1U、EC/EU、OC/OU、MC/MU分别代表两个Survivor区、Eden区、老年代、元数据区的容量和使用量
# YGC/YGT、FGC/FGCT、GCT代表YoungGc、FullGc的耗时和次数以及总耗时


# 上下文切换
# cs 上下文切换次数
vmstat pid

# 监控特定pid
# 安装 yum install -y sysstat
# 这个工具还包含iostat
# cswch(自愿) nvcswch(非自愿)
pidstat -w pid
```

### 磁盘

```shell
# 查看磁盘占用情况
df -hl

# 查看读写情况
iostat -d -k -x


# iotop
# 安装 yum install -y iotop
iotop

# 将看到的TID转为PID
readlink -f /proc/*/task/tid/../..

# 查看磁盘读写情况
cat /proc/pid/io

# 也可以通过lsof查看磁盘读写情况
# yum install -y lsof
lsof -p pid
```

### 内存

```shell
# 查看总内存
free -h

# top
top
```



### OOM和StackOverflow



#### OOM

* **`Exception in thread “main” java.lang.OutOfMemoryError: unable to create new native thread`**

   		没有足够的内存空间给线程分配java栈，基本上还是线程池代码写的有问题，比如说忘记shutdown，所以说应该首先从代码层面来寻找问题，使用`jstack`或者`jmap`。如果一切都正常，JVM方面可以通过指定Xss来减少单个thread stack的大小。另外也可以在系统层面，可以通过修改`/etc/security/limits.confnofile`和`nproc`来增大`os`对线程的限制。



* **`Exception in thread “main” java.lang.OutOfMemoryError: Java heap space`**

     	堆的内存占用已经达到`-Xmx`设置的最大值，应该是最常见的OOM错误了。解决思路仍然是先应该在代码中找，怀疑存在内存泄漏，通过`jstack`和`jmap`去定位问题。如果说一切都正常，才需要通过调整`Xmx`的值来扩大内存。



* **`Caused by: java.lang.OutOfMemoryError: Meta space`**

   		元数据区的内存占用已经达到`XX:MaxMetaspaceSize`设置的最大值，排查思路和上面的一致，参数方面可以通过`XX:MaxPermSize`来进行调整。



#### StackOverflow



* **`Exception in thread “main” java.lang.StackOverflowError`**

 表示线程栈需要的内存大于`Xss`值，同样也是先进行排查，参数方面通过Xss来调整，但调整的太大可能又会引起OOM。



* 常见原因：
  * 函数调用层次过深,每调用一次,函数的参数、局部变量等信息就压一次栈。（递归调用）
  * 局部静态变量体积太大

> 通常通过jmap导出dump文件

```shell
jmap -dump:format=b,file=filename pid
jmap -dump:format=b,file=test.hprof 23571

# 保存dump文件的vm参数，保存OOM的dump日志
-XX:+HeapDumpOnOutOfMemoryError

-XX:HeapDumpPath=/xxx/dump.hprof # dump fullGC相关的文件
```

#### pstree

> 安装：yum install -y psmisc，OOM排查工具

```shell
# 查看进程总线程数
pstree -p pid | wc -l

# 直接查看
ls -l /proc/pid/task | wc -l
```

```shell
# 倒序查看pid大内存前30段
pmap -x pid | sort -rn -k3 | head -30
```

