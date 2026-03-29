# virtualbox虚拟机扩容

* VirtulBox磁盘扩容，进入virtualbox所在目录执行

```sh
VBoxManage modifymedium disk "D:\vm\vm1\vm1\vm1.vdi" --resize 102400
```

* 虽然磁盘已经扩到 100G 了，但 `/dev/sda2` 还只有 39G，所以只需要把 sda2 扩满，然后扩 LVM

> sda1为引导分区

```sh
[root@vm1 ~]# lsblk
NAME            MAJ:MIN RM  SIZE RO TYPE MOUNTPOINT
sda               8:0    0  100G  0 disk
├─sda1            8:1    0    1G  0 part /boot
└─sda2            8:2    0   39G  0 part
  ├─centos-root 253:0    0   37G  0 lvm  /
  └─centos-swap 253:1    0    2G  0 lvm
sr0              11:0    1 58.5M  0 rom
sr1              11:1    1   51M  0 rom

```

* 执行如下步骤

```bash
# growpart 可以直接把已有分区扩展到磁盘剩余空间
yum install -y cloud-utils-growpart

# 把 /dev/sda 的第 2 个分区，也就是 /dev/sda2 扩展到整块磁盘剩余空间
growpart /dev/sda 2

# 因为 /dev/sda2 是 LVM 的物理卷（PV），分区虽然变大了，但 LVM 还不知道
# /dev/sda2 已经从 39G 变成 99G 了，请重新识别容量
pvresize /dev/sda2

# 把 LVM 里面所有剩余空间全部分配给根分区 centos-root 是你的根目录 /
lvextend -l +100%FREE /dev/mapper/centos-root

# 把文件系统扩展到新的 LVM 大小
xfs_growfs /
```

* 再次查看

```bash
lsblk
df -h
```

