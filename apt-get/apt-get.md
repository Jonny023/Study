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

