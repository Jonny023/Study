# virtualbox问题

### [virtualbox历史版本](https://www.virtualbox.org/wiki/Download_Old_Builds_7_0)

### 问题1
* 安装virtualbox（版本7.0.16）后，在虚拟机里面安装了centos7，通过ssh工具远程连接虚拟机就蓝屏报错：

> ssh连接virtualbox虚拟机蓝屏报错：错误代码：PAGE_FAULT_IN_NONPAGED_AREA VBoxNetAdp6.sys

* [官网解决](https://www.virtualbox.org/ticket/22045)
	* 官方说需要将版本降低到7.0.14，果然我降低后重新安装，远程连接不报错了

### 问题2

> 到 virtualbox 的安装目录下找到 drivers\vboxdrv 文件夹。右击 VBoxDrv.inf 文件，选择安装，然后重启 virtualbox 即可。

### 问题3
> windows11安装VirtualBox7，解决报错： needs the Microsoft Visual C++2019
https://learn.microsoft.com/zh-CN/cpp/windows/latest-supported-vc-redist?view=msvc-170
