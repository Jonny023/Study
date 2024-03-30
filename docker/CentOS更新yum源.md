# 更新yum源

* 创建一个yum_update.sh脚本

```sh
# 备份
yum install -y wget
cd /etc/yum.repos.d
mkdir -p repo_bak
mv *.repo repo_bak/

# 查看yum源码 https://mirrors.aliyun.com/repo/
wget http://mirrors.aliyun.com/repo/Centos-7.repo

# 清理缓存
yum clean all
yum makecache

yum install -y epel-release

yum repolist enabled
yum repolist all

# 更新yum
yum -y update
```
