# 推送报错

> fatal: unable to access 'https://github.com/jonny023/study.git/': OpenSSL SSL_read: Connection was reset, errno 10054

```shell
# 取消ssl验证
git config --global http.sslVerify "false"

# 根据项目修改
git config lfs.https://github.com/jonny023/study.git/info/lfs.locksverify false

# 重新推送
git push origin master
```

