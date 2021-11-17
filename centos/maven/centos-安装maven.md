# centos7安装maven

## 1.下载

```shell
wget -P /usr/local/env https://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz

cd /usr/local/env

tar -xf apache-maven-3.6.3-bin.tar.gz -C ./

mkdir -p /usr/local/env/maven3.6/.m2

```

* 显示行号:将53行代码复制至55行(:set nu显示行号)

## 2.修改仓库位置

```
<localRepository>${MAVEN_HOME}/.m2</localRepository>
```

## 3.配置镜像

```xml
<mirror>
    <id>alimaven</id>
    <mirrorOf>central</mirrorOf>
    <name>aliyun maven</name>
    <url>https://maven.aliyun.com/repository/central</url>
</mirror>
<mirror> 
    <id>aliyun-maven</id> 
    <mirrorOf>*</mirrorOf> 
    <name>aliyun maven</name> 
    <url>http://maven.aliyun.com/nexus/content/groups/public</url> 
</mirror>
```

## 4.配置环境变量

* 编辑文件

  > vim /etc/profile

* 在配置文件中加入

  > export MAVEN_HOME=/usr/local/env/maven3.6
  > export PATH=$PATH:$MAVEN_HOME/bin

* 让配置生效

  > source /etc/profile

* 验证

  > mvn -version