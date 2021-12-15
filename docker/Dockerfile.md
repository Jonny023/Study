# Dockerfile

## 1.运行Dockerfile

* `Dockerfile`文件

> `ADD`添加文件到镜像

```dockerfile
#FROM openjdk:8u265-jdk
FROM openjdk:8-jdk-alpine
WORKDIR /root
ADD test-1.0-SNAPSHOT.jar /root/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]
```

* 生成镜像

```shell
docker build -t test:v1 .
```

* 运行

| -i   | 交互模式运行容器                                             |
| ---- | ------------------------------------------------------------ |
| -t   | 为容器分配一个伪终端，运行后可通过`docker exec -it xxx bash`进入容器 |
| -d   | 后台运行容器，返回容器id                                     |

```shell
# 容器端口映射
docker run -itd -p 8085:8085 test:v1

# 直接使用宿主机端口
docker run -itd --network=host --name=test test:v1
docker run -itd --net=host --name=test test:v1
```

* 命令

```shell
# 停止容器
docker stop 容器id

# 启动容器
docker start 容器id

# 删除容器（删除前需要先停止容器）
docker rm 容器id

# 强制停止并删除容器
docker rm -f 容器id


#===========镜像===========
# 查看镜像
docker images

# 删除镜像（删除前需停止容器）
docker rmi 镜像id或镜像名称

# 进入容器
docker exec -it 容器id或名称 bash
docker exec -it 容器id或名称 /bin/bash
```

## 2.网络

* 查看网络

```shell
docker network ls
```

* 查看某个网络详情

```shell
docker network inspect 网络id
```

* 创建网络

> 重点：指定`--subnet`和`--gateway`

```shell
# 创建网络 --d == --driver 使用bridge驱动
docker network create --driver bridge --subnet 172.22.16.0/24 --gateway 172.22.16.1 my_net01

# 查看网桥信息
brctl show
# 测试centos7.9+
bridge link show

# 查看网络信息
ifconfig 网桥name

# 使用指定网络【host模式】
docker run -itd --net=my_net01 --name=test test:v1

# 端口映射【容器独立网络】
docker run -itd --net=my_net01 -p 80:8080 --name=test test:v1
```

* 查看容器

```shell
docker container inspect test:v1
# 等价于
docker inspect test:v1
```

* 指定网络

### 两种方式

```shell
# 1.容器创建时
docker run -itd --net=my_net01 --name=test test:v1

# 2.指定默认网络【绑定bridge网络】
docker network connect bridge test:v1

# 删除容器指定网络【删除bridge】
docker network disconnect bridge test:v1
```

## 3.日志

* 查看容器日志

```shell
docker logs 容器id
```

* 查看容器实时日志

```shell
docker logs -t -f --tail 100 容器id或name
docker logs -t -f --tail=100 容器id或name
docker logs -t -f -n=100 容器id或name
docker logs -t -f -n 100 容器id或name
```

