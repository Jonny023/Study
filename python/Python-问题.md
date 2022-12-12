## Python

```python
# pip源配置 https://newsn.net/say/pip-mirror.html
# 清华大学 https://pypi.tuna.tsinghua.edu.cn/simple/
# 阿里云 https://mirrors.aliyun.com/pypi/simple/
# 豆瓣 https://pypi.douban.com/simple/
# 东软 https://mirrors.neusoft.edu.cn/pypi/web/simple/

# 指定源安装pydev-pycharm
pip install pydevd-pycharm~=222.3739.30 -i https://mirrors.aliyun.com/pypi/simple/

# 配置全局依赖仓库
pip config set global.index-url https://pypi.tuna.tsinghua.edu.cn/simple/

# 全局配置pip下载源
pip config set global.index-url https://mirrors.aliyun.com/pypi/simple/

# 查看依赖版本
pip install lxml==

# 安装指定版本
pip install lxml==4.9.0
```



### no.1

> error: Microsoft Visual C++ 14.0 or greater is required. Get it with "Microsoft C++ Build Tools": https://visualstudio.microsoft.com/visual-cpp-build-tools/

* [前往下载](https://visualstudio.microsoft.com/visual-cpp-build-tools/)，下载好后，安装时勾选 “使用c++的桌面开发”，安装完成后重启电脑

### no.2

> 安装lxml报错

[手动下载](https://www.lfd.uci.edu/~gohlke/pythonlibs/#lxml)

```python
# 执行安装
python -m pip install lxml-4.9.0-cp311-cp311-win_amd64.whl

# 安装报错，自行换版本测试，如果报错了，就换一个版本执行安装
python -m pip install lxml-4.9.0-pp38-pypy38_pp73-win_amd64.whl
Looking in indexes: https://pypi.tuna.tsinghua.edu.cn/simple/
ERROR: lxml-4.9.0-pp38-pypy38_pp73-win_amd64.whl is not a supported wheel on this platform.
```

