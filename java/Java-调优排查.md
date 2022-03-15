# java程序问题排查

```shell
# 查看占用高的进程
top
jps
jps -mlvV

# 输出堆栈到本地
jstack pid > a.log

# 查看占用高的线程
top -p pid -H

# 将pid转成16进制
printf %0x pid

# 查找对应16进制日志
grep "xxx" a.log
```

