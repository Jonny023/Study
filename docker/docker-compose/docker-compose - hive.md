## 下载

* [github](https://github.com/big-data-europe/docker-hive)
* [参考1](https://zhuanlan.zhihu.com/p/414797007)
* [参考2](https://www.bilibili.com/read/cv7220228/)

```sh
git clone https://github.com/big-data-europe/docker-hive.git
```

## 运行

```sh
cd docker-hive

# 这步在后台起一个hive，元数据库用的是postgresql
# 会费一点时间，需要耐心等待
docker-compose up -d
```

## 连接

```sh
# 进入bash
docker-compose exec hive-server bash

# 使用beeline客户端连接
/opt/hive/bin/beeline -u jdbc:hive2://localhost:10000
```

## 命令

> 需要进入到项目目录docker-compose.yml位置执行命令

```sh
# 启动构建
docker-compose up

# 查看运行的容器
docker-compose ps

docker-compose logs [-f]

# 停止
docker-compose stop

# 再次启动
docker-compose start

# 重启
docker-compose restart

# 查看配置
docker-compose config
```

```sh
docker-compose down [options]
停止和删除容器、网络、卷、镜像。
选项包括：
–rmi type，删除镜像，类型必须是：all，删除compose文件中定义的所有镜像；local，删除镜像名为空的镜像
-v, –volumes，删除已经在compose文件中定义的和匿名的附在容器上的数据卷
–remove-orphans，删除服务中没有在compose中定义的容器
docker-compose down
停用移除所有容器以及网络相关
```

