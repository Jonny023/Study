# 下载安装篇

[参考地址]([https://minalz.cn/#/soft/jenkins%E5%AE%89%E8%A3%85](https://minalz.cn/#/soft/jenkins安装))

## 1.下载

```shell
wget -P /usr/local/env https://mirrors.aliyun.com/jenkins/war-stable/2.303.2/jenkins.war

# 最新版
wget -P /usr/local/env http://pub.mirrors.aliyun.com/jenkins/war-stable/latest/jenkins.war
```

## 2.启动

```shell
mkdir -p /var/log/jenkins

# 后台运行
nohup java -DJENKINS_HOME=/data/jenkins -jar /usr/local/env/jenkins.war --httpPort=9001 -Dhudson.util.ProcessTree.disable=true > /var/log/jenkins/jenkins.log 2>&1 &

# 查看日志
tail -200f /var/log/jenkins/jenkins.log
```

## 3.配置密码

> 查看默认密码: `cat /data/jenkins/secrets/initialAdminPassword`，这个密码存放路径在启动日志里面可以看到，根据实际情况定

```
tail -200f /var/log/jenkins/jenkins.log

Jenkins initial setup is required. An admin user has been created and a password generated.
Please use the following password to proceed to installation:

8641f7f2a7a1492e8a16a1ee5da6fc90

This may also be found at: /data/jenkins/secrets/initialAdminPassword
```

## 4.访问9001端口

> http://192.168.56.101:9001，登录密码用`cat /data/jenkins/secrets/initialAdminPassword`看到的密码，第一次访问需要安装插件，自定义安装完成后可设置用户名和密码，统一用`admin`

## 5.安装推荐插件

> **为什么不自定义安装？**第一次用不熟悉，里面水太深把握不住，容易翻车。

![](.\install_img\install_plugins.png)



# Gitee篇

![](.\install_img\gitee_plugin.png)









# 时区配置

> 登录web端口，`设置`-`User Defined Time Zone`-`Asia/Shanghai`，保存重启服务

