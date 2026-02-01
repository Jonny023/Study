# KubeSphere环境搭建

[参考地址](https://mp.weixin.qq.com/s/QJgs23th9ezp-9m91yTljg)

> 需提前安装`containerd`，`k8s（1.24+）`开始弃用`docker`，改为了`containerd`

## 1.准备

> 配置说明：本文通过虚拟机进行试验，3台虚拟机均为4G 2 cpu，40G，cpu硬性要求不低于2，搭建kubesphere时发现master一直卡住，所以调整到了4G

| 节点   | ip            | 主机名       | 系统    | 说明      |
| ------ | ------------- | ------------ | ------- | --------- |
| master | 192.168.56.20 | kube-master  | centos7 | 主节点    |
| worker | 192.168.56.21 | kube-worker1 | centos7 | 工作节点1 |
| worker | 192.168.56.22 | kube-worker2 | centos7 | 工作节点2 |

### 1.1 设置主机名

```sh
# 设置hostname(kube-master，kube-worker1，kube-worker2)
hostnamectl set-hostname kube-master
hostnamectl set-hostname kube-worker1
hostnamectl set-hostname kube-worker2

# 查看hostname
hostnamectl status
```

### 1.2 Kubernetes 集群节点免密登录

> 配置**kube-master（192.168.56.20）** 免密 SSH 登录**kube-worker1（192.168.56.21）** 和**kube-worker2（192.168.56.22）**，全程在`kube-master`节点操作即可，步骤简洁可直接执行。

* 在`kube-master`节点执行以下命令，**全程按回车默认配置**（无需设置密码，设置密码会失去免密意义）：

```sh
# 生成rsa类型SSH密钥对，默认保存在~/.ssh/目录（id_rsa私钥、id_rsa.pub公钥）
ssh-keygen -t rsa

# 将master自身公钥追加到授权列表，覆盖原有配置问题
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
```

* 执行完成后，会在`/root/.ssh/`（root 用户）或`/home/用户名/.ssh/`（普通用户）下生成两个文件：

  - `id_rsa`：私钥（核心文件，切勿泄露、修改或删除）；
  - `id_rsa.pub`：公钥（需要分发到所有 worker 节点的文件）。

* 将`kube-master`的公钥分别分发到`kube-worker1` 和 `kube-worker2`

```sh
ssh-copy-id root@192.168.56.21
ssh-copy-id root@192.168.56.22
```

### 1.3 配置hosts (kube-master)

> 为了方便所有节点都配置下host
>
> `vi /etc/hosts`

```sh
192.168.56.20  kube-master
192.168.56.21  kube-worker1
192.168.56.22  kube-worker2
```

### 1.4 验证

```sh
ssh kube-worker1
ssh kube-worker2
```

### 1.5 关闭 selinux 和 swap

```sh
# 关闭 selinux：
# 修改selinux后防止不能重启，执行该命令先：touch /.autorelabel
# 需要重启
touch /.autorelabel && sed -i 's/enforcing/disabled/' /etc/selinux/config

setenforce 0  #宽容模式
# 查看：
getenforce
  
#关闭 swap：
swapoff -a  
sed -ri 's/.*swap.*/#&/' /etc/fstab 
#通过free -m确保swap分区都为0即完成
```

### 1.6 安装依赖

```sh
# kube-master节点
yum install -y socat
yum install -y conntrack
yum install -y ebtables
yum install -y ipset

# worker节点
ssh kube-worker1 "yum install -y conntrack socat ebtables ipset"
ssh kube-worker2 "yum install -y conntrack socat ebtables ipset"

yum install -y conntrack socat
# 若为Ubuntu/Debian系列，替换为：apt install -y conntrack socat
# 缺失依赖会出现如下日志

15:26:48 CST [ERRO] kube-worker2: conntrack is required.
15:26:48 CST [ERRO] kube-worker2: socat is required.
```

## 2.安装

### 2.1 在master节点下载KubeKey

```sh
# 在线
export KKZONE=cn
curl -sfL https://get-kk.kubesphere.io | VERSION=v3.1.12 sh -
chmod +x kk

# 获取最新版本
export KKZONE=cn
curl -sfL https://get-kk.kubesphere.io | sh -
chmod +x kk


# 离线 downloadKubekey.sh
sh get-kk.sh
chmod +x kk

# 目录列表
[root@kube-master ~]# ll
total 119712
-rw-------. 1 root root     1236 Apr 24  2024 anaconda-ks.cfg
-rw-r--r--. 1 root root     5107 Jan 31 14:53 get-kk.sh
-rwxr-xr-x. 1 root root 84047082 Jan 26 16:21 kk
-rw-r--r--. 1 root root 38518735 Jan 31 14:55 kubekey-v3.1.12-linux-amd64.tar.gz
-rw-r--r--. 1 root root      826 Apr 24  2024 update-yum.sh

# 启动containerd
systemctl daemon-reexec
systemctl restart containerd
systemctl enable containerd
```

### 2.2 创建集群配置

```sh
# 将 <Kubernetes version> 替换为实际需要的版本，例如 v1.27.4。KubeSphere 默认支持 Kubernetes v1.23~1.32
./kk create config -f config-simple.yaml
```

### 2.3 修改config-simple.yaml

> 特别关注的就是`修改内网ip`，里面的内网ip修改为对应节点的ip

```yaml
apiVersion: kubekey.kubesphere.io/v1alpha3
kind: Cluster
metadata:
  name: sample

spec:
  hosts:
    # 1.修改内网ip, address为公网ip（没有公网可配置为内网ip）, internalAddress为内网ip，集群通过它通信
    # 密码方式
    #- {name: kube-master, address: 192.168.56.20, internalAddress: 192.168.56.20, user: root, password: "root"}
    - {name: kube-master, address: 192.168.56.20, internalAddress: 192.168.56.20, user: root, privateKeyPath: "~/.ssh/id_rsa"}
    - {name: kube-worker1, address: 192.168.56.21, internalAddress: 192.168.56.21, user: root, privateKeyPath: "~/.ssh/id_rsa"}
    - {name: kube-worker2, address: 192.168.56.22, internalAddress: 192.168.56.22, user: root, privateKeyPath: "~/.ssh/id_rsa"}

  roleGroups:
    etcd:
      - kube-master
    control-plane:
      - kube-master
    worker:
      - kube-worker1
      - kube-worker2

  kubernetes:
    version: v1.26.6
    clusterName: cluster.local
    containerManager: containerd
    autoRenewCerts: true

  etcd:
    type: kubekey
    # 2.配置
    dataDir: /var/lib/etcd

  network:
    plugin: calico
    kubePodsCIDR: 10.233.64.0/18
    kubeServiceCIDR: 10.233.0.0/18

  # 3.kubesphere配置
  kubesphere:
    version: v3.4.1
    enabled: true
    console:
      enableMultiLogin: true
      port: 30880
    monitoring:
      enabled: false
    logging:
      enabled: false
    devops:
      enabled: false
```

### 2.4 引导创建集群

```sh
./kk delete cluster
# 安装
./kk create cluster -f config-simple.yaml -y

# 配置文件中配置kubesphere
./kk create cluster -f config-simple.yaml --with-kubesphere v3.4.1 -y

# 添加kubesphere
./kk create cluster \
  -f config-simple.yaml \
  --with-kubernetes v1.26.6 \
  --with-kubesphere v3.4.1 \
  -y
```

* 查看

```
watch -n2 "kubectl get pods -n kube-system | grep calico"
```

### 2.5 worker配置

```sh
scp root@kube-master:/etc/kubernetes/admin.conf root@kube-worker1:/root/admin.conf
scp root@kube-master:/etc/kubernetes/admin.conf root@kube-worker2:/root/admin.conf

# 每个worker执行
# 管理员用户
cp -p ~/admin.conf ~/.kube/config
# 普通用户
cp -p ~/admin.conf $HOME/.kube/config
```

## 3.访问

### 3.1 web

[http://192.168.56.20:30880](http://192.168.56.20:30880)

* 用户名：admin
* 默认密码：P@88w0rd
* 第一次登陆要求修改密码，比如我修改为：Root123456

## 4.面板中文配置

### 4.1 编辑配置

```sh
kubectl edit clusterconfiguration ks-installer -n kubesphere-system
```

* 在`spec.common`下添加`consoleDefaultLanguage`：

```yaml
spec:
  common:
    consoleDefaultLanguage: zh-CN  # 可选值：zh-CN/zh-TW/en/es
```

* 保存后重启 ks-console 生效：

```sh
kubectl rollout restart deployment ks-console -n kubesphere-system
```

## 5.查看

### 5.1 查看集群节点

```sh
[root@kube-master ~]# kubectl get nodes
NAME           STATUS   ROLES           AGE   VERSION
kube-master    Ready    control-plane   89m   v1.26.6
kube-worker1   Ready    worker          89m   v1.26.6
kube-worker2   Ready    worker          89m   v1.26.6

# 包含ip
kubectl get nodes -o wide
```

### 5.2 查看集群信息

```sh
[root@kube-master ~]# kubectl cluster-info
Kubernetes control plane is running at https://lb.kubesphere.local:6443
CoreDNS is running at https://lb.kubesphere.local:6443/api/v1/namespaces/kube-system/services/coredns:dns/proxy

To further debug and diagnose cluster problems, use 'kubectl cluster-info dump'.
```

### 5.3 查看k8s版本

```sh
kubectl version --short
```

### 5.4 查看所有命名空间

> 完整写法：kubectl get pods --all-namespaces

```sh
[root@kube-master ~]# kubectl get namespaces
NAME                              STATUS   AGE
default                           Active   93m
kube-node-lease                   Active   93m
kube-public                       Active   93m
kube-system                       Active   93m
kubekey-system                    Active   92m
kubesphere-controls-system        Active   78m
kubesphere-monitoring-federated   Active   78m
kubesphere-monitoring-system      Active   80m
kubesphere-system                 Active   80m
```

### 5.5 查看所有pod

```sh
[root@kube-master ~]# kubectl get pods -A
NAMESPACE                      NAME                                               READY   STATUS    RESTARTS       AGE
kube-system                    calico-kube-controllers-57db949bd8-9574s           1/1     Running   1 (38m ago)    93m
kube-system                    calico-node-6ws5b                                  1/1     Running   1 (38m ago)    93m
kube-system                    calico-node-ghdlz                                  1/1     Running   1 (38m ago)    93m
kube-system                    calico-node-sqz8t                                  1/1     Running   1 (38m ago)    93m
kube-system                    coredns-6ffcf7c9c6-qzj9t                           1/1     Running   1 (38m ago)    65m
kube-system                    coredns-6ffcf7c9c6-tq2ls                           1/1     Running   1 (38m ago)    66m
kube-system                    kube-apiserver-kube-master                         1/1     Running   1 (38m ago)    93m
kube-system                    kube-controller-manager-kube-master                1/1     Running   3 (38m ago)    93m
kube-system                    kube-proxy-pmt44                                   1/1     Running   1 (38m ago)    93m
kube-system                    kube-proxy-qtt9h                                   1/1     Running   1 (38m ago)    93m
kube-system                    kube-proxy-smfnv                                   1/1     Running   1 (38m ago)    93m
kube-system                    kube-scheduler-kube-master                         1/1     Running   3 (38m ago)    93m
kube-system                    nodelocaldns-hrtg4                                 1/1     Running   1 (38m ago)    93m
kube-system                    nodelocaldns-ht6g6                                 1/1     Running   1 (38m ago)    93m
kube-system                    nodelocaldns-jdqp4                                 1/1     Running   1 (38m ago)    93m
kube-system                    openebs-localpv-provisioner-589cc46f59-vbwgj       1/1     Running   12 (34m ago)   81m
kube-system                    snapshot-controller-0                              1/1     Running   1 (38m ago)    78m
kubesphere-controls-system     default-http-backend-767cdb5fdc-8lc45              1/1     Running   1 (38m ago)    77m
kubesphere-controls-system     kubectl-admin-5656cd6dfc-vthpq                     1/1     Running   0              29m
kubesphere-monitoring-system   alertmanager-main-0                                2/2     Running   0              34m
kubesphere-monitoring-system   alertmanager-main-1                                2/2     Running   0              34m
kubesphere-monitoring-system   alertmanager-main-2                                2/2     Running   0              34m
kubesphere-monitoring-system   kube-state-metrics-7f4df45cc5-ck8rs                3/3     Running   0              35m
kubesphere-monitoring-system   node-exporter-4sk9w                                2/2     Running   0              35m
kubesphere-monitoring-system   node-exporter-phctg                                2/2     Running   0              35m
kubesphere-monitoring-system   node-exporter-wfdtx                                2/2     Running   0              35m
kubesphere-monitoring-system   notification-manager-deployment-6bd69dcc66-6fmp8   2/2     Running   0              31m
kubesphere-monitoring-system   notification-manager-deployment-6bd69dcc66-fdmsf   2/2     Running   0              31m
kubesphere-monitoring-system   notification-manager-operator-69b55cdd9-6sc4n      2/2     Running   0              34m
kubesphere-monitoring-system   prometheus-k8s-0                                   2/2     Running   0              34m
kubesphere-monitoring-system   prometheus-k8s-1                                   2/2     Running   0              34m
kubesphere-monitoring-system   prometheus-operator-6fb9967754-xs7gn               2/2     Running   0              35m
kubesphere-system              ks-apiserver-bc7f66f4-g8zrp                        1/1     Running   0              77m
kubesphere-system              ks-console-6d4cf64b58-k5s4d                        1/1     Running   0              16m
kubesphere-system              ks-controller-manager-595b688958-89h9m             1/1     Running   0              77m
kubesphere-system              ks-installer-76d9d9d4f7-xpsqk                      1/1     Running   2 (37m ago)    81m
```

### 5.6 查看指定命名空间的 Pod

```sh
[root@kube-master ~]# kubectl get pods -n kubesphere-system
NAME                                     READY   STATUS    RESTARTS      AGE
ks-apiserver-bc7f66f4-g8zrp              1/1     Running   0             79m
ks-console-6d4cf64b58-k5s4d              1/1     Running   0             17m
ks-controller-manager-595b688958-89h9m   1/1     Running   0             79m
ks-installer-76d9d9d4f7-xpsqk            1/1     Running   2 (39m ago)   83m
```

### 5.7 查看pod IP

```sh
kubectl get pods -n kube-system -o wide
```

### 5.8 查看 Pod日志

```sh
kubectl logs ks-console-6d4cf64b58-k5s4d -n kubesphere-system
# 查看日志最后100行（排查崩溃问题）
kubectl logs ks-console-6d4cf64b58-k5s4d -n kubesphere-system --tail=100
# 实时日志
kubectl logs ks-console-6d4cf64b58-k5s4d -n kubesphere-system --tail=100 -f
```

## 6.补全工具

> kubectl 自动补全

```sh
# 安装 
yum install bash-completion -y
  
# 自动补全 
echo 'source <(kubectl completion bash)' >>~/.bashrc 
kubectl completion bash >/etc/bash_completion.d/kubectl 
source /usr/share/bash-completion/bash_completion
```



## 7.卸载

### 1.停止kk / k8s / containerd

```sh
systemctl stop kubelet || true
systemctl stop containerd || true
systemctl stop docker || true
```

### 2.清理master

```sh
#!/bin/bash
set -e

echo ">>> stop services"
systemctl stop kubelet 2>/dev/null || true
systemctl stop containerd 2>/dev/null || true

echo ">>> remove kk state"
rm -rf /root/.kk
rm -rf /root/.kube

echo ">>> remove kubernetes"
rm -rf /etc/kubernetes
rm -rf /var/lib/kubelet
rm -rf /var/lib/etcd
rm -rf /etc/cni
rm -rf /opt/cni
rm -rf /run/kubernetes

echo ">>> remove etcd leftovers"
rm -rf /etc/ssl/etcd
rm -rf /var/lib/etcd

echo ">>> iptables & net"
iptables -F || true
iptables -t nat -F || true
iptables -t mangle -F || true
iptables -X || true
ipvsadm -C || true

echo ">>> done"
```

### 3.清理worker

```sh
rm -rf /root/.kk /root/.kube
rm -rf /etc/kubernetes /var/lib/kubelet /etc/cni /opt/cni
iptables -F
```

### 4.删除文件

```sh
rm -rf /var/lib/etcd
rm -f /etc/etcd.env
rm -rf /etc/ssl/etcd
```



## 问题

### 1.虚拟机拷贝无法上网

* virtualbox虚拟机

#### 1.1 nat网卡

> NAT网卡无法访问公网

```sh
# 方式1
# 释放enp0s8旧IP，重新获取（强制刷新）
dhclient -r enp0s8 && dhclient enp0s8

# 方式2
###########强烈推荐#############
###########强烈推荐#############
##########强烈推荐##############
# 清空enp0s8网卡配置(一劳永逸)
# 删除旧的配置
rm -rf /var/lib/dhclient/*
rm -rf /etc/udev/rules.d/70-persistent-net.rules
# 重新配置
echo "" > /etc/sysconfig/network-scripts/ifcfg-enp0s8 \
&& cat > /etc/sysconfig/network-scripts/ifcfg-enp0s8 << 'EOF'
TYPE=Ethernet
BOOTPROTO=dhcp
NAME=enp0s8
DEVICE=enp0s8
ONBOOT=yes
NM_CONTROLLED=no
PERSISTENT_DHCLIENT=yes
EOF


# 重启网卡
systemctl restart network
```

#### 1.2 host-only网卡

> host-only网卡重启后无ip，导致无法远程连接，每次重启都要`service network restart`才生效

* 问题分析
  * 1.服务没启动
  * 2.配置存在问题

##### 1.2.1 方式1

> 通过查看状态发现network服务是dead状态，需要启动开机自启

```sh
systemctl status network
chkconfig network on
systemctl start network

# 确保网卡 UP
ifup enp0s8

# 手动获取 DHCP，这里会生成dhclient.leases
dhclient -v enp0s8

# 正常的
[root@kube-worker1 ~]# ll /var/lib/dhclient/
total 8
-rw-r--r-- 1 root root 836 Feb  1 10:41 dhclient--enp0s8.lease
-rw-r--r-- 1 root root 418 Jan 31 21:36 dhclient.leases
```

##### 1.2.2 方式2

###### 1.2.2.1 创建系统服务

* `/etc/systemd/system/ifup-enp0s3.service`

```sh
[Unit]
Description=Activate enp0s3 after boot
After=network.target

[Service]
Type=oneshot
ExecStart=/sbin/ifup enp0s3
RemainAfterExit=yes

[Install]
WantedBy=multi-user.target
```

###### 1.2.2.2 开机自启

```sh
systemctl daemon-reload
systemctl enable ifup-enp0s3.service
systemctl start ifup-enp0s3.service
```

