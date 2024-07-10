# Docker部署Jenkins

> jdk用容器内部的jdk，maven、docker共用宿主机的环境

### 配置maven

```sh
# 创建目录
mkdir -p /etc/devtools/maven/.m2
cd /etc/devtools/maven

# 下载压缩包
wget https://archive.apache.org/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz -O /etc/devtools/maven/apache-maven-3.6.3-bin.tar.gz

# 解压文件
tar -xvf apache-maven-3.6.3-bin.tar.gz

# 编辑配置
vim /etc/devtools/maven/apache-maven-3.6.3/conf/settings.xml
# 配置本地仓库存放地址
<localRepository>/etc/devtools/maven/.m2</localRepository>
# 配置阿里云加速镜像
<mirror>
    <id>aliyunmaven</id>
    <mirrorOf>*</mirrorOf>
    <name>阿里云公共仓库</name>
    <url>https://maven.aliyun.com/repository/public</url>
</mirror>

# 配置环境变量
# vim ~/.bash_profile
MAVEN_HOME=/etc/devtools/maven/apache-maven-3.6.3
export PATH=${MAVEN_HOME}/bin:${PATH}

# 刷新环境变量
source ~/.bash_profile
```



### 运行

```sh
# 拉取镜像，jdk11这种是自带了jdk的，无需额外配置
# docker pull jenkins/jenkins:lts-jdk17
docker pull jenkins/jenkins:jdk11

# 创建jenkins本地存放路径，并设置权限
mkdir -p /etc/devtools/jenkins/data
chmod -R 777 /etc/devtools/jenkins/data

# 运行容器，将宿主主机的maven以及docker映射到容器，容器和宿主共用环境，docker镜像也能在宿主里面看到
docker run \
-p 8080:8080 \
-p 5030:5000 \
--restart=always \
-e TZ="Asia/Shanghai" \
-v /etc/localtime:/etc/localtime \
-v /etc/devtools/jenkins/data:/var/jenkins_home \
-v /usr/bin/docker:/usr/bin/docker \
-v /etc/docker:/etc/docker:rw \
-v /var/run/docker.sock:/var/run/docker.sock:rw \
-v /etc/devtools/maven/apache-maven-3.6.3:/usr/local/maven:rw \
-v /etc/devtools/maven/.m2:/etc/devtools/maven/.m2:rw \
--name jenkins_jdk11 \
-d jenkins/jenkins:jdk11

# 由于jenkins容器内部用的是宿主的环境，docker没有权限，需要配置下权限
docker exec -u root -it jenkins_jdk11 bash
# 查看用户信息
id
# 修改宿主机上 socket 的 owner 为 id=1000 的用户
chown 1000 /var/run/docker.sock
# 或者直接修改文件权限
chmod 666 /var/run/docker.sock

# 然后接口可以在容器内部执行docker命令了
docker images
```



### 访问

* [http://localhost:8080](http://localhost:8080)

> 初始密码位置：/var/jenkins_home/secrets/initialAdminPassword

```sh
# 方式1 可以进入容器查看
docker exec -it jenkins_jdk11 bash
# 查看密码
cat /var/jenkins_home/secrets/initialAdminPassword

# 方式2 因为映射了目录，直接查看数据目录
cat /etc/devtools/jenkins/data/secrets/initialAdminPassword
```



### 配置jenkins容器的maven

> 由于jenkins里面使用的宿主主机的maven，宿主主机的本地仓库配置的是/etc/devtools/maven/.m2/，容器里面没有这个路径所以需要创建，并且需要权限

```sh
# 以管理员进入容器
docker exec -it -u root jenkins_jdk11 bash

# 创建本地仓库
mkdir -p /etc/devtools/maven/.m2/

# 设置权限
chmod -R 777 /etc/devtools/maven/.m2/
```



### Dockerfile

> 项目根目录下创建Dockerfile配置文件

```dockerfile
FROM openjdk:8-jdk-alpine
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY target/spring-boot-cicd-demo-0.0.1-SNAPSHOT.jar /home/app.jar
ENTRYPOINT [ "java","-jar", "/home/app.jar" ]
```



### Execute shell

```sh
docker build -f Dockerfile -t springboot-cicd-demo:v1.0 .
```

