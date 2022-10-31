## k8s



[参考文档](https://blog.csdn.net/weixin_43757027/article/details/124132550)



> Kubernetes 1.24开始弃用Docker，替代品：cri-dockerd，Kubernetes 1.24以下的版本可以配合Docker使用，参考：https://www.cnblogs.com/wxwbblog/p/16601054.html

## 主机节点规划

| 主机        | 操作系统  | IP           | docker版本 | k8s版本 |
| ----------- | --------- | ------------ | ---------- | ------- |
| k8s-master1 | Centos7.9 | 192.168.70.4 | 20.10.18   | 1.25-0  |
| k8s-node1   | Centos7.9 | 192.168.70.5 | 20.10.18   | 1.25-0  |
| k8s-node2   | Centos7.9 | 192.168.70.6 | 20.10.18   | 1.25-0  |

## 步骤

1. 准备（所有节点）
2. 安装docker（所有节点）
3. 部署kubeadm,kubelet,kubectl服务（所有节点）
4. 初始化master节点（master节点）
5. node节点使用kubeadm join 加入集群(所有node节点）

## 1.准备

```sh
#设置主机名
hostnamectl set-hostname k8s-master1
hostnamectl set-hostname k8s-node1
hostnamectl set-hostname k8s-node2

#时间同步
yum install -y chrony
systemctl enable chronyd && systemctl restart chronyd
timedatectl set-ntp true


# 关闭防火墙
systemctl stop firewalld
systemctl disable firewalld
 
#禁用selinux,设置SELINUX=disabled
sed -i 's/enforcing/disabled/' /etc/selinux/config
setenforce 0


#禁用swap分区
swapoff -a
sed -ri 's/.*swap.*/#&/' /etc/fstab


# 确保 br_netfilter 模块被加载
lsmod | grep br_netfilter
# 若要显式加载该模块，可执行 
sudo modprobe br_netfilter
 
#允许iptables检查桥接流量
cat <<EOF | sudo tee /etc/modules-load.d/k8s.conf
br_netfilter
EOF
cat <<EOF | sudo tee /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF
sudo sysctl --system


#加载ipvs相关模块
cat > /etc/sysconfig/modules/ipvs.modules <<EOF
#!/bin/bash
modprobe -- ip_vs
modprobe -- ip_vs_rr
modprobe -- ip_vs_wrr
modprobe -- ip_vs_sh
modprobe -- nf_conntrack_ipv4
EOF

# vim /etc/sysconfig/kubelet
KUBELET_EXTRA_ARGS="--fail-swap-on=false"

#继续执行脚本
chmod 755 /etc/sysconfig/modules/ipvs.modules && bash /etc/sysconfig/modules/ipvs.modules && lsmod | grep -e ip_vs -e nf_conntrack_ipv4

#管理工具ipvsadm安装
yum install ipset ipvsadm -y
```

## 2.安装docker

```sh
# 1. 如果已经安装了docker,卸载旧版本(版本过低的情况下（k8s版本和docker版本有依赖关系)
yum remove docker \
docker-client \
docker-client-latest \
docker-common \
docker-latest \
docker-latest-logrotate \
docker-logrotate \
docker-engine

#2. 安装docker
# 提供yum-config-manager程序，device mapper 存储驱动程序需要 device-mapper-persistent-data 和 lvm2
sudo yum install -y yum-utils  device-mapper-persistent-data lvm2
 
# 3. 设置镜像仓库
# yum-config-manager会自动生成/etc/yum.repos.d下面的yum源文件
# 使用阿里云源
 
sudo yum-config-manager --add-repo \
     https://download.docker.com/linux/centos/docker-ce.repo
 
# 4. 查看可用版本
yum list docker-ce --showduplicates | sort -r
 
# 5. 安装最新版本，或者也可以安装指定版本
yum -y install docker-ce docker-ce-cli containerd.io
yum -y install docker-ce-<VERSION_STRING> docker-ce-cli-<VERSION_STRING> containerd.io
 
# 6. 设置docker开机自启动
systemctl start docker && systemctl enable docker
 
# 7. 检查docker是否正常运行
docker version 
 
# 8. 配置docker，使用 systemd 来管理容器的 cgroup 
sudo mkdir -p /etc/docker
cat <<EOF | sudo tee /etc/docker/daemon.json
{
  "exec-opts": ["native.cgroupdriver=systemd"],
  "log-driver": "json-file",
  "log-opts": {
    "max-size": "100m"
  },
  "registry-mirrors": [
    "https://5vfekjm3.mirror.aliyuncs.com",
    "http://docker-registry-mirror.kodekloud.com"
  ],
  "storage-driver": "overlay2"
}
EOF
 
# 9. 重启docker
sudo systemctl daemon-reload
sudo systemctl restart docker
```



## 3.部署kubeadm,kubelet,kubectl

```sh
# 由于官网中的地址不可访问，所以添加阿里源
cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=http://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=http://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg http://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
exclude=kube*
EOF
 
 
#安装 kubelet kubeadm kubectl 
#--disableexcludes=kubernetes  禁掉除了这个之外的别的仓库
# k8s 1.24+移除了docker:https://cloud.it168.com/a2022/0426/6661/000006661320.shtml
yum install -y kubelet-1.23.6 kubeadm-1.23.6 kubectl-1.23.6 --disableexcludes=kubernetes
systemctl enable kubelet && systemctl start kubelet
systemctl daemon-reload

# 卸载k8s
yum remove kubelet kubeadm kubectl
rm -rm /etc/kubernetes

# 查看状态，发现未启动成功是正常的
# master节点执行init操作成功或node节点加人集群后会启动
systemctl status kubelet

# 查看kubelet日志
systemctl status -l kubelet
journalctl -f -u kubelet.service


# 复制主节点配置到从节点
scp /etc/kubernetes/admin.conf root@k8s-node1:/etc/kubernetes/admin.conf
scp /etc/kubernetes/admin.conf root@k8s-node2:/etc/kubernetes/admin.conf

#具体根据情况，此处记录linux设置该环境变量
#方式一：编辑文件设置
vim /etc/profile
#在底部增加新的环境变量 export KUBECONFIG=/etc/kubernetes/admin.conf

#方式二:直接追加文件内容
echo "export KUBECONFIG=/etc/kubernetes/admin.conf" >> /etc/profile

#使profile生效
source /etc/profile
```

## 4.初始化master

### 4.1.创建kubeadm-init.yaml

```sh
# 生成kubeadm-init.yaml
kubeadm config print init-defaults > kubeadm-init.yaml

# 编辑kubeadm-init.yaml
vim kubeadm-init.yaml

# 将advertiseAddress: 1.2.3.4修改为本机IP地址
# 将imageRepository: k8s.gcr.io修改为imageRepository: registry.cn-hangzhou.aliyuncs.com/google_containers(阿里云的仓库)
#修改节点名称，如果不修改就是默认的’node’
#如果采用calico作为网络插件，在serviceSubnet: 10.96.0.0/12下面
#添加podSubnet: 192.168.0.0/16
```

### 4.2.完整的kubeadm-init.yaml

```yaml
apiVersion: kubeadm.k8s.io/v1beta3
bootstrapTokens:
- groups:
  - system:bootstrappers:kubeadm:default-node-token
  token: abcdef.0123456789abcdef
  ttl: 24h0m0s
  usages:
  - signing
  - authentication
kind: InitConfiguration
localAPIEndpoint:
  advertiseAddress: 192.168.70.4
  bindPort: 6443
nodeRegistration:
  criSocket: /var/run/dockershim.sock
  imagePullPolicy: IfNotPresent
  name: k8s-master1
  taints: null
---
apiServer:
  timeoutForControlPlane: 4m0s
apiVersion: kubeadm.k8s.io/v1beta3
certificatesDir: /etc/kubernetes/pki
clusterName: kubernetes
controllerManager: {}
dns: {}
etcd:
  local:
    dataDir: /var/lib/etcd
imageRepository: registry.cn-hangzhou.aliyuncs.com/google_containers
kind: ClusterConfiguration
kubernetesVersion: 1.23.0
networking:
  dnsDomain: cluster.local
  serviceSubnet: 10.96.0.0/12
  podSubnet: 192.168.0.0/16
scheduler: {}
```

### 4.3.执行init

```sh
kubeadm init --config kubeadm-init.yaml
kubeadm init --config kubeadm-init.yaml --ignore-preflight-errors=all --v=5
kubeadm init --config=kubeadm-init.yaml --ignore-preflight-errors=all --upload-certs --v=5

# 若报错可执行重置,并删除文件
kubeadm reset
rm -rf /etc/kubernetes

kubeadm init \
--apiserver-advertise-address=192.168.70.4  \
--image-repository registry.aliyuncs.com/google_containers \
--kubernetes-version v1.25.3 \
--service-cidr=10.96.0.0/12 \
--pod-network-cidr=10.244.0.0/16 \
--ignore-preflight-errors=all


#apiserver-advertise-address API 服务器所在的地址，这里就是master节点的IP
#image-repository 配置拉取k8s镜像的容器仓库，我们配置的是阿里的一个镜像仓库
#kubernetes-version 指定k8s的版本。
#service-cidr 服务的虚拟 IP 地址另外指定 IP 地址段
#pod-network-cidr pod节点网络可以使用的 IP 地址段
#service-cidr和pod-network-cidr的IP地址段设置是依据你service和pod的容量决定的。
#咱们可以通过IP计算机算出网段的容量：https://tool.520101.com/wangluo/ipjisuan/

#启动成功提示如下
[addons] Applied essential addon: kube-proxy

Your Kubernetes control-plane has initialized successfully!

To start using your cluster, you need to run the following as a regular user:

  mkdir -p $HOME/.kube
  sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
  sudo chown $(id -u):$(id -g) $HOME/.kube/config

Alternatively, if you are the root user, you can run:

  export KUBECONFIG=/etc/kubernetes/admin.conf

You should now deploy a pod network to the cluster.
Run "kubectl apply -f [podnetwork].yaml" with one of the options listed at:
  https://kubernetes.io/docs/concepts/cluster-administration/addons/

Then you can join any number of worker nodes by running the following on each as root:

kubeadm join 192.168.70.4:6443 --token abcdef.0123456789abcdef \
	--discovery-token-ca-cert-hash sha256:488a5f0a3e6206976e72801acd55f0334c753962addb7f6a07cfe2dfc0283270
```

## 4.4安装网络插件

### 4.4.1安装calico

```sh
curl https://docs.projectcalico.org/manifests/calico.yaml -O
kubectl apply -f calico.yaml
```

### 4.4.2kube-proxy开启ipvs

```sh
#修改ConfigMap的kube-system/kube-proxy中的config.conf，mode: “ipvs”：
kubectl edit cm kube-proxy -n kube-system

#之后重启各个节点上的kube-proxy pod：
kubectl get pod -n kube-system | grep kube-proxy | awk '{system("kubectl delete pod "$1" -n kube-system")}'

#查看日志,kube-proxy-xxx可以通过kubectl get pod -n kube-system获取
kubectl logs kube-proxy-xxx -n kube-system
#日志中打印出了Using ipvs Proxier，说明ipvs模式已经开启。
```



## 5.node节点加入集群(子节点)

* 在子节点服务器上执行kubeadm join 命令（master节点init操作生成的）

```sh
kubeadm join 192.168.70.4:6443 --token kg0p1z.utkape3zgdxmzeee --discovery-token-ca-cert-hash sha256:488a5f0a3e6206976e72801acd55f0334c753962addb7f6a07cfe2dfc0283270
```

* 如果忘记保存这串命令可以执行以下命令重新获取

```sh
kubeadm token create --print-join-command
```

* 删除节点

> 由于这个节点上运行着服务,直接删除掉节点会导致服务不可用.我们首先使用`kubectl drain`命令来驱逐这个节点上的所有pod

```sh
kubectl drain nodename --delete-local-data --force --ignore-daemonsets
kubectl delete node nodename
```

## 常用命令

```sh
# 查看节点状态
kubectl get node
kubectl get nodes

# 获取正在运行的pod
kubectl get pod -n kube-system

# 查看日志
kubectl logs {podName} -n kube-system

# 查看镜像
crictl images

# k8s查看镜像
crictl ps -a
ctr ps ls

# 存放路径
/etc/kubernetes

# 删除pod
kubectl delete service kubernetes-dashboard --namespace=kubernetes-dashboard
kubectl delete -f recommended.yaml
```

