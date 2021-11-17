# Docker安装

# 1.安装

```shell
wget -P /etc/yum.repos.d/ https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
yum install docker-ce-20.10.8-3.el7 -y
systemctl start docker
systemctl enable docker
```

## 2.配置加速镜像

```dockerfile
mkdir -p /etc/docker
tee /etc/docker/daemon.json <<-'EOF'
{
"exec-opts":["native.cgroupdriver=systemd"],
"insecure-registries": ["0.0.0.0/0"],
"registry-mirrors": ["https://zbkz1bx2.mirror.aliyuncs.com"]
}
EOF

# 重启服务
systemctl daemon-reload
systemctl restart docker
```

- exec-opts：修改docker Cgroup Driver 为`systemtd`，是k8s需要，默认是`cgroupfs`
- insecure-registries：支持http方式推送镜像
- registry-mirrors：添加镜像加速器，这里添加的是阿里云个人加速器地址，也可以添加其他镜像加速器，多个使用英文逗号分开即可
