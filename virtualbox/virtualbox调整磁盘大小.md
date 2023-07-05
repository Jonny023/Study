## virtualbox调整磁盘大小

* (参考)[https://betheme.net/news/txtlist_i26982v.html?action=onClick]

* (参考1)[https://blog.csdn.net/weixin_40542512/article/details/101105433]
* (参考2)[https://blog.csdn.net/UKerLee/article/details/126498151]
* (磁盘扩容)[https://www.bilibili.com/video/av674836852/?vd_source=610e097b4d28ca7a9353304c7307c4a9]

### 方式1

> 关闭虚拟机，在【管理》虚拟介质管理》选中虚拟硬盘》调整大小】，设置好后需要重启系统通过[gparted](https://gparted.org/)工具或者系统`sudo fdisk /dev/sda`进行配置

### 方式2

> 若方式1不行，只能用方式2，或者从新创建磁盘，安装新系统

```sh
VBoxManage modifymedium "临时vdi虚拟硬盘.vdi" --resize 扩容后大小(单位MB)

# 进入virtualbox安装目录
cd /d G:\VirtualBox

#设置为50GB 50*1024MB
VBoxManage modifymedium "G:\vm\vm01\vm01.vdi" --resize 51200
vboxmanage modifymedium disk "G:\vm\vm01\vm01.vdi" --resize 51200


# 报错
#G:\VirtualBox>VBoxManage modifyhd "G:\vm\vm01\vm01.vdi" --resize 51200
#0%...
#Progress state: VBOX_E_NOT_SUPPORTED
#VBoxManage.exe: error: Failed to resize medium
#VBoxManage.exe: error: Resizing to new size 53687091200 is not yet supported for medium 'G:\vm\vm01\vm01.vdi'
#VBoxManage.exe: error: Details: code VBOX_E_NOT_SUPPORTED (0x80bb0009), component MediumWrap, interface IMedium
#VBoxManage.exe: error: Context: "enum RTEXITCODE __cdecl handleModifyMedium(struct HandlerArg *)" at line 816 of file VBoxManageDisk.cpp

#创建虚拟机
VBoxManage.exe createhd -filename "G:\vm\vm02\vm02.vdi" -size 151920 -format VDI -variant Standard

#克隆虚拟机
VBoxManage clonehd "G:\vm\vm01\vm01.vdi" "G:\vm\vm02\vm02.vdi" --existing

VBoxManage modifymedium "G:\vm\vm01\vm01.vdi" --resize 51200

# 分区参考：https://www.pudn.com/news/6353aeec2aaf6043c9527e80.html
#创建分区
sudo fdisk /dev/sda

sudo fdisk -l

# 查看磁盘情况
lsblk 
ls /dev/sd*
```

