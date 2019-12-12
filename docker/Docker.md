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
service docker start
service docker stop
```

