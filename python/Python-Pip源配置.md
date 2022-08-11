[参考地址](https://newsn.net/say/pip-config-whereis.html)

```sh
# pip源配置 https://newsn.net/say/pip-mirror.html
清华大学 https://pypi.tuna.tsinghua.edu.cn/simple/
阿里云 https://mirrors.aliyun.com/pypi/simple/
豆瓣 https://pypi.douban.com/simple/
东软 https://mirrors.neusoft.edu.cn/pypi/web/simple/

# 全局配置pip下载源
pip config set global.index-url https://mirrors.aliyun.com/pypi/simple/

# 安装pydev-pycharm
pip install pydevd-pycharm~=222.3739.30 -i https://mirrors.aliyun.com/pypi/simple/
```
