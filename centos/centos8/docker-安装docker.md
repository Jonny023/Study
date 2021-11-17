# Centos8通过yum源安装docker无法使用docker命令

* 问题描述：

> 在Centos8系统中，使用docker run时，出现如下报错：
> `Emulate Docker CLI using podman. Create /etc/containers/nodocker to quiet msg.`
> `Error: open /proc/self/uid_map: no such file or directory`

* 解决办法：

1.卸载podman软件（可以使用`rpm -qa|grep docker`）

```shell
yum remove docker
```

2.下载docker-ce源

```shell
curl https://download.docker.com/linux/centos/docker-ce.repo -o /etc/yum.repos.d/docker-ce.repo
```

3.安装docker-ce

```shell
yum install docker-ce -y
```

### 问题原因分析：
Centos 8使用`yum install docker -y`时，默认安装的是`podman-docker`软件

