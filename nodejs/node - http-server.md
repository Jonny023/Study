## node环境变量

```properties
npm config set prefix "C:\Program Files\nodejs\node_global"
npm config set cache "C:\Program Files\nodejs\node_cache"
```

查找`.npmrc`文件

```
where .npmrc
```

环境变量

```properties
NODE_PATH C:\Program Files\nodejs\node_modules
Path C:\Program Files\nodejs\node_global
```

## http-server

> 实现和nginx一样的文件列表

### 安装

```sh
npm install http-server -g
```

### 运行

```sh
# 默认运行，端口8080
http-server .

# 指定端口运行
http-server c:\ -p 8088

# 实时更新文件
http-server -c-1   （只输入http-server的话，更新了代码后，页面不会同步更新）


npm install -g http-server
http-server -c-1 -p 80
-d dist 指定目录
-p 指定端口号


npm install -g history-server
history-server biz -p 80
biz 项目目录名称，指定后无需输入项目名访问
-p 指定端口
```

