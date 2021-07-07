# 大文件切分

> 文件太大直接打开卡顿，通过`split`工具切分为多个小文件，这个工具是`git bash`自带的

```shell
# 将chproxy.out文件拆分，ck文件名前缀，每个文件-l 10000行，-a 5指定数字长度，-d指定文件后缀
split chproxy.out ck -l 10000 -a 5 -d

# 按文件大小切分
split chproxy.out ck -b 1m -a 5 -d
```

* -l 指定每个文件行数 

* -a 指定后缀长度
* -d 指定数字后缀
* -m 指定文件大小