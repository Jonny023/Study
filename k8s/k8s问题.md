## k8s问题

## 问题1

> error execution phase preflight: [preflight] Some fatal errors occurred:
> 	[ERROR CRI]: container runtime is not running: output: E1027 14:36:25.323157    6112 remote_runtime.go:948] "Status from runtime service failed" err="rpc error: code = Unimplemented desc = unknown service runtime.v1alpha2.RuntimeService"
> time="2022-10-27T14:36:25+08:00" level=fatal msg="getting status of runtime: rpc error: code = Unimplemented desc = unknown service runtime.v1alpha2.RuntimeService"
> , error: exit status 1
> [preflight] If you know what you are doing, you can make a check non-fatal with `--ignore-preflight-errors=...`
> To see the stack trace of this error execute with --v=5 or higher

```sh
kubeadm init --ignore-preflight-errors=all

# 还有就是docker需要："exec-opts": ["native.cgroupdriver=systemd"]
[root@k8s-master1 ~]# cat /etc/docker/daemon.json 
{
  "registry-mirrors": ["https://5vfekjm3.mirror.aliyuncs.com"],
  "exec-opts": ["native.cgroupdriver=systemd"],
  "log-driver": "json-file",
  "log-opts": {
    "max-size": "100m"
  },
  "storage-driver": "overlay2"
}
```



## 问题2

> systemctl start kubelet 报错

```sh
[root@ECS-k8s-master docker]# kubeadm init   --apiserver-advertise-address=192.168.2.2 --image-repository registry.aliyuncs.com/google_containers   --kubernetes-version v1.24.2   --service-cidr=10.140.0.0/16 --pod-network-cidr=10.240.0.0/16
[init] Using Kubernetes version: v1.24.2
[preflight] Running pre-flight checks
error execution phase preflight: [preflight] Some fatal errors occurred:
  [ERROR CRI]: container runtime is not running: output: E0704 14:55:48.132984    5226 remote_runtime.go:925] "Status from runtime service failed" err="rpc error: code = Unimplemented desc = unknown service runtime.v1alpha2.RuntimeService"
time="2022-07-04T14:55:48+08:00" level=fatal msg="getting status of runtime: rpc error: code = Unimplemented desc = unknown service runtime.v1alpha2.RuntimeService"
, error: exit status 1
[preflight] If you know what you are doing, you can make a check non-fatal with `--ignore-preflight-errors=...`
To see the stack trace of this error execute with --v=5 or higher
```

* 修改docker配置

```sh
rm /etc/containerd/config.toml
systemctl restart containerd
```



## 问题3

> 初始化提示文件存在

```sh
[root@k8s-master1 ~]# kubeadm init --config kubeadm-init.yaml
[init] Using Kubernetes version: v1.25.0
[preflight] Running pre-flight checks
error execution phase preflight: [preflight] Some fatal errors occurred:
	[ERROR FileAvailable--etc-kubernetes-manifests-kube-apiserver.yaml]: /etc/kubernetes/manifests/kube-apiserver.yaml already exists
	[ERROR FileAvailable--etc-kubernetes-manifests-kube-controller-manager.yaml]: /etc/kubernetes/manifests/kube-controller-manager.yaml already exists
	[ERROR FileAvailable--etc-kubernetes-manifests-kube-scheduler.yaml]: /etc/kubernetes/manifests/kube-scheduler.yaml already exists
	[ERROR FileAvailable--etc-kubernetes-manifests-etcd.yaml]: /etc/kubernetes/manifests/etcd.yaml already exists
	[ERROR Port-10250]: Port 10250 is in use
[preflight] If you know what you are doing, you can make a check non-fatal with `--ignore-preflight-errors=...`
To see the stack trace of this error execute with --v=5 or higher
```

* 需要删除旧的文件

```sh
rm -rf /etc/kubernetes
```



## 问题4

> ERRO[0000] unable to determine image API version: rpc error: code = Unavailable desc = connection error: desc = "transport: Error while dialing dial unix /var/run/dockershim.sock: connect: no such file or directory" 
> Image is up to date for sha256:4873874c08efc72e9729683a83ffbb7502ee729e9a5ac097723806ea7fa13517

* 添加配置

  ```sh
  [root@k8s-master1 ~]# crictl config runtime-endpoint unix:///run/containerd/containerd.sock
  [root@k8s-master1 ~]# crictl config image-endpoint unix:///run/containerd/containerd.sock
  
  
  crictl config runtime-endpoint unix:///var/run/dockershim.sock unix:///run/containerd/containerd.sock
  
  crictl config image-endpoint unix:///var/run/dockershim.sock unix:///run/containerd/containerd.sock
  ```

  

## 问题5

> Oct 28 10:51:48 k8s-master1 kubelet[1583]: I1028 10:51:48.411231    1583 cni.go:240] "Unable to update cni config" err="no networks found in /etc/cni/net.d"
> Oct 28 10:51:50 k8s-master1 kubelet[1583]: I1028 10:51:50.517147    1583 scope.go:110] "RemoveContainer" containerID="2d54fda502f99dca18d81ae7bafe2a012934a432bec5338907635b0c465f4983"
> Oct 28 10:51:50 k8s-master1 kubelet[1583]: E1028 10:51:50.539363    1583 pod_workers.go:951] "Error syncing pod, skipping" err="failed to \"StartContainer\" for \"kube-controller-manager\" with CrashLoopBackOff: \"back-off 2m40s restarting failed container=kube-controller-manager pod=kube-controller-manager-k8s-master1_kube-system(9054a5d7b5befa0bad21882ce0905d05)\"" pod="kube-system/kube-controller-manager-k8s-master1" podUID=9054a5d7b5befa0bad21882ce0905d05

* 解决

  ```sh
  yum list *rhsm*
  # 安装：
  yum install *rhsm*
  
  #重启pod：
  #1、到master节点删除pod，查看pod：
  ```

  

## 问题6

> 从节点执行查看节点命令报错：root@k8s-master1 ~]# kubectl logs kube-proxy-xxx -n kube-system
> The connection to the server localhost:8080 was refused - did you specify the right host or port?

* 原因：未配置环境变量

  ```sh
  # 复制主节点的配置到从节点
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

  