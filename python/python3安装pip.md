## python3安装pip

```sh
cd /d %PYTHON%

# 下载pip脚本
curl https://bootstrap.pypa.io/get-pip.py -o %PYTHON%\get-pip.py

# 安装
python get-pip.py

# 配置环境变量,path中添加
path %PYTHON%\Scripts
```

> python3中下载并安装了pip，但是命令行还是无法使用，则需要进行如下配置：
> 1.修改python安装目录下的pythonxxx._pth文件,如`python312._pth`
>
> 2.在该文件中添加一行`Lib\site-packages`

