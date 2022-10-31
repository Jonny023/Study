## k8s部署java

[参考地址](https://www.cnblogs.com/guyouyin123/p/15592483.html)

[参考地址1](https://blog.csdn.net/u010245179/article/details/105845211)

| 主机        | 操作系统  | IP           | docker版本 | k8s版本 |
| ----------- | --------- | ------------ | ---------- | ------- |
| k8s-master1 | Centos7.9 | 192.168.70.4 | 20.10.18   | 1.25-0  |
| k8s-node1   | Centos7.9 | 192.168.70.5 | 20.10.18   | 1.25-0  |
| k8s-node2   | Centos7.9 | 192.168.70.6 | 20.10.18   | 1.25-0  |

### 1.准备（master节点）

```sh
# 构建docker,Dockerfile内容如下
FROM openjdk:8u201-jdk-alpine3.9
VOLUME /tmp
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' > /etc/timezone
EXPOSE 8080
ADD springboot-dockerfile-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","./app.jar","-C"]

# 构建dokcer镜像
docker build -t test:1.0 .

# 测试运行
docker run --name test -p 8088:8080 test:1.0

# 首先准备一个项目，docker部署后访问地址为：
http://192.168.70.4:8088/api/post/
```

### 2.推送到ali镜像仓库（master节点）

> 每个节点都要拉取镜像

```sh
# 需要先到阿里云注册：官网--产品--容器与中间件--容器镜像服务--管理控制台--个人实例
docker login registry.cn-hangzhou.aliyuncs.com
docker login --username=username registry.cn-hangzhou.aliyuncs.com
username  password

#标记镜像,my_com为命名空间，test为仓库名称
docker tag test:1.0 registry.cn-hangzhou.aliyuncs.com/my_com/test:1.0

#推送镜像
docker push registry.cn-hangzhou.aliyuncs.com/my_com/test:1.0

#拉取镜像(master节点和worker节点都要执行)
docker pull registry.cn-hangzhou.aliyuncs.com/my_com/test:1.0
```

### 3.构建配置（master节点）

> 运行：   kubectl apply -f svc-deployment.yaml

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: test-v1
  namespace: default
spec:
  replicas: 3
  selector:
    matchLabels:
      app: test-v1
      release: stabel
  template:
    metadata:
      labels:
        app: test-v1
        release: stabel
        env: test
    spec:
      containers:
      - name: test-v1
        #这里注意是命名空间+仓库名称
        #注意：latest版本号，远程仓库不认，需要对应版本号为1.0
        image: registry.cn-hangzhou.aliyuncs.com/my_com/test:1.0
        #本地有的话取本地，没有的话取远程仓库
        imagePullPolicy: IfNotPresent
        ports:
        - name: http
          containerPort: 80
```

> kubectl apply -f svc-nodePort.yaml

```yaml
apiVersion: v1
kind: Service
metadata:
  name: test-v1
  namespace: default
spec:
  type: NodePort
  selector:
    app: test-v1
    release: stabel
  ports:
  - name: http
    port: 8080
    targetPort: 8080
    nodePort: 30080
```

> 执行完成后访问：http://ip:30080/api/post/

### 4.弹性扩容

```sh
# 修改pod数量

#方式1 编辑配置
kubectl edit deployments test-v1
# 修改pod数量
replicas: 6

# 方式2 打标签
kubectl patch deployments.apps test-v1 -p '{"spec":{"replicas":1}}'

# 方式3（个人推荐）
# master节点不创建pod
kubectl scale 资源类型/资源名称 --replicas=10
kubectl scale deployment/test-v1 --replicas=6


# 查看pod数量
kubectl get deployments
```

### 5.查看

```sh
# 查看集群节点
kubectl get nodes -o wide
kubectl get deployment,pods -o wide

# 查看集群信息
kubectl cluster-info
```



### 6.删除

```sh
kubectl delete -f svc-deployment.yaml
kubectl delete -f svc-nodePort.yaml
```



### 7.宕机(断电)重启

> 当所有的worker节点宕机或者断电后，worker系统重启后，无法访问服务，需要在master节点执行重启

```sh
systemctl daemon-reload
systemctl restart kubelet
```

