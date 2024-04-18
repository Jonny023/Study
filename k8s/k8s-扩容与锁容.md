

# 扩容与锁容



> 动态的扩缩容



### 部署nginx

```sh
# 创建deployment,部署3个运行nginx的Pod
kubectl create deployment nginx-deployment --image=nginx:1.22 --replicas=3
# 查看deployment
kubectl get deploy
# 查看replicaSet
kubectl get rs 
# 删除deployment
kubectl delete deploy nginx-deployment
```

### 缩放 

●手动缩放

```sh
# 查看副本收缩过程
kubectl get replicaset --watch

# 将副本数量调整为5
kubectl scale deployment/nginx-deployment --replicas=5
kubectl get deploy
```

●自动缩放

自动缩放通过增加和减少副本的数量，以保持所有 Pod 的平均 CPU 利用率不超过 75%。

自动伸缩需要声明Pod的资源限制，同时使用 [Metrics Server](https://github.com/kubernetes-sigs/metrics-server#readme) 服务（K3s默认已安装）。

> 本例仅用来说明kubectl autoscale命令的使用，完整示例参考：[HPA演示](https://kubernetes.io/zh-cn/docs/tasks/run-application/horizontal-pod-autoscale-walkthrough/)

```sh
# 自动缩放
kubectl autoscale deployment/nginx-deployment --min=3 --max=10 --cpu-percent=75 

# 查看自动缩放
kubectl get hpa

# 删除自动缩放
kubectl delete hpa nginx-deployment
```

* 滚动更新 

```sh
#查看版本和Pod
kubectl get deployment/nginx-deployment -owide
kubectl get pods

#更新容器镜像
kubectl set image deployment/nginx-deployment nginx=nginx:1.23
#滚动更新
kubectl rollout status deployment/nginx-deployment
#查看过程
kubectl get rs --watch
```

* 版本回滚 

```sh
#查看历史版本
kubectl rollout history deployment/nginx-deployment
#查看指定版本的信息
kubectl rollout history deployment/nginx-deployment --revision=2
#回滚到历史版本
kubectl rollout undo deployment/nginx-deployment --to-revision=2
```

