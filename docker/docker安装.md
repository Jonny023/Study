# docker安装

[原文地址](https://juejin.cn/post/6844903790630404104)

### 方式1：使用脚本安装

#### 1. 确保 yum 包更新到最新。

```shell
$ sudo yum update
```

#### 2. 切换到root用户

```shell
$ sudo su -
```

#### 3. 执行脚本安装

```shell
$ curl -sSL https://get.docker.com/ | sh
```

#### 4.启动docker

```shell
systemctl start docker

systemctl stop docker
```

#### 5.开机启动

```shell
systemctl enable docker

systemctl disable docker
```

#### 6.查看日志

```
docker logs -f -t --tail 行数 容器名/id
```



### 方式2：使用yum安装【~~找不到yum源~~】

#### 1. 查看版本是否满足要求

使用`uname -r`命令查看系统内核版本，看是否为3.10以上

```shell
[root@hostname ~]# uname -r
3.10.0-514.26.2.el7.x86_64
```

#### 2. 移除旧版本docker(如果安装过)

```shell
sudo yum remove docker \
                docker-client \
                docker-client-latest \
                docker-common \
                docker-latest \
                docker-latest-logrotate \
                docker-logrotate \
                docker-selinux \
                docker-engine-selinux \
                docker-engine
```

#### 3. 安装一些必要的系统工具

```shell
sudo yum install -y yum-utils device-mapper-persistent-data lvm2
```

#### 4. 添加软件源信息

```shell
sudo yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

wget https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo -O /etc/yum.repos.d/docker-ce.repo
```

#### 5. 更新 yum 缓存

```shell
sudo yum makecache fast
```

#### 6. 安装 Docker-ce：

```shell
sudo yum -y install docker-ce
```

#### 7. 启动 Docker 后台服务

```shell
sudo systemctl start docker
```

#### 8. 测试运行 hello-world

```shell
[root@hostname ~]# docker run hello-world
```

运行结果如下，则表示docker安装成功

![img](https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2019/2/21/16910b45fe4893c4~tplv-t2oaga2asx-watermark.awebp)



# 运行第一个容器

刚刚测试启动的hello-world就是一个容器，不过没有什么实际作用。接下来运行一个httpd的容器，来直观感受一下。

```shell
docker run -d -p 80:80 httpd 
```

运行过程如下：

```shell
[root@izwz9alpqga9jjum6tmmkyz ~]# docker run -d -p 80:80 httpd
Unable to find image 'httpd:latest' locally
latest: Pulling from library/httpd
6ae821421a7d: Pull complete 
0ceda4df88c8: Pull complete 
24f08eb4db68: Pull complete 
ddf4fc318081: Pull complete 
fc5812428ac0: Pull complete 
Digest: sha256:214019bfc77677ac1f0c86b3a96e2b91600de7e2224f195b446cb13572cebb6b
Status: Downloaded newer image for httpd:latest
b554654b25376a48d41b1e4df53703cd7891fdbdcbc497c8f4d449000c4b9913
```

查看一下，容器是否在运行状态。可以看到STATUS为Up 2 seconds，说明在2秒钟前启动的。

```shell
[root@localhost ~]# docker ps -a
CONTAINER ID   IMAGE         COMMAND              CREATED          STATUS                     PORTS                               NAMES
8a3653621887   httpd         "httpd-foreground"   49 seconds ago   Created                                                        cool_haibt
c2ecbfc40cf9   httpd         "httpd-foreground"   55 seconds ago   Up 54 seconds              0.0.0.0:80->80/tcp, :::80->80/tcp   sleepy_brahmagupta
84779e019598   hello-world   "/hello"             3 minutes ago    Exited (0) 3 minutes ago                                       ecstatic_torvalds
2664a60b09fb   hello-world   "/hello"             5 minutes ago    Exited (0) 5 minutes ago                                       jovial_brahmagupta

```

可以通过浏览器访问主机的80端口，可以看到Apache服务器已经安装好了

上面的docker命令实际上做了一下几部工作：

1. 在本地查找httpd的镜像
2. 本地没有镜像，从公共仓库下载httpd最新的镜像文件
3. 启动httpd容器，将本地的80端口映射到容器的80端口

# 镜像加速

我们启动容器都需要一个镜像，但是docker官方的公共镜像仓库(https://hub.docker.com/)服务器在国外，下载速度可能会比较慢。还好国内有很多厂商提供了镜像下载的服务，我们需要在docker的配置文件中配置国内镜像的地址。

centos系统可以直接执行下面的命令配置加速器。

```shell
curl -sSL https://get.daocloud.io/daotools/set_mirror.sh | sh -s http://f1361db2.m.daocloud.io
```

这个是`https://www.daocloud.io/mirror`该网站提供的镜像加速服务。



![img](https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2019/3/5/1694df9e5ae28074~tplv-t2oaga2asx-watermark.awebp)



除了这种方法以外，我们也可以自己手动添加任意的加速镜像地址：

在配置文件`/etc/docker/daemon.json`中添加如下数据，该文件不存在的话，则新建一个。其中中括号里就是加速镜像的地址。

```shell
{
  "registry-mirrors": ["https://registry.docker-cn.com"]
}
```

## 加速配置

* 创建目录

```shell
sudo mkdir -p /etc/docker
```

* 编辑文件

```shell
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://docker.mirrors.ustc.edu.cn", "https://ustc-edu-cn.mirror.aliyuncs.com","https://7uuu3esz.mirror.aliyuncs.com","https://780urbjd.mirror.aliyuncs.com"]
}
EOF
```

* 重启docker

```shell
sudo systemctl daemon-reload
sudo systemctl restart docker
```

