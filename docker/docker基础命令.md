# docker基础命令

### 启动docker

```shell
systemctl start docker
systemctl restart docker
systemctl stop docker
systemctl enable docker
systemctl disable docker
```

## 1.镜像

### 查看镜像

```shell
docker images
```

### 搜索镜像

* [国外官网](hub.docker.com)

* [DaoCloud](https://hub.daocloud.io/)

```shell
docker search nginx
```

### 拉取镜像

```shell
docker pull redis
```

### 构建镜像

```shell
docker build -t imageName .
```

### 删除镜像

```shell
docker rmi containerID/containerName

# 强制删除
docker rmi -f containerID/containerName

# 清理临时镜像
docker rmi $(docker images -q -f dangling=true)
```

## 2.容器

### 列出容器

```shell
# 查看运行中的容器
docker ps

# 查看所有容器【包括停止的】
docker ps -a
```



### 启动容器

```shell
docker run -itd --name test -p 8086:8086 -v /usr/path:/usr/local/path test
docker start containerID/containerName
```

### 进入容器

```shell
docker exec -it containerID/containerName /bin/bash
docker exec -it containerID/containerName bash
```

### 关闭容器

```shell
docker stop containerID/containerName
```

### 重启容器

```shell
docker restart containerID/containerName
```

### 容器重命名

```shell
# oldName = containerID/containerName
docker rename oldName newName
```

### 删除容器

```shell
docker rm containerID/containerName

# 删除后台停止的容器
docker rm $(docker ps -a -q)
```

### 查看容器端口

```shell
docker port containerID/containerName
```

### 清理掉终止的容器

```shell
docker container prune
```

### 查看日志

```shell
docker logs containerID/containerName
```

### 停止所有容器

```shell
docker kill $(docker ps -q)
```

### 容器文件拷贝到外面

```shell
docker cp 容器id:/usr/local/sql /tmp/
```

### 文件拷贝到容器中

```shell
docker cp /tmp/db.sql 容器id:/opt/db.sql
```

### 查看容器详情

```shell
docker inspect containerId
```

### 查看最后一次创建的容器

```shell
docker ps -l
```

### 类似top命令

```shell
docker stats

#查看指定容器动态内存
dokcer stats kafka

# url导入
docker import http://example.com/exampleimage.tgz example/imagerepo
```

### 导出导入

```shell
# 导出
docker export 1e560fca3906 > nginx.tar

# 导入
cat docker/nginx.tar | docker import - nginx:v1
docker image import jenkins.tar fy:jenkins

# 导出容器
docker export containerId -o d.tar

# 导出镜像
docker image save container -o a.tar
```

# 查看容器环境变量

```shell
docker exec 容器id env
```

### 更新容器

```shell
# 开机自启
docker update --restart=always containerId

# 关闭开机自启
docker update --restart=no containerId
```

## 3.运行

> 常用参数

* -itd
* -p
* --name
* -e
* -v

```shell
docker run -p 80:80 -v /data:/data -d nginx:latest

# 参数说明
-a stdin: 指定标准输入输出内容类型，可选 STDIN/STDOUT/STDERR 三项；
-d: 后台运行容器，并返回容器ID；
-i: 以交互模式运行容器，通常与 -t 同时使用；
-P: 随机端口映射，容器内部端口随机映射到主机的端口
-p: 指定端口映射，格式为：主机(宿主)端口:容器端口
-t: 为容器重新分配一个伪输入终端，通常与 -i 同时使用；
--name="nginx-lb": 为容器指定一个名称；
--dns 8.8.8.8: 指定容器使用的DNS服务器，默认和宿主一致；
--dns-search example.com: 指定容器DNS搜索域名，默认和宿主一致；
-h "mars": 指定容器的hostname；
-e username="ritchie": 设置环境变量；
--env-file=[]: 从指定文件读入环境变量；
--cpuset="0-2" or --cpuset="0,1,2": 绑定容器到指定CPU运行；
-m :设置容器使用内存最大值；
--net="bridge": 指定容器的网络连接类型，支持 bridge/host/none/container: 四种类型；
--link=[]: 添加链接到另一个容器；
--expose=[]: 开放一个端口或一组端口；
--volume , -v: 绑定一个卷
```

## 4.网络

* host模式：使用 `--net=host` 指定，直接使用宿主主机的ip作为容器id。
* none模式：使用 -`-net=none` 指定。
* bridge模式：使用 `--net=bridge` 指定，默认设置。
