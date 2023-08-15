## 重装系统后，如何让以前安装过的mysql生效？

#### 安装mysql服务

* 从MS-DOS窗口进入目录`D:\dev_tools\mysql-5.7.20-winx64\bin`，运行如下命令：

```shell
cd /d D:\dev_tools\mysql-5.7.20-winx64\bin
mysqld --install mysql --defaults-file=D:\dev_tools\mysql-5.7.20-winx64\my.ini
```

#### 查看日志

```shell
mysqld --console
```

## 安装时报错

*  无法定位程序输入点`fesetround`于动态链接库`MSVCR120.dll`上
* 解决方法：
  [下载](http://download.microsoft.com/download/b/e/8/be8a5444-cdd8-4d3d-ae09-a0979b05aee3/vcredist_x64.exe)

#### 启动mysql数据库

```shell
net start mysql
```

#### 停止数据库

```bash
net stop mysql
```

#### 移除服务

```bash
mysqld --remove mysql
```
