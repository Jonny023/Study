## wsl安装

```sh
# 手动设置dns
114.114.114.114
8.8.8.8

wsl --set-default-version 1
wsl --install -d Ubuntu
用户名密码：docker

# 设置root用户密码
sudo passwd root

# 切换到root用户
su - root

# 查看磁盘空间
wsl df -h

# 查看当前安装的系统
wsl -l -v

# 如何连接
wsl -d ubuntu
wsl

wsl  --shutdown
```

### wsl无法打开
https://www.bilibili.com/read/cv33678828/?jump_opus=1

> 下载后放到Windows\System32目录下

```sh
NoLsp.exe C:\windows\system32\wsl.exe
```



### 报错Windows containers are not supported by your Windows versionCheck documentation for minimum requirements
#### 家庭版伪装为专业版
```sh
REG ADD "HKEY_LOCAL_MACHINE\software\Microsoft\Windows NT\CurrentVersion" /v EditionId /T REG_EXPAND_SZ /d Professional /F
```

### windows家庭版开启typora-v

### [https://blog.csdn.net/qq_51116518/article/details/137199643](https://blog.csdn.net/qq_51116518/article/details/137199643)



### 开启守护进程

```sh
"c:\Program Files\Docker\Docker\DockerCli.exe" -SwitchDaemon
"c:\Program Files\Docker\Docker\DockerCli.exe" -SwitchWindowsEngine

"C:\Program Files\Docker\Docker\resources\dockerd.exe"
```

### 普通用户执行报错，需要以管理员身份运行
> error during connect: This error may indicate that the docker daemon is not running.: Get "http://%2F%2F.%2Fpipe%2Fdocker_engine/v1.24/containers/json": open //./pipe/docker_engine: Access is denied.


### 配置国内镜像
https://blog.csdn.net/Lyon_Nee/article/details/124169099
https://cr.console.aliyun.com/cn-hangzhou/instances/mirrors

### 文件路径
```
subl C:\ProgramData\Docker\config\daemon.json
```

* 配置加速镜像地址


```sh
{
  "builder": {
    "gc": {
      "defaultKeepStorage": "20GB",
      "enabled": true
    }
  },
  "experimental": false,
  "features": {
    "buildkit": true
  },
  "registry-mirrors": [
    "https://docker.mirrors.ustc.edu.cn",
    "https://registry.docker-cn.com",
    "http://hub-mirror.c.163.com",
    "https://5vfekjm3.mirror.aliyuncs.com"
  ]
}
```

### 重启服务

```sh
net stop com.docker.service
net start com.docker.service
```


### 以脚本方式启动

```sh
del "C:\ProgramData\docker\docker.pid"
taskkill /F /IM dockerd.exe

"C:\Program Files\Docker\Docker\resources\dockerd.exe"

pause
```



### 编译时出现错误

> no matching manifest for windows/amd64 10.0.22631 in the manifest list entries

```sh
# subl C:\ProgramData\Docker\config\daemon.json
# subl %programdata%\docker\config\daemon.json
"experimental": true
```

* 重启服务

```sh
"C:\Program Files\Docker\Docker\resources\dockerd.exe"
```



#### 报错2

> failed to start service utility VM (createreadwrite): kernel 'C:\Program Files\Linux Containers\kernel' not found

```
cd c:\Program Files

mkdir "Linux Containers"

cd "Linux Containers"

curl -OutFile release.zip https://github.com/linuxkit/lcow/releases/download/v4.14.35-v0.3.9/release.zip

# 将zip解压到c:\Program Files\Linux Containers目录下

```

## windows无法通过wsl安装ubuntu

```sh
# 安装wsl
# https://learn.microsoft.com/zh-cn/windows/wsl/install-on-server

# 通过powershell下载ubuntu
Invoke-WebRequest -Uri https://aka.ms/wslubuntu2004 -OutFile Ubuntu.appx -UseBasicParsing

# 
Add-AppxPackage .\app_name.appx
$userenv = [System.Environment]::GetEnvironmentVariable("Path", "User")
[System.Environment]::SetEnvironmentVariable("PATH", $userenv + ";C:\Users\admin\Ubuntu", "User")

# 执行安装
ubuntu.exe
```

### 卸载wsl

```sh
wsl --unregister Ubuntu
```

