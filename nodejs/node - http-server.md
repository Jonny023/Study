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
```

