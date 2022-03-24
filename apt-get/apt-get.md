# apt-get

## 更新源

```shell
mv /etc/apt/sources.list /etc/apt/sources.list.bak

echo "deb http://mirrors.163.com/debian/ jessie main non-free contrib" >> /etc/apt/sources.list &&
echo "deb http://mirrors.163.com/debian/ jessie-proposed-updates main non-free contrib" >> /etc/apt/sources.list &&
echo "deb-src http://mirrors.163.com/debian/ jessie main non-free contrib" >> /etc/apt/sources.list &&
echo "deb-src http://mirrors.163.com/debian/ jessie-proposed-updates main non-free contrib" >> /etc/apt/sources.list &&
#更新安装源
apt-get update
```

## 安装vim

```shell
apt-get install -y vim
```

## 问题？

```shell
Reading package lists... Done
E: The repository 'http://mirrors.163.com/debian jessie-proposed-updates Release' does not have a Release file.
N: Updating from such a repository can't be done securely, and is therefore disabled by default.
N: See apt-secure(8) manpage for repository creation and user configuration details.
W: GPG error: http://mirrors.163.com/debian jessie Release: The following signatures couldn't be verified because the public key is not available: NO_PUBKEY 7638D0442B90D010 NO_PUBKEY CBF8D6FD518E17E1
E: The repository 'http://mirrors.163.com/debian jessie Release' is not signed.
N: Updating from such a repository can't be done securely, and is therefore disabled by default.
N: See apt-secure(8) manpage for repository creation and user configuration details.
```

### 解决方法

```
apt-key adv --keyserver keyserver.ubuntu.com --recv-keys  CBF8D6FD518E17E1

apt-key adv --keyserver keyserver.ubuntu.com --recv-keys  7638D0442B90D010
```



**[参考地址](https://learnku.com/articles/26066)**

## 更新apt源

> /etc/apt/sources.list，修改前先备份原文件：mv /etc/apt/sources.list /etc/apt/sources.list.bak

```properties
# 默认注释了源码镜像以提高 apt update 速度，如有需要可自行取消注释
deb http://mirrors.tuna.tsinghua.edu.cn/debian/ stretch main contrib non-free
# deb-src http://mirrors.tuna.tsinghua.edu.cn/debian/ stretch main contrib non-free
deb http://mirrors.tuna.tsinghua.edu.cn/debian/ stretch-updates main contrib non-free
# deb-src http://mirrors.tuna.tsinghua.edu.cn/debian/ stretch-updates main contrib non-free
deb http://mirrors.tuna.tsinghua.edu.cn/debian/ stretch-backports main contrib non-free
# deb-src http://mirrors.tuna.tsinghua.edu.cn/debian/ stretch-backports main contrib non-free
deb http://mirrors.tuna.tsinghua.edu.cn/debian-security stretch/updates main contrib non-free
# deb-src http://mirrors.tuna.tsinghua.edu.cn/debian-security stretch/updates main contrib non-free

# 默认注释了源码镜像以提高 apt update 速度，如有需要可自行取消注释
deb http://mirrors.tuna.tsinghua.edu.cn/debian/ buster main contrib non-free
# deb-src http://mirrors.tuna.tsinghua.edu.cn/debian/ buster main contrib non-free
deb http://mirrors.tuna.tsinghua.edu.cn/debian/ buster-updates main contrib non-free
# deb-src http://mirrors.tuna.tsinghua.edu.cn/debian/ buster-updates main contrib non-free
deb http://mirrors.tuna.tsinghua.edu.cn/debian/ buster-backports main contrib non-free
# deb-src http://mirrors.tuna.tsinghua.edu.cn/debian/ buster-backports main contrib non-free
deb http://mirrors.tuna.tsinghua.edu.cn/debian-security buster/updates main contrib non-free
# deb-src http://mirrors.tuna.tsinghua.edu.cn/debian-security buster/updates main contrib non-free
```

> 如果是docker需要在外部编辑好后拷贝到容器内，因为容器内部不支持vi命令

```shell
# 粘贴上面的内容到这个文件
vi sources.list

# 拷贝到容器中
docker cp sources.list 容器id:/etc/apt/sources.list

# 进入容器执行更新
docker exec -it 容器id bash

# 更新
apt update
# 或者
apt-get update

apt install vim
```



