## Docker

* `Centos7`要求系统64位，内核3.1以上
* `Centos6.5`要求系统64位，内核版本2.6.32-431或者更高



### 查看内核版本

> uname -r

### 查看系统版本

> cat /etc/redhat-release

### 安装

```bash
yum install -y epel-release
yum install -y docker-io

# 查看版本
docker version
```

### docker配置文件

```bash
# centos6.5
/etc/sysconfig/docker

# centos7配置文件，没有则自己新建
/etc/docker/daemon.json
```

### 服务

```bash
# 开机自启
systemctl enable docker

# 启动
service docker start
systemctl start docker

# 停止
service docker stop
systemctl stop docker

# 状态
service docker status
systemctl status docker
```

### 镜像

```shell
# 查看所有镜像
docker ps -a

# 启动
docker start containerID

# 停止
docker stop containerID

# 查看日志、状态
docker logs containerID

# 开机启动
docker run --restart=always containerID

# 创建时未启动开机自启，通过更新
docker update --restart=always containerID
```

