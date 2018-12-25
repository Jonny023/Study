## 重装系统后，如何让以前安装过的mysql生效？

#### 安装mysql服务

* 从MS-DOS窗口进入目录D:\dev_tools\mysql-5.7.20-winx64\bin，运行如下命令：

```
mysqld --install mysql --defaults-file=D:\dev_tools\mysql-5.7.20-winx64\my.ini
```

## 安装时报错
*  无法定位程序输入点fesetround于动态链接库MSVCR120.dll上
* 解决方法：
  [下载](http://download.microsoft.com/download/b/e/8/be8a5444-cdd8-4d3d-ae09-a0979b05aee3/vcredist_x64.exe)

#### 启动mysql数据库

```
net start mysql
```

#### 停止数据库

```
net stop mysql
```

#### 移除服务

```
mysqld --remove mysql
```
