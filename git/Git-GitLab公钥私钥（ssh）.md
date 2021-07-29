# GitLab公钥私钥（ssh）

> 解决每次提交代码都要输入用户名密码的问题

# 一、单个ssh

### 1.生产公钥和私钥对

```shell
ssh-keygen -t rsa -C 'e-lijing6@geely.com'
```

### 2.复制用户目录下`id_rsa.pub`文件内容

```shell
%HOMEPATH%\.ssh\id_rsa.pub

type %HOMEPATH%\.ssh\id_rsa.pub
```

### 3.打开gitlab配置公钥

```
Profile Settings-->SSH Keys--->Add SSH Key
```

## 二、多个ssh

### 1.生成秘钥

```shell
ssh-keygen -t rsa -C 'test@xx.com' -f %HOMEPATH%\.ssh\gitlab-rsa
ssh-keygen -t rsa -C 'test@xx.com' -f %HOMEPATH%\.ssh\github-rsa
```



```yaml
# gitlab
Host gitlab.com
HostName gitlab.com
PreferredAuthentications publickey
IdentityFile ~/.ssh/gitlab_id-rsa
# github
Host github.com
HostName github.com
PreferredAuthentications publickey
IdentityFile ~/.ssh/github_id-rsa
  
# 配置文件参数
# Host : Host可以看作是一个你要识别的模式，对识别的模式，进行配置对应的的主机名和ssh文件
# HostName : 要登录主机的主机名
# User : 登录名
# IdentityFile : 指明上面User对应的identityFile路径
```