# grep

## 查询关键字符

* `-name` 文件名称
* `-n` 显示行号
* `-i、-y` 忽略大小写
* `-w` 单词匹配
* `-c` 匹配的行数
* `-v` 不匹配，取反

```shell
# 匹配指定文件后缀文件内容中的指定字符
find -name *.log | xargs grep '2021/12/27 11:20'

# 匹配指定字符，显示行号
grep -n '2021/12/27 11:20' *.log
2:2021/12/27 11:20 ERROR error log

# 匹配
[root@vm01 ~]# awk '/11:20/ {print}' a.log 
2021/12/27 11:20 ERROR error log

# 不匹配
[root@vm01 ~]# awk '!/11:20/ {print}' a.log 
2021/12/27 11:17 INFO xxxx

sudo -u admin find /home/admin /tmp /usr -name \*.log(多个目录去找)
find . -iname \*.txt(大小写都匹配)
find . -type d(当前目录下的所有子目录)
find /usr -type l(当前目录下所有的符号链接)
find /usr -type l -name "z*" -ls(符号链接的详细信息 eg:inode,目录)
find /home/admin -size +250000k(超过250000k的文件，当然+改成-就是小于了)
find /home/admin f -perm 777 -exec ls -l {} \; (按照权限查询文件)
find /home/admin -atime -1  1天内访问过的文件
find /home/admin -ctime -1  1天内状态改变过的文件    
find /home/admin -mtime -1  1天内修改过的文件
find /home/admin -amin -1  1分钟内访问过的文件
find /home/admin -cmin -1  1分钟内状态改变过的文件    
find /home/admin -mmin -1  1分钟内修改过的文件

```

