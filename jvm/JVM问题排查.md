## JVM问题排查

[参考地址](https://www.cnblogs.com/looyee/articles/11101606.html)

### jps+jstack+git bash+ProcessExplorer

[下载ProcessExplorer](https://learn.microsoft.com/en-us/sysinternals/downloads/process-explorer#download)



1. 打开`ProcessExplorer`，搜索`java`，找到CPU占用较高的进程

2. 右键》`Properties`》`Threads`》找到CPU占用较高的线程，拿到他的`TID`

3. 打开`git bash`》执行`printf '%x\n' tid`拿到项目对应线程id对应的十六进制的值

   ```sh
   # 例如tid为：19416，通过printf转换为16进制
   $ printf '%x\n' 19416
   4bd8
   ```

4. 在`git bash`中执行`jps -l`获取java程序的pid

   ```sh
   # 通过jps -l获取到了程序的进程号为20540
   $ jps -l
   20540 a
   ```

5. 查找CPU占用高的线程对应的堆栈信息

   ```sh
   # jstack pid | findstr 前面通过printf拿到进程的16进制值
   jstack 19416 | findstr 4bd8
   
   # linux或者windows在git bash中执行如下命令获取到堆栈信息
   jstack pid | grep 16进制值
   ```

   
