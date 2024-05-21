## 抓包



```sh
docker run -itd --name ng1 -p 9001:80 nginx:1.22
docker run -itd --name ng2 -p 9002:80 nginx:1.22

docker exec -it ng1 bash
# 安装网络工具
apt install -y ethtool

# 或者一步到位
docker exec ng1 apt update && docker exec ng1 apt install -y ethtool
docker exec ng2 apt update && docker exec ng2 apt install -y ethtool

# 查看主机arp缓存表
arp -n

# 清空arp
arp -d -a

# 查看容器网卡对应宿主机里面的网桥网卡，下面的编号11对应的就是宿主里面的vethxxx网卡
docker exec ng1 ethtool -S eth0
NIC statistics:
     peer_ifindex: 11

# 容器内没有ip a命令
docker exec ng1 apt install -y iproute2
docker exec ng2 apt install -y iproute2

# 容器内没有arp命令
docker exec ng1 apt install -y net-tools
docker exec ng2 apt install -y net-tools

# 查看容器内部ip
docker exec ng1 ip a
docker exec ng2 ip a

# 安装tcpdump
yum install -y tcpdump

# 容器内部没有ping命令
apt-get install -y iputils-ping


# 查看ng1如何与ng2进行通信
# 查看ng1容器内ip
docker exec ng1 ip a
10: eth0@if11: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default
    link/ether 02:42:ac:11:00:03 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 172.17.0.3/16 brd 172.17.255.255 scope global eth0
       valid_lft forever preferred_lft forever


# 先查看ng1绑定的宿主网卡
docker exec ng1 ethtool -S eth0
NIC statistics:
     peer_ifindex: 11

# 查看编号11对应的网卡信息：vethca5e409
[root@vm1 ~]# ip a
11: vethca5e409@if10: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue master docker0 state UP group default
    link/ether e6:a7:c5:b2:5b:6f brd ff:ff:ff:ff:ff:ff link-netnsid 2
    inet6 fe80::e4a7:c5ff:feb2:5b6f/64 scope link
       valid_lft forever preferred_lft forever

# 监听网卡
tcpdump -i vethca5e409

# 进入容器ng2
docker exec -it ng2 bash
# 在容器ng2中访问ng1（ng1容器内部ip为172.17.0.3）
 curl 172.17.0.3


# 返回查看tcpdump监听信息
tcpdump: verbose output suppressed, use -v or -vv for full protocol decode
listening on vethca5e409, link-type EN10MB (Ethernet), capture size 262144 bytes
05:41:55.746801 ARP, Request who-has 172.17.0.3 tell 172.17.0.4, length 28
05:41:55.746814 ARP, Reply 172.17.0.3 is-at 02:42:ac:11:00:03 (oui Unknown), length 28
05:41:55.746826 IP 172.17.0.4.46358 > 172.17.0.3.http: Flags [S], seq 3794405826, win 29200, options [mss 1460,sackOK,TS val 2163822 ecr 0,nop,wscale 7], length 0
05:41:55.746835 IP 172.17.0.3.http > 172.17.0.4.46358: Flags [S.], seq 678902066, ack 3794405827, win 28960, options [mss 1460,sackOK,TS val 2163822 ecr 2163822,nop,wscale 7], length 0
05:41:55.746844 IP 172.17.0.4.46358 > 172.17.0.3.http: Flags [.], ack 1, win 229, options [nop,nop,TS val 2163822 ecr 2163822], length 0
05:41:55.746866 IP 172.17.0.4.46358 > 172.17.0.3.http: Flags [P.], seq 1:75, ack 1, win 229, options [nop,nop,TS val 2163822 ecr 2163822], length 74: HTTP: GET / HTTP/1.1
05:41:55.746868 IP 172.17.0.3.http > 172.17.0.4.46358: Flags [.], ack 75, win 227, options [nop,nop,TS val 2163822 ecr 2163822], length 0
05:41:55.746943 IP 172.17.0.3.http > 172.17.0.4.46358: Flags [P.], seq 1:239, ack 75, win 227, options [nop,nop,TS val 2163822 ecr 2163822], length 238: HTTP: HTTP/1.1 200 OK
05:41:55.746948 IP 172.17.0.4.46358 > 172.17.0.3.http: Flags [.], ack 239, win 237, options [nop,nop,TS val 2163822 ecr 2163822], length 0
05:41:55.746955 IP 172.17.0.3.http > 172.17.0.4.46358: Flags [P.], seq 239:854, ack 75, win 227, options [nop,nop,TS val 2163822 ecr 2163822], length 615: HTTP
05:41:55.746960 IP 172.17.0.4.46358 > 172.17.0.3.http: Flags [.], ack 854, win 247, options [nop,nop,TS val 2163822 ecr 2163822], length 0
05:41:55.747043 IP 172.17.0.4.46358 > 172.17.0.3.http: Flags [F.], seq 75, ack 854, win 247, options [nop,nop,TS val 2163822 ecr 2163822], length 0
05:41:55.748646 IP 172.17.0.3.http > 172.17.0.4.46358: Flags [F.], seq 854, ack 76, win 227, options [nop,nop,TS val 2163825 ecr 2163822], length 0
05:41:55.748665 IP 172.17.0.4.46358 > 172.17.0.3.http: Flags [.], ack 855, win 247, options [nop,nop,TS val 2163825 ecr 2163825], length 0
05:42:00.755453 ARP, Request who-has 172.17.0.4 tell 172.17.0.3, length 28
05:42:00.755515 ARP, Reply 172.17.0.4 is-at 02:42:ac:11:00:04 (oui Unknown), length 28

```

