## keepalived +nginx(docker-compose)主备高可用



|     节点          | ip              | port |
| ----------------- | --------------- | ---- |
| vip（vrrp虚拟ip） | 192.168.110.220 |      |
| master（主）      | 192.168.110.51  | 80   |
| backup（备）      | 192.168.110.140 | 80   |

### 1.master

#### 1.1 准备

> 在master主机上执行

```sh
mkdir -p /data/nginx & cd /data/nginx
mkdir -p {conf,conf.d,html}
docker run -itd --name master nginx:1.16.1
docker cp master:/etc/nginx/nginx.conf ./conf
docker rm -f master

# 创建配置
vim conf.d/default.conf
[root@localhost nginx]# cat conf.d/default.conf 
server {
  listen 80;
  server_name localhost;

  location / {
    root /usr/share/nginx/html;
    index index.html;
  }
}

# 创建html
[root@localhost nginx]# cat html/index.html 
192.168.110.51
```



#### 1.2 docker-compose.yml

```yaml
version: "3"
services:
   master:
     container_name: ng-master
     image: nginx:1.16.1
     ports:
       - 80:80   
     volumes:
       - ./html:/usr/share/nginx/html
       - ./conf/nginx.conf:/etc/nginx/nginx.conf:ro
       - ./conf.d:/etc/nginx/conf.d:ro
       - ./logs:/var/log/nginx
     restart: always
```

#### 1.3 启动

```sh
docker-compose up -d
```

#### 1.4 测试

```sh
curl localhost
```





### 2.backup

#### 2.1 准备

> 在backup主机上执行

```sh
mkdir -p /data/nginx & cd /data/nginx
mkdir -p {conf,conf.d,html}
docker run -itd --name backup nginx:1.16.1
docker cp backup:/etc/nginx/nginx.conf ./conf
docker rm -f backup

# 创建配置
vim conf.d/default.conf
[root@localhost nginx]# cat conf.d/default.conf 
server {
  listen 80;
  server_name localhost;

  location / {
    root /usr/share/nginx/html;
    index index.html;
  }
}

# 创建html
[root@localhost nginx]# cat html/index.html 
192.168.110.140
```



#### 2.2 docker-compose.yml

```yaml
version: "3"
services:
   master:
     container_name: ng-backup
     image: nginx:1.16.1
     ports:
       - 80:80   
     volumes:
       - ./html:/usr/share/nginx/html
       - ./conf/nginx.conf:/etc/nginx/nginx.conf:ro
       - ./conf.d:/etc/nginx/conf.d:ro
       - ./logs:/var/log/nginx
     restart: always
```

#### 2.3 启动

```sh
docker-compose up -d
```

#### 2.4 测试

```sh
curl localhost
```



## keepalived

[参考地址1](https://blog.csdn.net/millery22/article/details/123499744)

[参考地址2](https://cloud.tencent.com/developer/article/1719470)

### 下载

[官网地址](https://www.keepalived.org/download.html)

```sh
wget -O /data/keepalived-2.2.7.tar.gz --no-check-certificate https://www.keepalived.org/software/keepalived-2.2.7.tar.gz
```

### 解压

```sh
cd /data/ && tar -xvf keepalived-2.2.7.tar.gz
```

### 安装

> 默认是读取`/etc/keepalived/keepalived.conf`配置文件

```sh
# 更新yum源
yum update

# 安装依赖环境
yum install gcc build-essential

# 找不到makefile
yum install gcc gcc-c++ autoconf automake

yum -y install zlib zlib-devel openssl openssl-devel pcre pcre-devel

# 配置安装目录
cd /data/keepalived-2.2.7/
./configure --prefix=/data/keepalived --sysconf=/etc

# 编译安装
make && make install

# 创建软链接
ln -s /data/keepalived/sbin/keepalived /sbin/

# 覆盖软链接
ln -snf /data/keepalived/sbin/keepalived /sbin

# 复制运行命令
cp /data/keepalived/sbin/keepalived /etc/init.d
cp /data/keepalived-2.2.7/keepalived/etc/init.d/keepalived /etc/init.d
chkconfig --add keepalived

# 添加服务
chkconfig keepalived on
#启动服务
service keepalived start
# 查看状态
service keepalived status
```

### 配置

> 配置文件是存在`/etc/keepalived/`目录下，配置文件的名称`keepalived.conf`，配置如下：

#### master(192.168.110.51)

* vim /etc/keepalived/keepalived.conf

```bash
! Configuration File for keepalived

global_defs {               # 全局配置    
    router_id KL_MASTER     # 表示运行Keepalived服务器的一个标识，唯一的
}

vrrp_script chk_nginx {
    script "/etc/keepalived/nginx_check.sh"
    interval 2
    weight -20
}

vrrp_instance VI_1 {        # vrrp 实例定义部分
    state MASTER            # 指定keepalived的角色，MASTER为主，BACKUP为备
    interface eth0          # 当前进行vrrp通讯的网络接口卡(当前centos的网卡)
    virtual_router_id 66    # 虚拟路由编号，主从要一致
    priority 200            # 优先级，数值越大，获取处理请求的优先级越高
    advert_int 1            # 检查间隔，默认为1s(vrrp组播周期秒数)
    
    # 如果两节点的上联交换机禁用了组播，则采用vrrp单播通告的方式
    # 本机ip
    unicast_src_ip 192.168.110.51
    unicast_peer {
        # 其他机器ip
        192.168.110.140
    }
    # 设置nopreempt防止抢占资源
    nopreempt
    authentication {
        auth_type PASS      # 设置验证类型和密码，MASTER和BACKUP必须使用相同的密码才能正常通信
        auth_pass 1111
    }
    
    # 与上方nginx运行状况检测呼应
    track_script {
        chk_nginx
    }
    
    virtual_ipaddress {
        192.168.110.220     # 定义虚拟ip，可多设，每行一个
    }
}
```

#### backup(192.168.110.140)

* vim /etc/keepalived/keepalived.conf

```bash
! Configuration File for keepalived

global_defs {               # 全局配置    
    router_id KL_BACKUP     # 表示运行Keepalived服务器的一个标识，唯一的
}

#ng是否运行
vrrp_script chk_nginx {
    script "/etc/keepalived/nginx_check.sh"
    interval 2
    weight -20
}

vrrp_instance VI_1 {        # vrrp 实例定义部分
    state BACKUP            # 指定keepalived的角色，MASTER为主，BACKUP为备
    interface eth0          # 当前进行vrrp通讯的网络接口卡(当前centos的网卡)
    virtual_router_id 66    # 虚拟路由编号，主从要一致
    priority 100            # 优先级，数值越大，获取处理请求的优先级越高
    advert_int 1            # 检查间隔，默认为1s(vrrp组播周期秒数)
    
    # 如果两节点的上联交换机禁用了组播，则采用vrrp单播通告的方式
    # 本机ip
    unicast_src_ip 192.168.110.140
    unicast_peer {
        # 其他机器ip
        192.168.110.51
    }
    # 设置nopreempt防止抢占资源
    nopreempt
    
    authentication {
        auth_type PASS      # 设置验证类型和密码，MASTER和BACKUP必须使用相同的密码才能正常通信
        auth_pass 1111
    }
    
    # 与上方nginx运行状况检测呼应
    track_script {
        chk_nginx
    }
    
    virtual_ipaddress {
        192.168.110.220     # 定义虚拟ip，可多设，每行一个
    }
}
```

### 脚本

> 心跳监听nginx状态： vim /etc/keepalived/nginx_check.sh，主备主机上都要创建脚本

* 安装killall

  ```sh
  yum install psmisc -y
  ```

```sh
#!/bin/bash
A=`ps -C nginx --no-header|wc -l`        
if [ $A -eq 0 ];then
    docker-compose -f /data/nginx/docker-compose.yml up -d #重启nginx
    service keepalived stop
    sleep 2
    if [ `ps -C nginx --no-header|wc -l` -eq 0 ];then       #nginx重启失败，则停掉keepalived服务，进行虚拟ip转移
        #killall keepalived    #杀掉，虚拟ip就会漫游到另一台机器
        service keepalived start
    fi
fi
```

* 添加执行权限：

  ```sh
  chmod +x /etc/keepalived/nginx_check.sh
  ```

  

### 启动

```sh
#/data/keepalived/sbin/keepalived
service keepalived start
```

### 查看ip

> 正常情况下，主节点会多一个192.168.110.220的ip

```sh
[root@localhost bin]# ip a | grep -w inet
    inet 127.0.0.1/8 scope host lo
    inet 192.168.110.51/24 brd 192.168.110.255 scope global dynamic eth0
    inet 192.168.110.220/32 scope global eth0
```



### 验证

* 启动master和backup的nginx和keepalived
* 访问master的80端口
* 停止master的nginx或者keepalived，看看backup主机是否会自动生效
* 访问的时候都通过vrrp(vip)的虚拟ip进行访问

