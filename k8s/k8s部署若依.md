## k8s部署若依

[参考地址](https://www.yuque.com/wukong-zorrm/qdoy5p/ewazac)

* 通过k3s进行部署

```sh
export KUBECONFIG=/etc/rancher/k3s/k3s.yaml
```

## 部署单机版redis

```sh
# 查看ip
# ip -o -4 addr list | grep enp0s8 | awk '{print $4}' | cut -d/ -f1
# 真实ip
# curl -Ls http://ipv4.rehi.org/ip

helm install redis \
--set architecture=standalone \
--set-string auth.password=123456 \
--set master.persistence.enabled=false \
--set master.persistence.medium=Memory \
--set master.persistence.size=200Mi \
--set master.extraEnvVars[0].name=TZ \
--set master.extraEnvVars[0].value=Asia/Shanghai \
bitnami/redis \
--kubeconfig=/etc/rancher/k3s/k3s.yaml

# redis-master.default.svc.cluster.local

# 卸载
# helm uninstall redis
# 如果失败，可以手动删除pod
# kubectl delete pod redis --force

# 创建测试客户端
kubectl run redis-client --rm -it --image bitnami/redis --namespace default --env REDIS_PASSWORD=123456 -- bash

# 连接测试，redis-master为svc的服务名
redis-cli -h redis-master -a 123456
```

## 部署mysql

[参考](https://dev.mysql.com/doc/mysql-operator/en/mysql-operator-innodbcluster-simple-helm.html)

[参考2](https://juejin.cn/post/7210285643792547899)

* 在~/目录创建ry-mysql.yaml
  * vim ~/ry-mysql.yaml
  * https://www.yuque.com/wukong-zorrm/qdoy5p/qv96ln
  * 亲测不能将volumePermissions.enabled: true配置上，否则工作节点无法通过mysql -uroot -p连接
    * 默认密码为null

```yaml
auth:
  rootPassword: "123456"
  database: ry-vue

initdbScriptsConfigMap: ruoyi-init-sql
volumePermissions:
  enabled: false

primary:
  persistence:
    size: 1Gi
    enabled: true
  extraEnvVars:
    - name: TZ
      value: "Asia/Shanghai"
  startupProbe:
    enabled: false
  readinessProbe:
    enabled: false
  livenessProbe:
    enabled: false
  
secondary:
  replicaCount: 2
  persistence:
    size: 1Gi
    enabled: true
  extraEnvVars:
    - name: TZ
      value: "Asia/Shanghai"
  startupProbe:
    enabled: false
  readinessProbe:
    enabled: false
  livenessProbe:
    enabled: false

architecture: replication
```

* 安装

```sh
# 创建脚本目录，并将ruoyi数据库脚本上传到此目录
mkdir -p /home/app/sql

# 创建ConfigMap
kubectl create cm ruoyi-init-sql --from-file=/home/app/sql

# 删除
kubectl delete cm ruoyi-init-sql

# 查看ConfigMap
kubectl describe configmap/ruoyi-init-sql

# helm部署mysql
helm install db -f ry-mysql.yaml \
bitnami/mysql \
--kubeconfig=/etc/rancher/k3s/k3s.yaml

# 卸载mysql
helm delete db
# 删除pvc
kubectl delete pvc $(kubectl get pvc | grep mysql | awk '{print $1}') --force

# 验证
# 密码修改为刚才设置的密码
kubectl run mysql-client --rm -it --image bitnami/mysql --namespace default --env MYSQL_ROOT_PASSWORD=123456 -- bash

# 连接测试
mysql -h db-mysql-primary -u root -p
mysql -h db-mysql-secondary -u root -p


# 也可暴露端口，通过可视化工具连接测试
kubectl port-forward pods/mysql-primary-0 --address=192.168.56.105 3306:3306
kubectl port-forward pods/redis-master-0 --address=192.168.56.105 6379:6379
# 工作节点测试
kubectl port-forward pods/db-mysql-secondary-0 --address=192.168.56.104 3306:3306
kubectl port-forward --address 0.0.0.0 service/db-mysql-primary  8006:3306 -n prod

# echo Primary: mysql-primary.default.svc.cluster.local:3306
# echo Secondary: mysql-secondary.default.svc.cluster.local:3306

# 查看集群日志
kubectl get events --all-namespaces
kubectl get events -o yaml
```

## 编译后端

#### 方式1

* dockerfile

```dockerfile
FROM maven as build
WORKDIR /app/build
VOLUME d:/.m2 /root/.m2
COPY . .
RUN mvn clean package

FROM openjdk:8u342-jre
WORKDIR /app/ruoyi
COPY --from=build /app/build/ruoyi-admin/target/ruoyi-admin.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ruoyi-admin.jar"]
```

* 执行编译

```sh
#打包镜像
docker build -t ruoyi-admin:v3.8 .    
```

#### 方式2

> 将后端项目打包为jar，上传到服务器，在服务器上通过docker build制作镜像

* 创建dockerfile

```dockerfile
FROM openjdk:11.0.11-jdk-slim

ENV TZ=Asia/Shanghai

RUN echo -e "${TZ}" > /etc/timezone && ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime

# 复制本地的 ruoyi-admin.jar 到 Docker 镜像中
COPY ruoyi-admin.jar /app/ruoyi/ruoyi-admin.jar

WORKDIR /app/ruoyi

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "ruoyi-admin.jar"]
```

* 创建镜像

```sh
docker build -t ruoyi-admin:v3.8 -f ry-dockerfile .
```



## 编译前端

#### 方式1

* ry-admin-dockerfile

```dockerfile
FROM node:14-alpine AS build
WORKDIR /build/ruoyi-ui
COPY . .
# 安装依赖并打包到正式环境
#RUN npm install --registry=https://registry.npmmirror.com && npm run build:prod
RUN npm install --registry=https://registry.npm.taobao.org && npm run build:prod

FROM nginx:1.22
WORKDIR /app/ruoyi-ui
COPY --from=build /build/ruoyi-ui/dist .
EXPOSE 80
```

* 编译打包

```sh
docker build -t ruoyi-ui:v3.8 -f ry-admin-dockerfile .
```



#### 方式2

> 将前端项目编译打包，最后将dist目录打包为dist.tar上传到服务器

* ry-ui-dockerfile

```
FROM nginx:1.22
WORKDIR /app/ruoyi-ui
COPY dist /app/ruoyi-ui/
EXPOSE 80
```

* 构建镜像

```sh
docker build -t ruoyi-ui:v3.8 -f ry-ui-dockerfile .
```



## 搭建私有镜像仓库

* [registry](https://hub.docker.com/_/registry) 或 [harbor](https://hub.docker.com/r/bitnami/harbor-portal)

```sh
# 这里以registry为例
docker run -d -p 5000:5000 --restart always -e REGISTRY_STORAGE_DELETE_ENABLED="true" --name registry registry:2

# ui界面：http://192.168.56.102:8083
docker run -itd -p 8083:80 --name registry-ui \
-e REGISTRY_URL="http://192.168.56.102:5000" \
-e DELETE_IMAGES="true" \
-e REGISTRY_TITLE="RegistryUI" \
-d joxit/docker-registry-ui:1.5-static
```

* 将镜像推送到私有仓库

```sh
# 以文件方式导入docker镜像
docker load < ruoyi-admin.tar
docker load < ruoyi-ui.tar

#推送后端镜像
#修改镜像tag
docker tag ruoyi-admin:v1.0 192.168.56.102:5000/ruoyi-admin:v3.8
#推送到私有镜像仓库中
docker push 192.168.56.102:5000/ruoyi-admin:v3.8

# 报错：Get "https://k8s-master:5000/v2/": http: server gave HTTP response to HTTPS client
# 修改配置文件/etc/docker/daemon.json，vim /etc/docker/daemon.json
"insecure-registries": ["192.168.56.102:5000"]


#推送前端镜像
#修改镜像tag
docker tag ruoyi-ui:v3.8 192.168.56.102:5000/ruoyi-ui:v3.8
#推送到私有镜像仓库中
docker push 192.168.56.102:5000/ruoyi-ui:v3.8
```



* 拉取测试

```yaml
# 拉取测试
crictl pull 192.168.56.102:5000/ruoyi-admin:v3.8

# 拉取报错
# 每台机器修改配置
vim /etc/rancher/k3s/registries.yaml
# 写入ip配置及endpoint
mirrors:
  docker.io:
    endpoint:
      - "https://fsp2sfpr.mirror.aliyuncs.com/"
  # 加入下面的配置
  192.168.56.102:5000:
    endpoint:
      #使用http协议
      - "http://192.168.56.102:5000"
```

* 重启k8s

```sh
#重启master组件
systemctl restart k3s
#重启node组件
systemctl restart k3s-agent
```

* 查看containerd的配置文件

```sh
cat  /var/lib/rancher/k3s/agent/etc/containerd/config.toml
```

* 拉取镜像

```sh
docker pull 192.168.56.102:5000/ruoyi-admin:v3.8
docker pull 192.168.56.102:5000/ruoyi-ui:v3.8

# 或者用containerd命令
crictl pull 192.168.56.102:5000/ruoyi-admin:v3.8
crictl pull 192.168.56.102:5000/ruoyi-ui:v3.8
```

### 清理镜像

```sh
# 查看镜像目录
docker exec registry ls /var/lib/registry/docker/registry/v2/repositories

# 删除指定镜像
docker exec registry rm -rf /var/lib/registry/docker/registry/v2/repositories/要删除的容器名称

# 垃圾回收
docker exec registry bin/registry garbage-collect /etc/docker/registry/config.yml

# 重启容器
docker restart registry
```



## 正式部署

### 部署后端

* redis和mysql地址

```sh
# helm status redis
# helm status mysql

# redis
redis-master.default.svc.cluster.local

# mysql
Primary: db-mysql-primary.default.svc.cluster.local:3306
Secondary: db-mysql-secondary.default.svc.cluster.local:3306
```

* application-k8s.yaml
  * mkdir -p /home/app/ && vim /home/app/application-k8s.yaml

```yaml
# 数据源配置
spring:
  # redis 配置
  redis:
    # 地址
    host: redis-master
    # 端口，默认为6379
    port: 6379
    # 数据库索引
    database: 0
    # 密码
    password: 123456
    # 连接超时时间
    timeout: 10s
    lettuce:
      pool:
        # 连接池中的最小空闲连接
        min-idle: 0
        # 连接池中的最大空闲连接
        max-idle: 8
        # 连接池的最大数据库连接数
        max-active: 8
        # #连接池最大阻塞等待时间（使用负值表示没有限制）
        max-wait: -1ms
    datasource:
      type: com.alibaba.druid.pool.DruidDataSource
      driverClassName: com.mysql.cj.jdbc.Driver
      druid:
        # 主库数据源
        master:
          url: jdbc:mysql://db-mysql-primary:3306/ry-vue?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
          username: root
          password: 123456
        # 从库数据源
        slave:
          url: jdbc:mysql://db-mysql-secondary:3306/ry-vue?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
          # 从数据源开关/默认关闭
          enabled: false
          username: root
          password: 123456
        # 初始连接数
        initialSize: 5
        # 最小连接池数量
        minIdle: 10
        # 最大连接池数量
        maxActive: 20
        # 配置获取连接等待超时的时间
        maxWait: 60000
        # 配置连接超时时间
        connectTimeout: 30000
        # 配置网络超时时间
        socketTimeout: 60000
        # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        timeBetweenEvictionRunsMillis: 60000
        # 配置一个连接在池中最小生存的时间，单位是毫秒
        minEvictableIdleTimeMillis: 300000
        # 配置一个连接在池中最大生存的时间，单位是毫秒
        maxEvictableIdleTimeMillis: 900000
        # 配置检测连接是否有效
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        webStatFilter:
          enabled: true
        statViewServlet:
          enabled: true
          # 设置白名单，不填则允许所有访问
          allow:
          url-pattern: /druid/*
          # 控制台管理用户名和密码
          login-username: ruoyi
          login-password: 123456
        filter:
          stat:
            enabled: true
            # 慢SQL记录
            log-slow-sql: true
            slow-sql-millis: 1000
            merge-sql: true
          wall:
            config:
              multi-statement-allow: true
```

* 创建configMap

```sh
kubectl create configmap ruoyi-admin-config --from-file=/home/app/application-k8s.yaml
kubectl describe configmap/ruoyi-admin-config

# 删除cm
kubectl delete cm ruoyi-admin-config
```

* 部署后端服务
  * vim /home/app/svc-ruoyi-admin.yaml

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: ruoyi-admin
  labels:
    app: ruoyi-admin
spec:
  replicas: 1
  selector:
    matchLabels:
      app: ruoyi-admin
  template:
    metadata:
      labels:
        app: ruoyi-admin
    spec:
      containers:
        - name: ruoyi-admin
          image: 192.168.56.102:5000/ruoyi-admin:v3.8
          ports:
            - containerPort: 8080
          volumeMounts:
            # springBoot启动时，在jar包所在位置的config目录下查找配置文件
            # jar包所在的位置就是dockerfile中WORKDIR定义的目录，即/app/ruoyi
            - mountPath: /app/ruoyi/config
              name: config
            # 使用application-k8s.yaml作为配置文件
            # 启动命令如下: java -jar ruoyi-admin.jar --spring.profiles.active=k8s
          args: ["--spring.profiles.active=k8s"]
      volumes:
        - name: config
          configMap:
            name: ruoyi-admin-config
---
apiVersion: v1
kind: Service
metadata:
  name: ruoyi-admin
spec:
  type: ClusterIP
  selector:
    app: ruoyi-admin
  ports:
    - port: 8080
      targetPort: 8080
```

* 部署后端服务

```sh
# 部署后端服务
kubectl apply -f /home/app/svc-ruoyi-admin.yaml

# 查看日志
kubectl logs ruoyi-admin-xxx

# 删除部署
kubectl delete -f /home/app/svc-ruoyi-admin.yaml
```



### 部署前端

* #### nginx配置文件

> mkdir -p /home/app/conf && vim /home/app/conf/nginx.conf

```properties
server {
    listen       80;
    server_name  localhost;
    charset utf-8;

    location / {
        # dockerfile中WORKDIR目录
        root   /app/ruoyi-ui;
        try_files $uri $uri/ /index.html;
        index  index.html index.htm;
    }

    location /prod-api/ {
        proxy_set_header Host $http_host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header REMOTE-HOST $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        # 后端service的DNS
        proxy_pass http://ruoyi-admin:8080/;
    }

    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
        root   html;
    }
}
```

* 创建configMap

```sh
kubectl create configmap ruoyi-ui-config --from-file=/home/app/conf/nginx.conf
kubectl describe configmap/ruoyi-ui-config

# 删除cm
kubectldelete cm ruoyi-ui-config
```



* svc-ruoyi-ui.yaml

> vim /home/app/svc-ruoyi-ui.yaml

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: ruoyi-ui
  labels:
    app: ruoyi-ui
spec:
  replicas: 1
  selector:
    matchLabels:
      app: ruoyi-ui
  template:
    metadata:
      labels:
        app: ruoyi-ui
    spec:
      containers:
        - name: ruoyi-ui
          image: 192.168.56.102:5000/ruoyi-ui:v3.8
          ports:
            - containerPort: 80
          volumeMounts:
            - mountPath: /etc/nginx/conf.d
              name: config
      volumes:
        - name: config
          configMap:
            name: ruoyi-ui-config
            items:
              - key: nginx.conf
                path: default.conf
---
apiVersion: v1
kind: Service
metadata:
  name: ruoyi-ui
spec:
  type: NodePort
  selector:
    app: ruoyi-ui
  ports:
    - port: 80
      targetPort: 80
      nodePort: 30080
```

* 部署前端服务

```sh
# 运行
kubectl apply -f /home/app/svc-ruoyi-ui.yaml

# 删除
kubectl delete -f /home/app/svc-ruoyi-ui.yaml

# 查看pod状态
kubectl describe pod ruoyi-ui-7d6cc64f-94fx5

# 查看pod日志
kubectl log pod-name

# 查看pod信息
kubectl describe pod pod-name
```



### 启动顺序控制

* svc-ruoyi-admin.yaml

> 后端服务启动需要等mysql和reids服务就绪后才启动

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: ruoyi-admin
  labels:
    app: ruoyi-admin
spec:
  replicas: 1
  selector:
    matchLabels:
      app: ruoyi-admin
  template:
    metadata:
      labels:
        app: ruoyi-admin
    spec:
      initContainers:
        - name: wait-for-mysql
          image: bitnami/mysql:8.0.31-debian-11-r0
          env:
            - name: MYSQL_ROOT_PASSWORD
              value: "123456"
          command:
            - sh
            - -c
            - |
              set -e
              maxTries=10
              while [ "$$maxTries" -gt 0 ] \
                    && ! mysqladmin ping --connect-timeout=3 -s \
                                    -hdb-mysql-primary -uroot -p$$MYSQL_ROOT_PASSWORD
              do 
                  echo 'Waiting for MySQL to be available'
                  sleep 5
                  let maxTries--
              done
              if [ "$$maxTries" -le 0 ]; then
                  echo >&2 'error: unable to contact MySQL after 10 tries'
                  exit 1
              fi
        - name: wait-for-redis
          image: bitnami/redis:7.0.5-debian-11-r7
          env:
            - name: REDIS_PASSWORD
              value: "123456"
          command:
            - sh
            - -c
            - |
              set -e
              maxTries=10
              while [ "$$maxTries" -gt 0 ] \
                    && ! timeout 3 redis-cli -h redis-master -a $$REDIS_PASSWORD ping
              do 
                  echo 'Waiting for Redis to be available'
                  sleep 5
                  let maxTries--
              done
              if [ "$$maxTries" -le 0 ]; then
                  echo >&2 'error: unable to contact Redis after 10 tries'
                  exit 1
              fi
      containers:
        - name: ruoyi-admin
          image: 192.168.56.102:5000/ruoyi-admin:v3.8
          ports:
            - containerPort: 8080
          volumeMounts:
            # springBoot启动时，在jar包所在位置的config目录下查找配置文件
            # jar包所在的位置就是dockerfile中WORKDIR定义的目录，即/app/ruoyi
            - mountPath: /app/ruoyi/config
              name: config
            # 使用application-k8s.yaml作为配置文件
            # 启动命令如下: java -jar ruoyi-admin.jar --spring.profiles.active=k8s
          args: ["--spring.profiles.active=k8s"]
      volumes:
        - name: config
          configMap:
            name: ruoyi-admin-config
---
apiVersion: v1
kind: Service
metadata:
  name: ruoyi-admin
spec:
  type: ClusterIP
  selector:
    app: ruoyi-admin
  ports:
    - port: 8080
      targetPort: 8080
```



* svc-ruoyi-ui.yaml

> 前端应用ruoyi-ui需要等待后端服务ruoyi-admin就绪之后再启动

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: ruoyi-ui
  labels:
    app: ruoyi-ui
spec:
  replicas: 1
  selector:
    matchLabels:
      app: ruoyi-ui
  template:
    metadata:
      labels:
        app: ruoyi-ui
    spec:
      initContainers:
        - name: wait-for-ruoyi-admin
          image: nginx:1.22
          command:
            - sh
            - -c
            - |
              until curl -m 3 ruoyi-admin:8080 
              do
                echo waiting for ruoyi-admin;
                sleep 5;
              done
      containers:
        - name: ruoyi-ui
          image: 192.168.56.102:5000/ruoyi-ui:v3.8
          ports:
            - containerPort: 80
          volumeMounts:
            - mountPath: /etc/nginx/conf.d
              name: config
      volumes:
        - name: config
          configMap:
            name: ruoyi-ui-config
            items:
              - key: nginx.conf
                path: default.conf
---
apiVersion: v1
kind: Service
metadata:
  name: ruoyi-ui
spec:
  type: NodePort
  selector:
    app: ruoyi-ui
  ports:
    - port: 80
      targetPort: 80
      nodePort: 30080
```

### Ingress

* ruoyi-ingress.yaml

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: ruoyi-ingress
spec:
  rules:
    - http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: ruoyi-ui
                port:
                  number: 80
```

* 主机域名

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: ruoyi-ingress
spec:
  rules:
    #类似于nginx的虚拟主机配置
    - host: "front.ruoyi.com"
      http:
        paths:
          - pathType: Prefix
            path: "/"
            backend:
              service:
                name: ruoyi-ui
                port:
                  number: 80
    - host: "backend.ruoyi.com"
      http:
        paths:
          - pathType: Prefix
            path: "/"
            backend:
              service:
                name: ruoyi-admin
                port:
                  number: 8080
```

* 部署ingress

```sh
kubectl apply -f /home/app/ruoyi-ingress.yaml

# 查看ingress
kubectl describe ingress ruoyi-ingress
```



## 问题

* 编译时报错`failed to solve with frontend dockerfile.v0: failed to create LLB definition: unexpected status code [manifests latest]: 403 Forbidden`

  * 修改配置`‪C:\Users\admin\.docker\daemon.json`或者`%userprofile%\.docker\daemon.json`

    ```json
    "features": {
       "buildkit": false
    }
    ```

    

* 删除none镜像

  * ```sh
    # linux
    docker rmi $(docker images | grep "^<none>" | awk "{print$3}")
    
    # windows powershell
    docker rmi $(docker images --filter "dangling=true" -q)
    
    # windows
    for /f "tokens=3" %i in ('docker images ^| findstr "^<none>"') do docker rmi %i
    ```

    

* 通过helm运行redis报错

  ```bash
  Warning  Failed     4m7s                 kubelet            Failed to pull image "docker.io/bitnami/redis:7.2.4-debian-12-r12": rpc error: code = Unknown desc = failed to pull and unpack image "docker.io/bitnami/redis:7.2.4-debian-12-r12": failed to resolve reference "docker.io/bitnami/redis:7.2.4-debian-12-r12": failed to do request: Head "https://registry-1.docker.io/v2/bitnami/redis/manifests/7.2.4-debian-12-r12": read tcp 10.0.2.15:47874->54.236.113.205:443: read: connection reset by peer
    Warning  Failed     3m18s                kubelet            Failed to pull image "docker.io/bitnami/redis:7.2.4-debian-12-r12": rpc error: code = Unknown desc = failed to pull and unpack image "docker.io/bitnami/redis:7.2.4-debian-12-r12": failed to resolve reference "docker.io/bitnami/redis:7.2.4-debian-12-r12": failed to do request: Head "https://registry-1.docker.io/v2/bitnami/redis/manifests/7.2.4-debian-12-r12": read tcp 10.0.2.15:38780->54.227.20.253:443: read: connection reset by peer
  ```

  * 原因是docker服务未启动：systemctl restart docker
