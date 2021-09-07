# 推送报错

> fatal: unable to access 'https://abc.com/demo.git/': OpenSSL SSL_read: Connection was reset, errno 10054

```shell
# 取消ssl验证
git config --global http.sslVerify "false"

# 重新推送
git push origin master
```

