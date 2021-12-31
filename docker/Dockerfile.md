# Dockerfile

* **ADD和COPY都只能操作同级目录或子目录下的文件或目录，无法操作上级及其他绝对路径的目录**，反例：`ADD /home/xxx.jar /home/xxx.jar`

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

> 使用不同网桥的容器是服务通信的

| 模式             | 优点                                                         | 缺点                                                         |
| ---------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| bridge(桥接模式) | 1.容器网络隔离                                               | 1.没有共有ip，和宿主机处于不同网段，安全性高<br>2.拥有独立的网络<br>3.网络传输效率低，因为需要主转发 |
| host             | 1.直接使用宿主机的ip地址和外界进行通信，无需NAT转换<br>2.    | 1.缺少网络隔离，安全信较低<br>2.没有独立的网络<br>3.由于没有独立的网络，容器会与宿主竞争网络栈的使用 |
| Container        | 1.多个容器共享同一个网络栈，容器间使用localhost高效快速通信【用于容器间的通信】<br>2.容器与宿主机及其他容器形成网络隔离<br>3.节约网络资源 | 1.没改善宿主机和容器间的通讯情况<br>2.和桥接一样，不能连接宿主机以外的其他设备 |
| none             | 1.无法进行容器间的通信                                       | 1.没有任何网络环境<br>2.一旦使用只能使用loopback回环地址     |



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
ip a show 网桥名称

# 查看网桥网络详情
docker network inspect my_net01

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

4.容器ip

```shell
# 获取指定容器的ip
docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' container_name_or_id

docker inspect 容器ID/name | grep IPAddress

# 获取所有容器的ip
docker inspect --format='{{.Name}} - {{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(docker ps -aq)


# 获取容器name
docker inspect -f='{{.Name}}' $(sudo docker ps -a -q)

# 获取容器ip/name/port
docker inspect -f='{{.Name}} {{.NetworkSettings.IPAddress}} {{.HostConfig.PortBindings}}' $(docker ps -aq)
```

