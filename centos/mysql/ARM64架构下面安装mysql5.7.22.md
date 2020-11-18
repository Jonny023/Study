# [ARM64架构下面安装mysql5.7.22](https://www.cnblogs.com/ming-4/p/11690816.html)

### 关键字

* `centos arm64|aarch64 mysql` 

### 安装前准备

```bash
cat /etc/yum.repos.d/CentOS-Base.repo
```

* 备份yum源

```bash
cp /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup
```

* 编辑`yum`，替换$releasever为centos版本，我的系统是7，正则替换：`:%s/$releasever/7/g`

```bash
vim /etc/yum.repos.d/CentOS-Base.repo
#非编辑模式输入命令全局替换
:%s/$releasever/7/g
#保存退出
wq
```

[安装教程](https://www.cnblogs.com/ming-4/p/11690816.html)