# Helm安装

下载安装包：

https://github.com/helm/helm/releases

https://get.helm.sh/helm-v3.10.0-linux-amd64.tar.gz



### 安装

```sh
#下载
wget -O helm-v3.10.0-linux-amd64.tar.gz https://get.helm.sh/helm-v3.10.0-linux-amd64.tar.gz

# 解压
tar -zxvf helm-v3.10.0-linux-amd64.tar.gz

mv linux-amd64/helm /usr/local/bin/helm
```

在K3s中使用，需要配置环境变量

```sh
export KUBECONFIG=/etc/rancher/k3s/k3s.yaml

# 也可以将它配置到用户配置文件中，不用每次都声明
vim ~/.bash_prifile

# 放到文件最后面即可，刷新配置
source ~/.bash_profile
```

### 简单使用

```sh
helm repo add bitnami https://charts.bitnami.com/bitnami

# 也可以添加国内的一些库（阿里云等）
helm repo add aliyun https://kubernetes.oss-cn-hangzhou.aliyuncs.com/charts
# 微软库
helm repo add azure http://mirror.azure.cn/kubernetes/charts

helm repo update   # 类似 yum update

# 查看仓库列表
helm repo list

# 移除指定的存储库aliyun
helm repo remove aliyun

# 简单部署一个mysql
helm install my-mysql \
--set-string auth.rootPassword=123456 \
--set primary.persistence.size=2Gi \
bitnami/mysql

# 查看配置
helm get values my-mysql

# 删除
helm delete my-mysql

# 无法删除
kubectl delete pod redis-master-0 --grace-period=0 --force
# 卸载redis
helm uninstall redis
# 或者
helm delete redis

# 查看信息
helm status redis

# 运行一个redis
# 参考：https://huaweicloud.csdn.net/637ef028df016f70ae4ca30e.html
helm install redis \
--set architecture=standalone \
--set-string auth.password=123456 \
--set master.persistence.enabled=false \
--set master.persistence.medium=Memory \
--set master.persistence.size=200Mi \
bitnami/redis \
--kubeconfig=/etc/rancher/k3s/k3s.yaml
```

* 查看安装

```sh
helm ls --all-namespaces
```



### 问题

#### 问题1

> 执行helm命令报错：[root@k8s-master ~]# helm uninstall redis
> Error: Kubernetes cluster unreachable: Get "http://localhost:8080/version": dial tcp [::1]:8080: connect: connection refused

```sh
# 通过k3s部署的k8s集群，执行helm报错需要指定k3s配置文件路径
export KUBECONFIG=/etc/rancher/k3s/k3s.yaml
```

#### 问题2

> helm部署应用失败

* 通过kubectl describe pod-name可以查看错误信息 

