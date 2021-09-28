# docker基础命令

### 启动docker

```shell
systemctl start docker
systemctl restart docker
systemctl stop docker
systemctl enable docker
systemctl disable docker
```

### 查看镜像

```shell
docker images
```

### 拉取镜像

```shell
docker pull redis
```

### 构建镜像

```shell
docker build -t imageName .
```

### 启动容器

```shell
docker run -itd --name test -p 8086:8086 -v /usr/path:/usr/local/path test
docker start containerID
```

### 进入容器

```shell
docker exec -it containerID /bin/bash
```

### 关闭容器

```shell
docker stop containerID
```

### 重启容器

```shell
docker restart containerID
```

