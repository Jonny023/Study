## virtualbox复制系统后启动无法上网

```sh

# 查看网卡
nmcli con show
# 删除指定网卡（网卡名称跟ip addr查询不一致时，可以删除后重新配置）
nmcli con delete net3

# 明明有网卡，但是没有配置信息，需要执行命令生成配置
nmcli con add con-name enp0s8 type ethernet ifname enp0s8

# ip addr比对前面nmcli conn show里面的网卡信息
# 如果没有配置信息，可通过上面一条生成配置文件
ip addr

# 修改完成，重启网卡
service network restart

# 无法解析域名
vim /etc/resolve.conf
nameserver 8.8.8.8
nameserver 114.114.114.114

# net模式不能设置static,虚拟机一般用nat+host only模式，host only网卡可以设置static静态ip
# 主要配置：复制的虚拟机网卡配置的uuid需要修改下
BOOTPROTO=static
ONBOOT=yes
IPADDR=192.168.70.6
NETMASK=255.255.255.0
GATEWAY=192.168.70.1
DNS1=8.8.8.8
DNS2=114.114.114.114
```
