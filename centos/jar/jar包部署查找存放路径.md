# 查看部署的jar项目

> 查看pid

```bash
[root@66olznfzmf280hmw /]# ps -ef|grep java
root     21278     1  0 Sep13 ?        01:00:06 java -jar chat.jar
root     24156 22077  0 16:33 pts/0    00:00:00 grep --color=auto java
```

> 查看jar存放路径

```bash
[root@66olznfzmf280hmw /]# ll /proc/21278 | grep cwd
lrwxrwxrwx  1 root root 0 Nov 29 16:25 cwd -> /usr/local/project
```
