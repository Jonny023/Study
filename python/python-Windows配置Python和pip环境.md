# Windows配置Python和pip环境

> 下载zip压缩包环境，如：`python-3.9.9-embed-amd64.zip`

* [官网地址](https://www.python.org/downloads/)

## 1.配置环境变量

> 配置好后验证：`python --version`
> dos输入python打开了应用商店

```shell
# 通过where命令找到python.exe路径，删除其他路径的文件
C:\Users\admin>where python
C:\Users\admin\AppData\Local\Microsoft\WindowsApps\python.exe
D:\devtools\python-3.10.6\python.exe
```

```shell
path D:\devtools\python-3.9.9
```

## 2.下载pip

* [pip下载](https://bootstrap.pypa.io/get-pip.py)

## 3.运行脚本

> 建议将get-pip.py脚本放到python根目录，执行命令后会在根目录下生成两个目录：Lib和Scripts

```shell
python get-pip.py
```

## 4.添加配置

> 在`python3X._pth`中添加如下配置

```shell
Lib\site-packages
```

## 5.验证pip

```shell
pip --version
pip3 --version
```

