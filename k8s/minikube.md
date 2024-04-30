# minikube

[官方文档](https://minikube.sigs.k8s.io/docs/start/)

[参考文档](https://www.yuque.com/wukong-zorrm/qdoy5p/uur3eh)



## 安装minikube

* 下载并安装minikube

```sh
curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
sudo install minikube-linux-amd64 /usr/local/bin/minikube && rm minikube-linux-amd64

# 若无权限则需要授权
chmod +x /usr/local/bin/minikube
```

## 安装k8s

* 安装k8s

```sh
minikube start --image-mirror-country='cn' --container-runtime=containerd

# 直接执行会报错，不允许以root权限用户执行
X Exiting due to DRV_AS_ROOT: The "docker" driver should not be used with root privileges.

# 需要制定--driver=docker
minikube start --image-mirror-country='cn' --container-runtime=containerd --driver=docker

# 继续执行还是报错
X Exiting due to GUEST_MISSING_CONNTRACK: Sorry, Kubernetes 1.28.3 requires conntrack to be installed in root's path

# 安装依赖
yum install conntrack

# 还是报错
X Exiting due to DRV_AS_ROOT: The "docker" driver should not be used with root privileges.

# 创建docker用户
useradd -m docker
passwd docker

# 创建docker用户并指定分组为docker
useradd -m docker -g docker

# 修改密码后，加入用户组
gpasswd -a docker docker

# 打开 /etc/sudoers 文件，在 root ALL=(ALL:ALL) ALL 下 增加新的一行：
docker ALL=(ALL) ALL

# 强制保存:wq!

# 再次执行启动
minikube start --image-mirror-country='cn' --container-runtime=containerd --force --driver=docker
```

- - `--image-mirror-country='cn'`

设置使用国内阿里云镜像源

- - `--container-runtime=containerd`

* 如果要运行其他kubernetes版本，使用以下命令

```sh
minikube start --image-mirror-country='cn' --kubernetes-version=v1.23.0
```





* 停止、启动、挂起、删除minikube

```sh
minikube stop
minikube start
# 挂起虚拟机
minikube pause
minikube delete --all

# 修改虚拟机内存配置
minikube config set memory 16384

# 查看 minikube 的安装目录列表
minikube addons list
```

* 查看节点状态

```sh
minikube kubectl -- get pods -A

# 简写
minikube kubectl -- get po -A
```

* 每次都输入kubectl kubelctl --比较麻烦，可以配置

```sh
vi ~/.bashrc

#加入一行
alias kubectl="minikube kubectl --"

#退出
source ~/.bashrc
```

* 也可以进入容器

```sh
minikube ssh
```

### 删除指定资源

```sh
# 展示资源
kubectl get deployments -A

# 删除一个
kubectl delete deployment name

# 删除指定namespace下的资源
 kubectl delete deployment --all -n namespace-name

# 删除指定命名空间的资源
kubectl delete pods --all -n namespace-name

# 删除所有deployment
kubectl delete deployment -all
```



## 安装Dashboard

```sh
# 一次性运行，类似docker run 控制台ctrl+c就关闭了
minikube dashboard --url --port=80

# 安装后默认只能本机访问
kubectl proxy --port=65001 --address='192.168.56.102' --accept-hosts='^192.168.*'
```



## 其他可视化：Kuboard、KubeSphere