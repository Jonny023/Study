# 卸载docker

```sh
# 停止所有运行的容器
sudo docker stop $(docker ps -aq)

# 删除所有容器
sudo docker rm $(docker ps -aq)

# 删除所有镜像
sudo docker rmi $(docker images -q)

# 卸载Docker引擎
sudo yum remove docker \
docker-client \
docker-client-latest \
docker-common \
docker-latest \
docker-latest-logrotate \
docker-logrotate \
docker-engine

# 删除 Docker 数据目录
sudo rm -rf /var/lib/docker

# 查看是否有漏掉的docker依赖
yum list installed | grep docker
# 删除依赖
yum remove docker-buildx-plugin.x86_64 docker-ce.x86_64 docker-ce-cli.x86_64 docker-ce-cli.x86_64 docker-ce-rootless-extras.x86_64 docker-compose-plugin.x86_64
```



# 卸载containerd

一、创建脚本

```sh
cat > remove-containerd.sh <<EOF
#!/bin/bash
# 删除contained命令及配置
rm -rf /etc/containerd/
# 删除containerd服务
rm -rf /usr/local/lib/systemd/system/containerd.service
# 删除runc
rm -rf /usr/local/sbin/runc
# 删除CNI插件find
rm -rf /opt/containerd/
# 删除ctr命令
rm -rf /usr/bin/ctr
EOF
```

二、赋权限及执行脚本

```sh
chmod +x remove-containerd.sh
./remove-containerd.sh

yum remove containerd
yum remove containerd.io
```