# docker Jenkins

## 1.拉取镜像

```shell
docker pull jenkins/jenkins:lts
```

## 2.运行

```shell
# 创建jenkins目录
mkdir /data/jenkins

# 启动容器
docker run -d --name jenkins -p 8081:8080 -v /data/jenkins:/home/jenkins jenkins/jenkins:lts

# 查看启动日志
docker logs jenkins

# docker方式安装需要先进入容器
docker exec -it jenkins bash
# 查看初始密码
cat /var/jenkins_home/secrets/initialAdminPassword

# 访问web管理端,第一次访问需要设置密码
localhost:8081
```

