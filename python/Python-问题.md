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

# 升级pip版本，切换到安装目录执行命令
python.exe -m pip install --upgrade pip
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

## python常见问题

```python
# 查看已安装组件列表
pip list

# 查看安装的组件版本
pip show 组件名称
pip show xxx --files

# 查看组件所有版本
pip install pandas==

# 安装指定组件
pip install pandas
# 指定版本安装
pip install pandas=1.5.2
# 指定最小版本
pip install 'SomePackage>=1.0.4'
# 重新安装
pip install -U pandas=x.x.x

# 查看有更新的组件
pip list --outdated

# 升级包
pip install --upgrade 组件名

# 卸载
pip uninstall 组件名
# 一次性卸载多个
pip uninstall -y pandas easyocr
```



### 问题

#### 1.依赖问题

> 1.依赖报错问题，经常出现依赖不兼容的问题，需要调整依赖版本

```bash
ERROR: pip's dependency resolver does not currently take into account all the packages that are installed. This behaviour is the source of the following dependency conflicts.
paddlepaddle-gpu 2.0.0 requires numpy<=1.19.3,>=1.13; python_version >= "3.5" and platform_system == "Windows", but you have numpy 1.24.1 which is incompatible.
```

> 卸载后重装

```sh
pip3 uninstall numpy
pip3 install numpy==1.19.2
```

> 继续报错

```sh
ERROR: pip's dependency resolver does not currently take into account all the packages that are installed. This behaviour is the source of the following dependency conflicts.
scipy 1.10.0 requires numpy<1.27.0,>=1.19.5, but you have numpy 1.19.2 which is incompatible.
pandas 1.5.2 requires numpy>=1.20.3; python_version < "3.10", but you have numpy 1.19.2 which is incompatible.
```

> 卸载掉pandas后重新安装

```sh
pip uninstall pandas
```



