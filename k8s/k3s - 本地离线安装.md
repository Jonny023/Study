# k3s



## 安装篇



[参考视频](https://www.bilibili.com/video/BV1k24y197KC/?p=6&spm_id_from=pageDriver&vd_source=610e097b4d28ca7a9353304c7307c4a9)

[参考文档1](https://www.yuque.com/wukong-zorrm/qdoy5p/lgspzc)

[参考文档2](https://blog.csdn.net/qq_30614345/article/details/131387040)

[常见问题](https://docs.rancher.cn/docs/k3s/faq/_index/)

[高可用参考](https://www.xiaowu95.wang/posts/37032a87/)

[k3s多网卡参考1](https://comphilip.wordpress.com/2020/01/22/k3s-%E5%9C%A8virtualbox%E6%90%AD%E5%BB%BAk3s%E9%9B%86%E7%BE%A4/)

[k3s多网卡参考2](https://www.rehiy.com/post/309/)



### 环境说明

* k3s版本：1.25.0
* containerd版本：1.6.28

| 主机名     | IP地址         | 说明     |
| ---------- | -------------- | -------- |
| k8s-master | 192.168.56.102 | 主节点   |
| k8s-node1  | 192.168.56.103 | 工作节点 |
| k8s-node2  | 192.168.56.104 | 工作节点 |

> 设置每台主机名

```sh
# 192.168.56.102
hostnamectl set-hostname k8s-master

# 192.168.56.103
hostnamectl set-hostname k8s-node1

# 192.168.56.104
hostnamectl set-hostname k8s-node2
```



> 每个节点都设置下hosts

```sh
# 设置主机地址
# vim /etc/hosts
192.168.56.102 k8s-master
192.168.56.103 k8s-node1
192.168.56.104 k8s-node2
```

> 设置完成后重启网卡

```sh
service network restart
```



### 1.准备

> 所有节点都需执行

* 关闭防火墙
* 设置setlinux

```sh
systemctl disable firewalld --now

yum install -y container-selinux selinux-policy-base
yum install -y https://rpm.rancher.io/k3s/latest/common/centos/7/noarch/k3s-selinux-0.2-1.el7_8.noarch.rpm
```



### 2.下载安装包

> 所有节点都需执行

* 下载安装脚本`install.sh`：https://get.k3s.io/

* 下载`k3s`二进制文件：[k3s](https://github.com/k3s-io/k3s/releases/download/v1.25.0%2Bk3s1/k3s)

* 下载必要的**image**：[离线安装需要的image文件](https://github.com/k3s-io/k3s/releases/download/v1.25.0%2Bk3s1/k3s-airgap-images-amd64.tar.gz)

这些文件可以在github仓库中获取：https://github.com/k3s-io/k3s

```sh
wget -O install.sh  https://get.k3s.io/
wget -O k3s https://github.com/k3s-io/k3s/releases/download/v1.25.0%2Bk3s1/k3s
wget -O k3s-airgap-images-amd64.tar.gz https://github.com/k3s-io/k3s/releases/download/v1.25.0%2Bk3s1/k3s-airgap-images-amd64.tar.gz

# 可以在master节点下载，然后在每个子节点执行复制
scp root@k8s-master:/root/{install.sh,k3s,k3s-airgap-images-amd64.tar.gz} .
```



### 3.执行安装脚本

1.将`k3s`二进制文件移动到`/usr/local/bin`目录，并添加执行权限

```sh
cp k3s /usr/local/bin && chmod +x /usr/local/bin/k3s
```

2.将镜像移动到`/var/lib/rancher/k3s/agent/images/`目录（无需解压）

```sh
mkdir -p /var/lib/rancher/k3s/agent/images/ &&
cp ./k3s-airgap-images-amd64.tar.gz /var/lib/rancher/k3s/agent/images/
```

#### master

- 在`k8s-master`节点执行：

```sh
#修改权限（所有节点都执行）
chmod +x install.sh
#离线安装,虚拟机多网卡NAT+host-only模式必须指定主机间能相互通信的IP地址 --cluster-init 集群模式
INSTALL_K3S_SKIP_DOWNLOAD=true ./install.sh --flannel-iface=enp0s8
# 或者
INSTALL_K3S_SKIP_DOWNLOAD=true ./install.sh --node-ip=192.168.56.102 

#安装完成后，查看节点状态
kubectl get node
#查看token
cat /var/lib/rancher/k3s/server/node-token
#14ec006827d0cd9d7f6fec2502723e00

# 查看集群信息
kubectl cluster-info
```

#### slave(工作节点)

* 在`k8s-node1`和`k8s-node2`节点执行

  > 默认端口6443，可通过/etc/systemd/system/k3s.service里面的ExecStart里面添加--apache-port=修改端口号

```sh
# --flannel-iface=enp0s8绑定指定网卡
INSTALL_K3S_SKIP_DOWNLOAD=true \
K3S_URL=https://k8s-master:6443 \
K3S_TOKEN=14ec006827d0cd9d7f6fec2502723e00 \
./install.sh \
--flannel-iface=enp0s8



# 或者
# worker1
INSTALL_K3S_SKIP_DOWNLOAD=true \
K3S_URL=https://k8s-master:6443 \
K3S_TOKEN=14ec006827d0cd9d7f6fec2502723e00 \
./install.sh \
--node-ip=192.168.56.103

# worker2
INSTALL_K3S_SKIP_DOWNLOAD=true \
K3S_URL=https://k8s-master:6443 \
K3S_TOKEN=14ec006827d0cd9d7f6fec2502723e00 \
./install.sh \
--node-ip=192.168.56.104


# 设置工作节点角色
kubectl label node k8s-node1 node-role.kubernetes.io/worker=true --overwrite
kubectl label node k8s-node2 node-role.kubernetes.io/worker=true --overwrite
```

* 查看节点状态

  > 若工作节点没加入成功，可尝试重启工作节点服务：systemctl restart k3s-agent

```sh
# 每隔1秒刷新一下节点状态（master执行）
watch -n 1 kubectl get node

kubectl top node
```

## kubectl 命令行补全

[参考地址](https://testerhome.com/topics/34980)

```sh
yum install bash-completion -y
source /usr/share/bash-completion/bash_completion
source <(kubectl completion bash)
echo "source <(kubectl completion bash)" >> ~/.bashrc
```

## 配置网络

> 跨服务商云主机集群通信，比如：阿里云和腾讯云的2台服务器作为master和worker节点部署时，需要这样配置

* [参考地址](https://kingsd.top/2020/05/14/k3s-Region/)

  ```sh
  kubectl annotate node k8s-node1 flannel.alpha.coreos.com/public-ip-overwrite=节点公网ip --overwrite
  ```

  

* 修改完后分别重启两次master和worker节点

  * ```sh
    # 重启master
    systemctl restart k3s
    
    # 重启worker
    systemctl restart k3s-agent
    ```
  
* 查看dst变成了配置的地址
  * bridge fdb show | grep flannel



## 配置篇

K3s 会自动生成containerd的配置文件**/var/lib/rancher/k3s/agent/etc/containerd/config.toml**,不要直接修改这个文件，k3s重启后修改会丢失。

为了简化配置，K3s 通过**/etc/rancher/k3s/registries.yaml**文件来配置镜像仓库，K3s会在启动时检查这个文件是否存在。

我们需要在每个节点上新建/etc/rancher/k3s/registries.yaml文件，配置内容如下：mkdir -p /etc/rancher/k3s & vim /etc/rancher/k3s/registries.yaml

```yaml
mirrors:
  docker.io:
    endpoint:
      - "https://fsp2sfpr.mirror.aliyuncs.com/"
```

重启每个节点

```sh
mkdir -p /etc/rancher/k3s/ && scp -r k8s-master:/etc/rancher/k3s /etc/rancher/

sed -i 's/127.0.0.1:6443/k8s-master:6443/' /etc/rancher/k3s/k3s.yaml

# master节点执行
systemctl restart k3s

# 工作节点执行
systemctl restart k3s-agent

# 查看服务
journalctl -u k3s -f

journalctl -u k3s-agent -f
```

查看配置是否生效。

```sh
cat /var/lib/rancher/k3s/agent/etc/containerd/config.toml
```



## 卸载篇

```sh
# 清理所有资源
kubectl delete all --all

# 查找k3s脚本所在位置
find / -name k3s*.sh

# master
# 关闭所有
sh /usr/local/bin/k3s-killall.sh
# 执行卸载
sh /usr/local/bin/k3s-uninstall.sh

# 工作节点
sh /usr/local/bin/k3s-killall.sh
sh /usr/local/bin/k3s-agent-uninstall.sh
```



## 工作节点

> 由于是集群模式，工作节点无法执行**kubelet**命令，需要从**master**节点拷贝配置

```sh
mkdir -p /etc/rancher/k3s/
# 在每个工作节点执行配置拷贝
scp root@k8s-master:/etc/rancher/k3s/k3s.yaml /etc/rancher/k3s/
```

> 并且需要修改配置中的**server**为**master**节点(这里我用主机名是因为我在`/etc/hosts`中配置了每个节点的主机名，如果没有配置则用**master**节点的IP地址即可)

```yaml
server: https://k8s-master:6443
```





## 采坑

### 问题1

> 原因分析：导致这个问题的原因是我多3台虚拟机都是NAT+host-only模式，NAT负责和公网通信，host-only负责局域网内部通信，创建集群的时候如果不通过--node-ip指定，它会默认用NAT的10.0.0.x网段作为它的INTERNAL-IP，由于多台虚拟机NAT网络是隔离的，相互之间无法通通信，且多台主机的NAT网卡对应的IP一模一样，导致工作节点无法加入到集群中，解决方法就是在每个节点都通过`--node-ip=多台主机局域网能相互通信的IP`即可

```sh
[root@k8s-master ~]# kubectl logs ng
Error from server: Get "https://10.0.2.15:10250/containerLogs/default/ng/ng": proxy error from 127.0.0.1:6443 while dialing 10.0.2.15:10250, code 503: 503 Service Unavailable
[root@k8s-master ~]# kubectl exec -it ng -- bash
Error from server: error dialing backend: proxy error from 127.0.0.1:6443 while dialing 10.0.2.15:10250, code 503: 503 Service Unavailable
```

### 问题2

> ClusterIP无法ping通？

- 在**kube-proxy**使用 **iptables** 的kubernetes集群中，ClusterIP是一个虚拟IP，不会响应ping，它仅作为iptables转发的目标规则。测试ClusterIP配置是否正确的最好方法是使用`curl`来访问 Pod IP 和端口以查看它是否响应。
- 在**kube-proxy**使用**ipvs**的kubernetes集群中，ClusterIP可以ping通。

### 问题3

> 测试的时候因为集群问题，可能需要反复安装卸载k3s，添加配置比较麻烦，可以将配置文件锁定，不允许修改

```sh
chattr +i /etc/rancher/k3s/k3s.yaml
```

