[vagrant下载](https://www.vagrantup.com/)

> 下载安装vagrant，安装完成需要重启生效

[官方镜像库](https://app.vagrantup.com/boxes/search)

```shell
# 配置虚拟机环境
setx VAGRANT_HOME "G:\developer\vms\os1" /M

# 初始化虚拟机，会在用户目录下创建一个Vagrantfile
# 想要保存到哪里，就到哪个路径下执行命令
vagrant init centos/7

# 如果默认下载慢，可以用第三方镜像：https://mirrors.ustc.edu.cn/centos-cloud/centos/7/vagrant/x86_64/images/
vagrant init centos7 https://mirrors.ustc.edu.cn/centos-cloud/centos/7/vagrant/x86_64/images/CentOS-7.box

# 删除虚拟机
vagrant destroy

# 启动虚拟机
vagrant up

# 关机
vagrant halt

# 查看虚拟机状态
vagrant status

# 连接虚拟机
vagrant ssh

# 重启虚拟机
vagrant reload

# 配置虚拟机固定ip,因为默认用的是nat【端口转发】，访问不方便，建议配置host-only的ip
# 编辑文件Vagrantfile，配置ip部分，具体网段请查看ipconfig【虚拟网卡网段】
config.vm.network "private_network", ip: "192.168.56.111"

# 编辑配置配置dns和网关
vi /etc/sysconfig/network-scripts/ifcfg-eth1
GATEWAY=192.168.56.1
DNS1=114.114.114.114
DNS2=8.8.8.8

# 重启网卡
systemctl restart network
service network restart

# 默认用户名和密码：vagrant
# 对应root用户的密码：vagrant


# 默认无法通过其他客户端远程连接虚拟机
vagrant ssh-config

# 查看配置PasswordAuthentication no，需要将no改为yes
G:\developer\vms\os1>vagrant ssh-config
Host default
  HostName 127.0.0.1
  User vagrant
  Port 2222
  UserKnownHostsFile /dev/null
  StrictHostKeyChecking no
  PasswordAuthentication no
  IdentityFile G:/developer/vms/os1/.vagrant/machines/default/virtualbox/private_key
  IdentitiesOnly yes
  LogLevel FATAL
  
# 修改root密码
sudo passwd root

# 编辑配置
sudo vi /etc/ssh/sshd_config

PermitRootLogin yes

# 重启服务
sudo systemctl restart sshd


# 修改时区
rm -rf /etc/localtime
ln -s /usr/share/zoneinfo/Asia/Shanghai /etc/localtime




# 更新yum源
## 1.进入yum源配置目录

cd /etc/yum.repos.d

## 2. 备份原来的yum源，便于恢复，改啥都得留个备份，万一玩儿了还能恢复

mv CentOS-Base.repo CentOS-Base.repo.bk

## 3. 下载新的CentOS-Base.repo 到/etc/yum.repos.d/
## 更新为阿里云的源
wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo
## 更新为163的源
wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.163.com/.help/CentOS6-Base-163.repo
## 更新为搜狐的源
wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.sohu.com/help/CentOS-Base-sohu.repo

## 4、运行yum makecache生成缓存
yum clean all
yum makecache



# 时间同步，如果centos时间和windows时间不一致
date -R

# （查看时间状态）
timedatectl

#  (将时钟调整为与本地时钟一致, 0 为设置为 UTC 时间)
timedatectl set-local-rtc 1

#  （设置系统时区为上海）
timedatectl set-timezone Asia/Shanghai

# 安装ntp
yum -y install ntp

# 同步
ntpdate ntp1.aliyun.com
```



## docker

[官方安装文档](https://docs.docker.com/engine/install/centos/)

* 安装docker

```sh
# 卸载旧版docker
sudo yum remove docker \
    docker-client \
    docker-client-latest \
    docker-common \
    docker-latest \
    docker-latest-logrotate \
    docker-logrotate \
    docker-engine
    
# 安装工具
sudo yum install -y yum-utils

# 配置地址
sudo yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo
    
# 安装插件
sudo yum install docker-ce docker-ce-cli containerd.io docker-compose-plugin
```

* docker服务

```sh
# 启动docker
sudo systemctl start docker

# 开机自启
sudo systemctl enable docker

# 禁用开机自启
sudo systemctl disable docker
```

### 配置阿里云docker镜像加速

[阿里云官网](https://www.aliyun.com/)

> 登录阿里云官网：控制台》左侧菜单找到容器服务》容器镜像服务》镜像加速器》CentOS

```sh
# 创建目录
sudo mkdir -p /etc/docker

# 配置镜像加速器地址
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://5vfekjm3.mirror.aliyuncs.com"]
}
EOF


sudo systemctl daemon-reload
# 重启服务
sudo systemctl restart docker

# 容器开机自启
sudo docker update redis --restart=always

# 禁用容器开机自启
sudo docker update redis --restart=no
```

