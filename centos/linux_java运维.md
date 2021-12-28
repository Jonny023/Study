```shell
ps -ef | grep java
top -H -p pid
# 查看端口
netstat - atulnp
lsof -i:port

# 查看cpu
lscpu

# 硬盘和分区
lsblk
```

```shell
jps -mlvV

jstack 2815

jstack -m 2815

# 查看启动参数
jinfo -flags 2815

# 查看堆
jmap -heap 2815

# dump
jmap -dump:live,format=b,file=/tmp/heap2.bin 2815
jmap -dump:format=b,file=/tmp/heap3.bin 2815

# 1000 每秒运行一次
jstat -gcutil 2815 1000

# 远程调试8000端口
jdb -attach 8000
```

