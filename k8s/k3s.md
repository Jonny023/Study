# k3s在线安装



## 安装篇



[参考视频](https://www.bilibili.com/video/BV1k24y197KC/?p=6&spm_id_from=pageDriver&vd_source=610e097b4d28ca7a9353304c7307c4a9)

[参考文档1](https://docs.rancher.cn/docs/k3s/quick-start/_index/)

[多主多从高可用](https://docs.k3s.io/zh/datastore/ha-embedded)

[端口说明](https://ranchermanager.docs.rancher.com/zh/getting-started/installation-and-upgrade/installation-requirements/port-requirements)

[参考](https://www.rehiy.com/post/547/)

### 环境说明

* virtualbox对应的虚拟机：网络模式：NAT+host-only双网卡
* CentOS 7.9

* k3s版本：1.26.5

| 主机名     | IP地址（host-only网卡对应的IP） | 说明     |
| ---------- | ------------------------------- | -------- |
| k8s-master | 192.168.56.102                  | 主节点   |
| k8s-node1  | 192.168.56.103                  | 工作节点 |
| k8s-node2  | 192.168.56.104                  | 工作节点 |

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
* 关闭交换分区

```sh
systemctl disable firewalld --now

yum install -y container-selinux selinux-policy-base
yum install -y https://rpm.rancher.io/k3s/latest/common/centos/7/noarch/k3s-selinux-0.2-1.el7_8.noarch.rpm

# 关闭setlinux
sed -i 's/enforcing/disabled/' /etc/selinux/config

# 永久关闭交换分区，注释掉与 swap 分区相关的行
vim /etc/fstab

reboot
```

> 添加防火墙规则（这步不用做）原本以为是端口转发规则有问题，结果跟这个无关

```sh
# 清空规则
iptables -F && iptables -t nat -F  && iptables -t mangle -F

sudo iptables -I INPUT 1 -p tcp --dport 6444 -j ACCEPT
sudo iptables -I OUTPUT 1 -p tcp --sport 6444 -j ACCEPT

sudo iptables -I INPUT 1 -p tcp --dport 6443 -j ACCEPT
sudo iptables -I OUTPUT 1 -p tcp --sport 6443 -j ACCEPT

# 查看
sudo iptables -L -n --line-numbers | grep :644*
```



### 2.安装

[参考地址](https://www.cnblogs.com/roy2220/p/14766334.html)

[参考](https://docs.rancher.cn/docs/k3s/faq/_index/)

#### master

- 在`k8s-master`节点执行：

```sh
# 若果是物理机可以直接用这个命令
curl -sfL https://rancher-mirror.rancher.cn/k3s/k3s-install.sh | INSTALL_K3S_VERSION=v1.26.5+k3s1 INSTALL_K3S_MIRROR=cn sh -

# 本文是通过虚拟机多网卡跑的k3s，需要通过--node-ip指定当前节点的ip，和物理机的host-only网卡绑定，不然工作节点无法和master进行通信，因为网络是隔离的（通过--node-ip指定INTERNAL-IP），多网卡通过--flannel-iface=enp0s8指定网卡
# 多个master节点需要指定--cluster-init 
curl -sfL https://rancher-mirror.rancher.cn/k3s/k3s-install.sh | INSTALL_K3S_VERSION=v1.26.5+k3s1 INSTALL_K3S_MIRROR=cn sh -s - server --flannel-iface=enp0s8


#安装完成后，查看节点状态
kubectl get node
#查看token
cat /var/lib/rancher/k3s/server/node-token
#K109e40238d2526c28a9fcf3da1f13aab359ec314ef31be13ade6abd6fcec99ab97::server:5ea4aa01e7b9fd3548ff9bcd2068c684

# 查看集群信息
kubectl cluster-info


# 多网卡多主多从（将k8s-node1和k8s-node2都加入为master）
# k8s-node1 可通过--node-ip=192.168.56.103绑定ip或者通过--flannel-iface=enp0s8绑定网卡
curl -sfL https://rancher-mirror.rancher.cn/k3s/k3s-install.sh | INSTALL_K3S_VERSION=v1.26.5+k3s1 INSTALL_K3S_MIRROR=cn K3S_URL=https://k8s-master:6443 K3S_TOKEN=5ea4aa01e7b9fd3548ff9bcd2068c684 sh -s - server --flannel-iface=enp0s8
# k8s-node2
curl -sfL https://rancher-mirror.rancher.cn/k3s/k3s-install.sh | INSTALL_K3S_VERSION=v1.26.5+k3s1 INSTALL_K3S_MIRROR=cn K3S_URL=https://k8s-master:6443 K3S_TOKEN=5ea4aa01e7b9fd3548ff9bcd2068c684 sh -s - server --flannel-iface=enp0s8
```

#### slave(工作节点)

* 在`k8s-node1`和`k8s-node2`节点执行

  > 默认端口6443，可通过/etc/systemd/system/k3s.service里面的ExecStart里面添加--apache-port=修改端口号

```sh
# 单网卡（工作节点都执行这条命令即可）
# k8s-master token可以用整个完整的token，也可以只复制最后一段
curl -sfL https://rancher-mirror.rancher.cn/k3s/k3s-install.sh | INSTALL_K3S_VERSION=v1.26.5+k3s1 INSTALL_K3S_MIRROR=cn K3S_URL=https://k8s-master:6443 K3S_TOKEN=5ea4aa01e7b9fd3548ff9bcd2068c684 sh -

# 多网卡，通过--flannel-iface=enp0s8指定网卡
curl -sfL https://rancher-mirror.rancher.cn/k3s/k3s-install.sh | INSTALL_K3S_VERSION=v1.26.5+k3s1 INSTALL_K3S_MIRROR=cn K3S_URL=https://k8s-master:6443 K3S_TOKEN=5ea4aa01e7b9fd3548ff9bcd2068c684 INSTALL_K3S_EXEC="--flannel-iface=enp0s8" sh -

curl -sfL https://rancher-mirror.rancher.cn/k3s/k3s-install.sh | INSTALL_K3S_VERSION=v1.26.5+k3s1 INSTALL_K3S_MIRROR=cn K3S_URL=https://k8s-master:6443 K3S_TOKEN=5ea4aa01e7b9fd3548ff9bcd2068c684 INSTALL_K3S_EXEC="--flannel-iface=enp0s8" sh -

# # 或者通过--node-ip指定INTERNAL-IP
curl -sfL https://rancher-mirror.rancher.cn/k3s/k3s-install.sh | INSTALL_K3S_VERSION=v1.26.5+k3s1 INSTALL_K3S_MIRROR=cn K3S_URL=https://k8s-master:6443 K3S_TOKEN=5ea4aa01e7b9fd3548ff9bcd2068c684 sh -s - agent --node-ip=192.168.56.103

curl -sfL https://rancher-mirror.rancher.cn/k3s/k3s-install.sh | INSTALL_K3S_VERSION=v1.26.5+k3s1 INSTALL_K3S_MIRROR=cn K3S_URL=https://k8s-master:6443 K3S_TOKEN=5ea4aa01e7b9fd3548ff9bcd2068c684 sh -s - agent --node-ip=192.168.56.104
```

* 查看节点状态

  > 若工作节点没加入成功，可尝试重启工作节点服务：systemctl restart k3s-agent

```sh
# 每隔1秒刷新一下节点状态（master执行）
watch -n 1 kubectl get node -owide

kubectl top node
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
# 关闭所有 升级时可能用到
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

> 原因分析：导致这个问题的原因是我多3台虚拟机都是NAT+host-only模式，NAT负责和公网通信，host-only负责局域网内部通信，创建集群的时候如果不通过--node-ip指定，它会默认用NAT的10.0.0.x网段作为它的INTERNAL-IP，由于多台虚拟机NAT网络是隔离的，相互之间无法通通信，且多台主机的NAT网卡对应的IP一模一样，导致工作节点无法加入到集群中，解决方法就是在每个节点都通过`--node-ip=多台主机局域网能相互通信的IP`即可

```sh
[root@k8s-master ~]# kubectl logs ng
Error from server: Get "https://10.0.2.15:10250/containerLogs/default/ng/ng": proxy error from 127.0.0.1:6443 while dialing 10.0.2.15:10250, code 503: 503 Service Unavailable
[root@k8s-master ~]# kubectl exec -it ng -- bash
Error from server: error dialing backend: proxy error from 127.0.0.1:6443 while dialing 10.0.2.15:10250, code 503: 503 Service Unavailable
```

> 最终效果如下

```sh
NAME         STATUS   ROLES                  AGE   VERSION        INTERNAL-IP      EXTERNAL-IP   OS-IMAGE                KERNEL-VERSION                 CONTAINER-RUNTIME
k8s-master   Ready    control-plane,master   42m   v1.26.5+k3s1   192.168.56.102   <none>        CentOS Linux 7 (Core)   3.10.0-1160.114.2.el7.x86_64   containerd://1.7.1-k3s1
k8s-node2    Ready    <none>                 40m   v1.26.5+k3s1   192.168.56.103   <none>        CentOS Linux 7 (Core)   3.10.0-1160.114.2.el7.x86_64   containerd://1.7.1-k3s1
k8s-node1    Ready    <none>                 40m   v1.26.5+k3s1   192.168.56.104   <none>        CentOS Linux 7 (Core)   3.10.0-1160.114.2.el7.x86_64   containerd://1.7.1-k3s1
```

