## Jdk安装

[下载地址](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)

* 安装`wget`命令

```bash
yum -y install wget
```

* 上传并解压

```bash
tar -zxvf jdk-8u161-linux-x64.tar.gz
```

* 修改配置文件`vi /etc/profile`或者`vi ~/.base_profile`

```bash
export JAVA_HOME=/usr/local/jdk/jdk1.8.0_301
export PATH=$PATH:$JAVA_HOME/bin
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
```

* 应用配置

```bash
source /etc/profile

source ~/.base_profile
```

* 测试

```bash
java -version
javac -version
```