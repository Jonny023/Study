# k8s基础

[参考文档](https://geekhour.net/2023/12/23/kubernetes/)

## 缩写

| 名称         | 缩写   |
| ------------ | ------ |
| namespaces   | ns     |
| nodes        | no     |
| pods         | po     |
| services     | svc    |
| deployments  | deploy |
| replicasets  | rs     |
| statefulsets | sts    |



## 配置

```sh
# 查看配置
kubectl config view
```



## 节点

```sh
# 查看集群节点 -owide显示更多信息
kubectl get node
```



## pod

```sh
# 查看所有
kubectl get all

# 查看节点
kubectl get pod -owide

# 查看pod信息
kubectl describe pod pod-name

# 删除pod
kubectl delete pod pod-name

# 强制删除pod
kubectl delete pod pod-name --force

# 进入pod
kubectl exec -it pod-name -- /bin/bash
```



## 日志

```sh
# 查看pod日志
kubelctl logs -f pod-name

# 查看master服务
systemctl status k3s 

# 查看工作节点日志
systemctl status k3s-agent

# 实时输出k3s服务日志
journalctl -u k3s -f
```



## 服务 

```sh
# 公开为服务 nginx-deployment为pod名称，--target-port=80为容器端口
# 集群内部容器可通过服务ip+端口访问该服务
kubectl expose deploy/nginx-deployment --name=nginx-server --port=8080 --target-port=80

kubectl get service

# 创建一次性pod --rm退出时删除pod
kubectl run test -it --image=nginx:1.22 --rm -- bash

# 在容器内部执行访问服务
curl nginx-server:8080

# 查看service
kubectl describe service nginx-server
```

## 

## 命名空间（namespace）

> 默认命名空间为default

```sh
# 查看命名空间
kubectl get namespace

kubectl get lease -A

# 创建命名空间,ns为namespace缩写
kubectl create ns dev

# 指定命名空间
kubectl run nginx --image=nginx:1.22 -n=dev

# 查看指定命名空间的pod
kubectl get pod -n=dev

# 设置命名空间
kubectl config set-context $(kubectl config current-context) --name=dev

# 删除命名空间并删除命名空间下所有内容
kubectl delete ns dev
```



## yml配置

[配置模板](https://kubernetes.io/zh-cn/docs/concepts/workloads/pods/)

### 规范

* :键值对，后面必须有空格
* -列表，后面必须有空格
* []数组
* `#`注释
* |多行文本块
* ---表示文档的开始，用于分割多个资源对象

### 创建

```sh
kubectl apply -f xxx.yaml
```

### 删除

```sh
kubectl delete -f xxx.yaml
```

### 

```sh
# 查看标签
kubectl get pod --show-labels

# 查看指定标签
kubectl get pod -l "app=nginx"

# 多条件过滤
kubectl get pod -l "app=nginx,environment=dev"
```

### 导入导出

```sh
docker save nginx:1.22 > nginx-1.22.tar

# 在k8s中都位于k8s.io这个命名空间下
ctr -n k8s.io import nginx-1.22.tar --platform linux/amd64

# 查看镜像
crictl images

# 导出镜像
crt -n k8s.io image export nginx-1.22.tar docker.io/library/nginx:1.22 --platform linux/amd64
```

### ConfigMap

```sh
kubectl get cm

# 查看
kubectl describe cm mysql-config

# 编辑
kubectl edit cm mysql-config
```

### Secret

```sh
# 将密码通过base64进行编码
echo -n '123456' | base64
echo 'MTIzNDU2' | base64 --decode

# 查看有哪些secret
kubectl get secret

# 查看secret
kubectl describe secret/mysql-password
```

## 端口转发

> 外部无法直接访问k8s集群内部的服务，需要用到端口转发将服务暴露出去

```sh
kubectl port-forward pods/mysql-pod --address=192.168.56.105 3306:3306
```

## 扩展

```sh
# 直接在外部执行容器内部命令
kubectl exec -it mysql-pod -- mysql -u root -p -e "GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '123456' WITH GRANT OPTION; FLUSH PRIVILEGES;"
```

