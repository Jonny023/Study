## git配置

* 配置用户和邮箱

```sh
git confg --global user.name "Jonny"

git config --global user.email "342418262@qq.com"
```

* git bash配置

```sh
# 一直回车生成秘钥，生成的文件在用户目录下
ssh-keygen -t rsa -C "342418262@qq.com"

# 查看秘钥
cat ~/.ssh/id_rsa.pub
```



### 马云秘钥配置

> 登录[码云](gitee.com)，设置》SSH公钥，标题自定义，公钥用前面生成的即可。

