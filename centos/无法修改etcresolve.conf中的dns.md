# centos7无法修改/etc/resolve.conf中的dns

> 局域网内其他主机无法访问
>
> 修改完后，重启网卡修改文件内容恢复了

## 方法1

> 设置网卡ip为静态ip并指定dns

```java
DEVICE=eth0
BOOTPROTO=none                 //DHCP自动或者手动获取ip
HWADDR=00:0c:29:f4:63:83
IPV6INIT=yes
NM_CONTROLLED=yes
ONBOOT=yes
TYPE=Ethernet
UUID="17348826-0ae4-40e1-9b43-4ddc3fd5dae4"
IPADDR=192.168.1.200        //ip
NETMASK=255.255.255.0            
GATEWAY=192.168.1.1         //网关
DNS1=192.168.1.2           //1修改DNS
DNS1=192.168.1.2           //2修改DNS
USERCTL=no
PEERDNS=no                  //3默认为yes，修改为no之后则不会在重启之后更新resolv
```



## 方法2

> 锁定resolve文件，锁定后即使是root用户也无法修改，必须解锁后才能继续修改

```shell
# 锁定
chattr +i /etc/resolve.conf

# 解锁
chattr -i /etc/resolve.conf
```

# 修改完成后重启网卡

```shell
service network restart
```

