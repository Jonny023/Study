## git pull或者push时报错



```bash
fatal: unable to access 'https://github.com/Jonny023/Study.git/': Failed to connect to github.com port 443 after 21067 ms: Couldn't connect to server
```



* 关闭代理

```sh
# 关闭https验证
 git config --global http.sslVerify false
 
# 关闭代理
git config --global --unset http.proxy
git config --global --unset https.proxy
```