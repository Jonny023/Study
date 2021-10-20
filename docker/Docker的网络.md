## Docker的网络

### 一、网络基础知识

https://zhuanlan.zhihu.com/p/65226634

* URL：Uniform Resource Locator(统一资源定位符)
* IP地址：IP协议提供的一种统一地址格式。它是一种逻辑地址，用来标识网络中的一个个主机，在本地局域网是唯一的
* 子网掩码：子网掩码只有一个作用，就是将某个IP地址划分成网络地址和主机地址两部分；子网掩码不能单独存在，它必须结合IP地址一起使用。它的长度和IP地址一样也是32位，左边连续的1代表网络号，右边连续的0代表主机位。如：255.255.255.0 表示IP地址前24位是网络号，后8位是主机号，则该网络最多支持256-2台主机，主机号不能全是0或全是1
* 网关：网关的IP地址是具有路由功能的设备的IP地址。如果一个网络中的主机发现数据包的目标主机不在本地网络中，就把数据包转发给它自己的网关，再由网关转发给目标网络的网关；目标网络的网关再转发给目标主机
* DNS：域名服务器(Domain Name Server)。DNS就是进行域名解析的服务器
* 端口号：端口包括物理端口和逻辑端口。物理端口是用于连接物理设备之间的接口；逻辑端口是逻辑上用于区分服务的端口。TCP/IP协议中的端口就是逻辑端口，通过不同的逻辑端口来区分不同的服务。
  * 公认端口(Well-Known Ports) ：不可他用（0-1023）
  * 注册端口(Registered Ports) ：可用于分配给用户进程或服务（1025-49151）
* DHCP服务器：DHCP指的是由服务器控制一段IP地址范围，客户机登录服务器时就可以自动获得服务器分配的IP地址和子网掩码。提升地址的使用率
* MAC地址：全球唯一标识网卡的地址；但通常可修改。网络层设备(如路由器)通过IP地址进行操作；数据链路层设备(如交换机)通过MAC地址来操作；IP和MAC的这种映射关系由ARP(Address Resolution Protocol，地址解析协议)完成

### 二、网络常用命令

1. 查看IP地址

   ~~~shell
   # windows
   ipconfig
   # linux 
   ifconfig
   ip addr
   ~~~

2. 网络连通性测试

   ~~~shell
   # ping命令（测试IP）
   ping www.baidu.com
   
   # telnet命令（测试端口）
   telnet www.baidu.com 80
   
   # traceroute（路径探测跟踪）
   ## tracepath(linux)
   tracepath www.baidu.com
   ## tracert.exe(windows)
   tracert.exe www.baidu.com
   
   # curl命令（请求web服务）
   ## 参考文档：http://www.ruanyifeng.com/blog/2019/09/curl-reference.html
   curl www.baidu.com
   ~~~



### 三、Docker 网络命令

~~~shell
# 查看docker 网络
docker network ls

# 查看网络详情
docker network inspect ${ID}

# 查看桥接情况
## centos7 使用brctl命令（yum install -y bridge-utils）
brctl show
## centos8以上
bridge link show
~~~



### 四、Docker Bridge 网络

1. 创建自定义的bridge网络

   ~~~shell
   # 创建bridge网络
   ## -d bridge:使用bridge驱动
   ## mybridge:新创建网络的名称
   docker network create 
   	-d bridge 
   	mybridge
   
   # 创建网络时，也可以指定网关和子网
   docker network create 
   	-d bridge 
   	--gateway 172.100.0.1 
   	--subnet 172.100.0.0/16
   	mybridge2
   
   # 可查看更多选项
   docker network create --help
   ~~~

2. 使用指定的网络

   ~~~shell
   # 创建容器时使用指定网络
   docker run -d 
   	--network mybridge 
   	--name mymongo5 
   	-e MONGO_INITDB_ROOT_USERNAME=admin 
   	-e MONGO_INITDB_ROOT_PASSWORD=123456 
   	-v /opt/test2/mongo/data:/data/db 
   	-v /opt/test2/mongo/config:/data/configdb 
   	-p 27021:27017 
   	mongo:5.0.2
   
   # 为已有的容器连接指定网络(一个容器可连接多个网络)
   ## mymongo5创建时已连接mybridge，再连接默认的bridge网络
   docker network connect bridge mymongo5
   # 查看容器详情，确认是否连接多个网络
   docker container inspect mymongo5
   ~~~

3. 为容器移除指定网络

   ~~~shell
   # 为容器移除指定网络
   ## mymongo5移除网络inspect
   docker network disconnect bridge mymongo5
   ~~~

4. 自定义的bridge网络，带了域名解析的能力，即通过容器名可访问对应容器

### 五、Docker Host 网络

* host网络就是使用宿主机的网络，不可以创建，在启动容器时可以指定使用host网络，会使用宿主机的网络端口

~~~shell
# 创建容器时使用host网络,此时使用宿主机网络端口，不能再映射端口
## 因为是占用的宿主机端口，因此宿主机端口已使用的时候，容器创建会失败
docker run -d 
	--network host 
	--name mymongo5 
	-e MONGO_INITDB_ROOT_USERNAME=admin 
	-e MONGO_INITDB_ROOT_PASSWORD=123456 
	-v /opt/test2/mongo/data:/data/db 
	-v /opt/test2/mongo/config:/data/configdb  
	mongo:5.0.2
~~~



### 六、网络命名空间



