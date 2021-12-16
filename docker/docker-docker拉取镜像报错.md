# docker拉取镜像报错

> no space left on device 磁盘空间不足

```shell
# 查看docker根目录
docker info | grep -i "docker root dir"

# 查看剩余空间
df -hl /data/docker
```

## 解决方法

> 将`docker root dir`指向一个更大的空间的目录

```shell
# 假如这是一个更大空间的目录
mkdir /xxx/docker/data

# 查看服务
service docker status
systemctl status docker.service

# 停掉docker【停掉前需要将运行的容器停掉】
systemctl stop docker.service
service docker stop
```

* 修改路径

```shell
# 查看服务路径
service docker status
#Loaded: loaded (/usr/lib/systemd/system/docker.service; enabled; vendor preset: disabled)

# /usr/lib/systemd/system/docker.service为服务路径
# 需要在服务配置文件中添加参数--graph=/xxx/docker/data
[Service]
Type=notify
ExecStart=/usr/bin/dockerd --graph=/xxx/docker/data -H fd:// --containerd=/run/containerd/containerd.sock
ExecReload=/bin/kill -s HUP $MAINPID
TimeoutSec=0
RestartSec=2
Restart=always
```

* 重启docker

```shell
systemctl daemon-reload
systemctl restart docker
systemctl enable docker
```

