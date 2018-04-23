下载redis
```
wget http://download.redis.io/releases/redis-3.2.9.tar.gz
```
解压
```
tar zxvf redis-3.2.9.tar.gz
```
可能需要安装redis-3.0.0.gem
```
yum install redis-3.0.0.gem
```

安装到指定路径/usr/local/redis
```
cd redis-3.2.9
make && make PREFIX=/usr/local/redis install
```
如果失败，请使用yum安装gcc和tcl
```
yum install gcc
yum install tcl
```
创建自定义目录，存放多个自定义redis
```
mkdir redis-cluster
```
复制安装的redis目录为多个实例，命令redis01、redis02、redis03、redis04、redis05、redis06
```
cp -r redis ./redis-cluster/redis01
cp -r redis ./redis-cluster/redis02
cp -r redis ./redis-cluster/redis03
cp -r redis ./redis-cluster/redis04
cp -r redis ./redis-cluster/redis05
cp -r redis ./redis-cluster/redis06
```
copy解压目录redis-3.2.9/redis.conf配置文件到redis01/etc,redis02/etc,redis03/etc,redis04/etc,redis05/etc,redis06/etc下面
```
cp redis-3.2.9/redis.conf /usr/local/redis-cluster/redis02/etc/redis.conf
cp redis-3.2.9/redis.conf /usr/local/redis-cluster/redis03/etc/redis.conf
cp redis-3.2.9/redis.conf /usr/local/redis-cluster/redis04/etc/redis.conf
cp redis-3.2.9/redis.conf /usr/local/redis-cluster/redis05/etc/redis.conf
cp redis-3.2.9/redis.conf /usr/local/redis-cluster/redis01/etc/redis.conf
```
编辑多个实例下面的redis.conf，将45行端口分别改为7001-7006，配置后台启动daemonize修改为yes，632行开启集群cluster-enabled改为yes
```
[root@localhost redis-cluster]# ./start-all.sh 
[root@localhost redis-cluster]# 
```
进入redis-cluster目录
```
cd /usr/local/redis-cluster
```
创建启动脚本start-all.sh
```
touch start-all.sh
```

编辑start-all.sh
```
vi start-all.sh
```
写入并保存退出
```
cd redis01/
./bin/redis-server ./etc/redis.conf
cd ../
cd redis02/
./bin/redis-server ./etc/redis.conf
cd ../
cd redis03/
./bin/redis-server ./etc/redis.conf
cd ../
cd redis04/
./bin/redis-server ./etc/redis.conf
cd ../
cd redis05/
./bin/redis-server ./etc/redis.conf
cd ../
cd redis06/
./bin/redis-server ./etc/redis.conf
cd ../
```
修改start-all.sh的执行权限
```
chmod -x start-all.sh
```
启动脚本，如下表示成功
```
./start-all.sh
```

查看进程
```
[root@localhost redis-cluster]# ps -ef|grep redis
root       9257      1  0 12:14 ?        00:00:02 ./bin/redis-server *:7001 [cluster]
root       9261      1  0 12:14 ?        00:00:02 ./bin/redis-server *:7002 [cluster]
root       9263      1  0 12:14 ?        00:00:02 ./bin/redis-server *:7003 [cluster]
root       9269      1  0 12:14 ?        00:00:02 ./bin/redis-server *:7004 [cluster]
root       9273      1  0 12:14 ?        00:00:02 ./bin/redis-server *:7005 [cluster]
root       9277      1  0 12:14 ?        00:00:02 ./bin/redis-server *:7006 [cluster]
root       9954   2590  0 13:06 pts/0    00:00:00 grep --color=auto redis
```
复制redis-3.2.9/src/redis-trib.rb到redis-cluster目录
```
cp redis-3.2.9/src/redis-trib.rb /usr/local/redis-cluster
```
进入/usr/local/redis-cluster，启动集群
```
./redis-trib.rb create 127.0.0.1:7001 127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005 127.0.0.1:7006
```
