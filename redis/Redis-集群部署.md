## Redis集群

### 1.docker版

[参考](https://cloud.tencent.com/developer/article/1838120)



#### 1.1 拉取镜像

```
docker pull redis:5.0.5
```

#### 1.2 创建网卡

> 创建6个配置

```sh
docker network create redis --subnet 172.28.0.0/16
```

#### 1.3 创建配置

```
for port in $(seq 1 6); \
do \
mkdir -p /data/redis/node-${port}/conf
touch /data/redis/node-${port}/conf/redis.conf
cat << EOF > /data/redis/node-${port}/conf/redis.conf
port 6379
bind 0.0.0.0
cluster-enabled yes
cluster-config-file nodes.conf
cluster-node-timeout 5000
cluster-announce-ip 172.28.0.1${port}
cluster-announce-port 6379
cluster-announce-bus-port 16379
appendonly yes
EOF
done
```

#### 1.4 配置权限

```sh
chmod -R 755 /data/redis/
```

#### 1.5 运行容器

> 容器端口：6371-6376

```sh
# 容器1
docker run -p 6371:6379 -p 16371:16379 --name redis-1 \
-v /data/redis/node-1/data:/data \
-v /data/redis/node-1/conf/redis.conf:/etc/redis/redis.conf \
-d --net redis --ip 172.28.0.11 redis:5.0.5 redis-server /etc/redis/redis.conf

# 容器2
docker run -p 6372:6379 -p 16372:16379 --name redis-2 \
-v /data/redis/node-2/data:/data \
-v /data/redis/node-2/conf/redis.conf:/etc/redis/redis.conf \
-d --net redis --ip 172.28.0.12 redis:5.0.5 redis-server /etc/redis/redis.conf

# 容器3
docker run -p 6373:6379 -p 16373:16379 --name redis-3 \
-v /data/redis/node-3/data:/data \
-v /data/redis/node-3/conf/redis.conf:/etc/redis/redis.conf \
-d --net redis --ip 172.28.0.13 redis:5.0.5 redis-server /etc/redis/redis.conf

# 容器4
docker run -p 6374:6379 -p 16374:16379 --name redis-4 \
-v /data/redis/node-4/data:/data \
-v /data/redis/node-4/conf/redis.conf:/etc/redis/redis.conf \
-d --net redis --ip 172.28.0.14 redis:5.0.5 redis-server /etc/redis/redis.conf

# 容器5
docker run -p 6375:6379 -p 16375:16379 --name redis-5 \
-v /data/redis/node-5/data:/data \
-v /data/redis/node-5/conf/redis.conf:/etc/redis/redis.conf \
-d --net redis --ip 172.28.0.15 redis:5.0.5 redis-server /etc/redis/redis.conf

# 容器6
docker run -p 6376:6379 -p 16376:16379 --name redis-6 \
-v /data/redis/node-6/data:/data \
-v /data/redis/node-6/conf/redis.conf:/etc/redis/redis.conf \
-d --net redis --ip 172.28.0.16 redis:5.0.5 redis-server /etc/redis/redis.conf
```

#### 1.6 创建集群

```sh
# 进入容器
docker exec -it redis-1 /bin/sh

# 进入容器后，在容器中创建集群
redis-cli --cluster create 172.28.0.11:6379 172.28.0.12:6379 172.28.0.13:6379 172.28.0.14:6379 172.28.0.15:6379 172.28.0.16:6379 --cluster-replicas 1


# 启动redis集群客户端（-c表示集群）
redis-cli -c

# 查看集群信息
cluster info

# 集群创建好之后，11，12，13为主节点，其余为从节点
cluster nodes

# 查看redis版本
redis-server -v
```

#### 1.7 测试

```sh
# 在任意节点添加数据
set user:1 zhangsan

get user:1

# 退出容器
exit

# 停止刚刚添加数据的节点
docker stop redis-1

# 进入其他容器测试
docker exec -it redis-5 sh

# 连接
redis-cli -c

# 获取数据
get user:1
```







### 2.普通安装

> 集群6个节点：3主3从

```sh
127.0.0.1:7000
127.0.0.1:7001
127.0.0.1:7002
127.0.0.1:7003
127.0.0.1:7004
127.0.0.1:7005
```



#### 2.1 下载安装包

```sh
# 创建目录
mkdir -p /data & cd /data

# 下载安装包
wget http://download.redis.io/releases/redis-3.2.9.tar.gz
```

#### 2.2 解压安装

```sh
tar zxvf redis-3.2.9.tar.gz
cd redis-3.2.9
make MALLOC=libc && make && make PREFIX=/data/redis install

# 若失败安装gcc和tcl
yum install gcc && yum install tcl

# 创建软链接
ln -s /data/redis/bin/redis-server /usr/bin/redis-server
ln -s /data/redis/bin/redis-cli /usr/bin/redis-cli
```

#### 2.3 创建集群目录

```sh
mkdir -p /data/redis/cluster/{7000,7001,7002,7003,7004,7005}
```

#### 2.3 copy配置并修改

```sh
cp /data/redis-3.2.9/redis.conf /data/redis/cluster/7000
```

* 修改

  > 编辑配置：vim /data/redis/cluster/7000/redis.conf

```properties
# 端口号
port 7000
# 后台启动
daemonize yes
# 开启集群
cluster-enabled yes
#集群节点配置文件
cluster-config-file nodes-7000.conf
# 集群连接超时时间
cluster-node-timeout 5000
# 进程pid的文件位置
pidfile /var/run/redis-7000.pid
# 开启aof
appendonly yes
# aof文件路径
appendfilename "appendonly-7000.aof"
# rdb文件路径
dbfilename dump-7000.rdb
```

* 拷贝/data/redis-3.2.9/redis.conf /data/redis/cluster/7000到其他端口配置目录并修改端口

```sh
cp -p /data/redis/cluster/7000/redis.conf /data/redis/cluster/7001/redis.conf &
cp -p /data/redis/cluster/7000/redis.conf /data/redis/cluster/7002/redis.conf &
cp -p /data/redis/cluster/7000/redis.conf /data/redis/cluster/7003/redis.conf &
cp -p /data/redis/cluster/7000/redis.conf /data/redis/cluster/7004/redis.conf &
cp -p /data/redis/cluster/7000/redis.conf /data/redis/cluster/7005/redis.conf
```

#### 2.4 创建启动脚本

> 在/data/redis目录下创建start.sh, vi /data/redis/start.sh

```sh
#!/bin/bash
redis-server cluster/7000/redis.conf
redis-server cluster/7001/redis.conf
redis-server cluster/7002/redis.conf
redis-server cluster/7003/redis.conf
redis-server cluster/7004/redis.conf
redis-server cluster/7005/redis.conf
```

```sh
# 运行
sh start.sh

# 查看状态
ps -ef | grep redis

# 关闭
#!/bin/bash
redis-cli -p 7000 shutdown
redis-cli -p 7001 shutdown
redis-cli -p 7002 shutdown
redis-cli -p 7003 shutdown
redis-cli -p 7004 shutdown
redis-cli -p 7005 shutdown
```



#### 2.5 开启集群

> 前面只是开启了6个redis进程而已，它们都还只是独立的状态，还没有组成集群这里我们使用官方提供的工具redis-trib，不过这个工具是用ruby写的，要先安装ruby的环境

```sh
yum install ruby rubygems -y

gem sources --add https://gems.ruby-china.com/ --remove https://rubygems.org/

gem update --system

gem -v

gem sources -l

# 安装gem-redis
gem install redis

gem install redis -v 3.3.5

curl -L  get.rvm.io | bash -s stable

# 参考地址
https://www.dandelioncloud.cn/article/details/1448920032855306241

# 将redis-3.2.9的src目录下的trib复制到相应文件夹
cp redis-3.2.9/src/redis-trib.rb /data/redis/bin/redis-trib

# 创建软链接
ln -s /data/redis/bin/redis-trib /usr/bin/redis-trib

# 创建集群
redis-trib create --replicas 1 127.0.0.1:7000 127.0.0.1:7001 127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005
```



#### 2.6 验证

[参考](https://zhuanlan.zhihu.com/p/391762630)

```sh
redis-cli -c -p 7000

set name zhangsan

get name

exit

# 查看进程id
ps -ef | grep redis
root      3156     1  0 09:19 ?        00:00:00 redis-server 127.0.0.1:7000 [cluster]
root      3160     1  0 09:19 ?        00:00:00 redis-server 127.0.0.1:7001 [cluster]
root      3164     1  0 09:19 ?        00:00:00 redis-server 127.0.0.1:7002 [cluster]
root      3168     1  0 09:19 ?        00:00:00 redis-server 127.0.0.1:7003 [cluster]
root      3172     1  0 09:19 ?        00:00:00 redis-server 127.0.0.1:7004 [cluster]
root      3176     1  0 09:19 ?        00:00:00 redis-server 127.0.0.1:7005 [cluster]
root      7536  2101  0 09:38 pts/0    00:00:00 grep --color=auto redis

# 结束进程，端口号7000
kill 3156

# 检查节点状态
redis-trib check 127.0.0.1:7001

# 发现刚刚的slave节点变成了master节点（M-主节点，S-从节点）
>>> Performing Cluster Check (using node 127.0.0.1:7001)
M: 8f9ebdc3cd6f1c697f58e5ac873ce70468148cb5 127.0.0.1:7001
   slots:5461-10922 (5462 slots) master
   1 additional replica(s)
S: 29a5b856d9f99e203c99dcdfabc6b5c0c7b650eb 127.0.0.1:7004
   slots: (0 slots) slave
   replicates 8f9ebdc3cd6f1c697f58e5ac873ce70468148cb5
S: bbad30f79929eb79af530058ec605b8b8e8b7f10 127.0.0.1:7005
   slots: (0 slots) slave
   replicates 3b2f027156f99c85be08d7558db960600a81188d
M: 2c53e636cb47f1cccbf1cfc93fe9fe5c48ce11f4 127.0.0.1:7003
   slots:0-5460 (5461 slots) master
   0 additional replica(s)
M: 3b2f027156f99c85be08d7558db960600a81188d 127.0.0.1:7002
   slots:10923-16383 (5461 slots) master
   1 additional replica(s)
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.

# 再次启动7000服务，看他会不会升级为Master
redis-server /data/redis/cluster/7000/redis.conf
redis-trib check 127.0.0.1:7000

# 可以看到7000这个服务再次升级为主节点
[root@localhost data]# redis-trib check 127.0.0.1:7000
>>> Performing Cluster Check (using node 127.0.0.1:7000)
M: e19bd5205a3b59c395712dd53cb4a540c8746566 127.0.0.1:7000
   slots: (0 slots) master
   0 additional replica(s)
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[ERR] Not all 16384 slots are covered by nodes

# 模拟两个主节点同时挂掉
[root@localhost data]# ps -ef | grep redis
root      3160     1  0 09:19 ?        00:00:01 redis-server 127.0.0.1:7001 [cluster]
root      3164     1  0 09:19 ?        00:00:01 redis-server 127.0.0.1:7002 [cluster]
root      3168     1  0 09:19 ?        00:00:01 redis-server 127.0.0.1:7003 [cluster]
root      3172     1  0 09:19 ?        00:00:01 redis-server 127.0.0.1:7004 [cluster]
root      3176     1  0 09:19 ?        00:00:01 redis-server 127.0.0.1:7005 [cluster]
root      7736     1  0 09:44 ?        00:00:00 redis-server 127.0.0.1:7000 [cluster]
root      7859  2101  0 09:47 pts/0    00:00:00 grep --color=auto redis
[root@localhost data]# kill 3160
[root@localhost data]# kill 3168

# 检测主节点
[root@localhost data]# redis-trib check 127.0.0.1:7002
>>> Performing Cluster Check (using node 127.0.0.1:7002)
M: 3b2f027156f99c85be08d7558db960600a81188d 127.0.0.1:7002
   slots:10923-16383 (5461 slots) master
   1 additional replica(s)
S: bbad30f79929eb79af530058ec605b8b8e8b7f10 127.0.0.1:7005
   slots: (0 slots) slave
   replicates 3b2f027156f99c85be08d7558db960600a81188d
M: 29a5b856d9f99e203c99dcdfabc6b5c0c7b650eb 127.0.0.1:7004
   slots:5461-10922 (5462 slots) master
   0 additional replica(s)
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[ERR] Not all 16384 slots are covered by nodes.

# 因为两个节点主节点和从节点都挂掉了，原来7001分配的slot现在无节点接管，需要人工介入重新分配slots
[root@localhost data]# redis-cli -c -p 7002
127.0.0.1:7002> get name
(error) CLUSTERDOWN The cluster is down

```



* 添加节点

  ```sh
  redis-trib add-node 127.0.0.1:7006 127.0.0.1:7007
  redis-trib add-node 127.0.0.1:7000 127.0.0.1:7001 127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005
  # slots: (0 slots)说明没有分配哈希槽，需要手动重新分片迁移
  redis-trib reshard 127.0.0.1:7000
  ```

* 宕机修复

  ```sh
  redis-trib fix 127.0.0.1:7000
  ```

* 备份恢复

  > 当集群宕机导致集群无法正常使用时，需要备份数据文件，重新创建集群再恢复，主节点挂掉一半集群即不可用

  ```sh
  # 备份文件
  mv appendonly-7000.aof appendonly-7000.aof.bak
  mv appendonly-7001.aof appendonly-7001.aof.bak
  mv appendonly-7002.aof appendonly-7002.aof.bak
  mv appendonly-7003.aof appendonly-7003.aof.bak
  mv appendonly-7004.aof appendonly-7004.aof.bak
  mv appendonly-7005.aof appendonly-7005.aof.bak
  
  mv dump-7000.rdb dump-7000.rdb.bak
  mv dump-7001.rdb dump-7001.rdb.bak
  mv dump-7002.rdb dump-7002.rdb.bak
  mv dump-7003.rdb dump-7003.rdb.bak
  mv dump-7004.rdb dump-7004.rdb.bak
  mv dump-7005.rdb dump-7005.rdb.bak
  
  # 重新创建集群
  redis-trib create --replicas 1 127.0.0.1:7000 127.0.0.1:7001 127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005
  
  # 删除
  rm -rf appendonly-700*.aof
  rm -rf dump-700*.rdb
  
  # 恢复数据
  mv appendonly-7000.aof.bak appendonly-7000.aof
  mv appendonly-7001.aof.bak appendonly-7001.aof
  mv appendonly-7002.aof.bak appendonly-7002.aof
  mv appendonly-7003.aof.bak appendonly-7003.aof
  mv appendonly-7004.aof.bak appendonly-7004.aof
  mv appendonly-7005.aof.bak appendonly-7005.aof
  
  mv dump-7000.rdb.bak dump-7000.rdb
  mv dump-7001.rdb.bak dump-7001.rdb
  mv dump-7002.rdb.bak dump-7002.rdb
  mv dump-7003.rdb.bak dump-7003.rdb
  mv dump-7004.rdb.bak dump-7004.rdb
  mv dump-7005.rdb.bak dump-7005.rdb
  
  # 重新启动服务
  sh stop.sh
  sh start.sh
  
  # 集群信息
  
  ```

  

### 3.docker-compose版

[参考](https://www.cnblogs.com/AD-milk/p/16200539.html)

#### 3.1 docker路径

```sh
# 拉取镜像
docker pull redis:5.0.5

# 创建文件夹
mkdir -p /data/redis

# 创建文件夹
mkdir -p /data/redis/node1/master
mkdir -p /data/redis/node1/slave
mkdir -p /data/redis/node2/master
mkdir -p /data/redis/node2/slave
mkdir -p /data/redis/node3/master
mkdir -p /data/redis/node3/slave

mkdir -p /data/redis/node1/master/data
mkdir -p /data/redis/{node1,node2,node3}/{master,slave}/data

# 设置权限
chmod -R 755 /data/redis
```

#### 3.2 创建配置

```sh
# 创建空文件
vi /data/redis/redis.conf

# 分别写入内容
port 6379
cluster-enabled yes
cluster-config-file nodes-redis.conf
cluster-node-timeout 5000
appendonly yes
appendfilename "appendonly-redis.aof"

# 拷贝配置到所有节点
cp -f /data/redis/redis.conf /data/redis/node1/master
cp -f /data/redis/redis.conf /data/redis/node1/slave
cp -f /data/redis/redis.conf /data/redis/node2/master
cp -f /data/redis/redis.conf /data/redis/node2/slave
cp -f /data/redis/redis.conf /data/redis/node3/master
cp -f /data/redis/redis.conf /data/redis/node3/slave

# 设置权限
chmod -R 755 /data/redis
```



#### 3.2 创建网卡

```sh
# 创建
docker network create redis-net --subnet 172.29.0.0/16

# 删除
docker network rm redis-net

# 删除所有未使用的
docker network prune -f

# 查看列表
docker network ls

# 查看详情
docker network inspect redis-net

# 将容器链接
docker network connect redis-net 容器名

# 断开链接
docker network disconnect redis-net 容器名
```

#### 3.3 创建docker-compose.yml

> vi /data/redis/docker-compose.yml，测试的时候master1的ip我设置为了172.29.0.1导致启动提示地址被占用，特别注意172.29.0.1网关地址，不能使用它

```yaml
version: '3'

services:
  redis-cluster:
  	image: redis:5.0.5
  	command: 'redis-cli --cluster create --cluster-replicas 1 172.29.0.11:6379 172.29.0.12:6379 172.29.0.13:6379 172.29.0.14:6379 172.29.0.15:6379 172.29.0.16:6379 --cluster-yes'
  	networks:
  	  - redis-net
  	depends_on:
  	  - master1
  	  - slave1
  	  - master2
  	  - slave2
  	  - master3
  	  - slave3

  master1:
    image: redis:5.0.5
    container_name: redis-node1-master
    command: redis-server /etc/redis/redis.conf
    stdin_open: true
    tty: true
    volumes:
      - /data/redis/node1/master/redis.conf:/etc/redis/redis.conf
      - /data/redis/node1/master/data:/data
    ports:
      - 7001:6379
    networks:
      redis-net:
        ipv4_address: 172.29.0.11

  slave1:
    image: redis:5.0.5
    container_name: redis-node1-slave
    command: redis-server /etc/redis/redis.conf
    stdin_open: true
    tty: true
    volumes:
      - /data/redis/node1/slave/redis.conf:/etc/redis/redis.conf
      - /data/redis/node1/slave/data:/data
    ports:
      - 7002:6379
    networks:
      redis-net:
        ipv4_address: 172.29.0.12

  master2:
    image: redis:5.0.5
    container_name: redis-node2-master
    command: redis-server /etc/redis/redis.conf
    stdin_open: true
    tty: true
    volumes:
      - /data/redis/node2/master/redis.conf:/etc/redis/redis.conf
      - /data/redis/node2/master/data:/data
    ports:
      - 7003:6379
    networks:
      redis-net:
        ipv4_address: 172.29.0.13

  slave2:
    image: redis:5.0.5
    container_name: redis-node2-slave
    command: redis-server /etc/redis/redis.conf
    stdin_open: true
    tty: true
    volumes:
      - /data/redis/node2/slave/redis.conf:/etc/redis/redis.conf
      - /data/redis/node2/slave/data:/data
    ports:
      - 7004:6379
    networks:
      redis-net:
        ipv4_address: 172.29.0.14

  master3:
    image: redis:5.0.5
    container_name: redis-node3-master
    command: redis-server /etc/redis/redis.conf
    stdin_open: true
    tty: true
    volumes:
      - /data/redis/node3/master/redis.conf:/etc/redis/redis.conf
      - /data/redis/node3/master/data:/data
    ports:
      - 7005:6379
    networks:
      redis-net:
        ipv4_address: 172.29.0.15

  slave3:
    image: redis:5.0.5
    container_name: redis-node3-slave
    command: redis-server /etc/redis/redis.conf
    stdin_open: true
    tty: true
    volumes:
      - /data/redis/node3/slave/redis.conf:/etc/redis/redis.conf
      - /data/redis/node3/slave/data:/data
    ports:
      - 7006:6379   
    networks:
      redis-net:
        ipv4_address: 172.29.0.16

networks:
  redis-net:
    external: true
```

#### 3.4 运行

```sh
# 后台启动，成功全部为done
docker-compose up -d

# 停止
docker-compose down

# 查看日志
docker-compose logs -f --tail 200

# 查看运行的容器
docker-compose ps
```

#### 3.5 创建集群(可选)

> 若前面docker-comopse.yml有创建集群命令，则此步骤可省略

```sh
# 进入任意一个容器
docker exec -it 容器id bash

redis-cli --cluster create --cluster-replicas 1 172.29.0.11:6379 172.29.0.12:6379 172.29.0.13:6379 172.29.0.14:6379 172.29.0.15:6379 172.29.0.16:6379
```

