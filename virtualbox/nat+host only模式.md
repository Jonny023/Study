# nat+host only模式

> host only模式有时关机重启后看不到host only网卡对应的ip，需要在
> 工具》网络》网卡（手动配置网卡），指定网关，ipv4地址为网关》dhcp服务器里面，服务器地址也是网关
> 修改网卡地址后需要关机，然后再开机，直接重启没效果


## 上网设置

```sh

# 添加新网卡后，在/etc/sysconfig/network-scripts/目录下找不到网卡对应配置，可以初始化配置
nmcli c add type ethernet con-name enp0s8 ifname enp0s8

# 激活
nmcli c up enp0s8

# 查询指定网卡
ip a show enp0s8

# 配置网关(NAT网卡)
ONBOOT=YES
DNS1=114.114.114.114
DNS2=8.8.8.8

```
