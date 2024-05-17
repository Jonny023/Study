# dbeaver企业版破解

[参考](https://blog.csdn.net/qq_34373197/article/details/137087074)


## 下载

* [下载地址](https://dbeaver.com/files/dbeaver-ee-latest-win32.win32.x86_64.zip)

## 安装JDK(略过)

## 下载破解文件

* [dbeaver-agent-latest.zip](https://wwb.lanzouw.com/iHRdly1nu9c)

## 配置

* 解压并删除根目录下的jre目录

* 修改根目录下的配置
  * 在根目录下找到dbeaver.ini，注释并配置jdk
  ```sh
  # -startup
  #plugins/org.jkiss.dbeaver.launcher_1.0.0.202404011634.jar
  # --launcher.library
  # plugins/org.eclipse.equinox.launcher.win32.win32.x86_64_1.2.800.v20231003-1442
  
  -vm
  D:/dev_tools/jdk-17.0.1/bin/server/jvm.dll
  ```

  * 在dbeaver末尾添加破解配置

  ```sh
  -Dlm.debug.mode=true
  -javaagent:d:/devtools/dbeaver-agent.jar
  ```

  ## 启动