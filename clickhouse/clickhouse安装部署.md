# 系统环境

##  资源配置

###   测试环境

| 系统       | CPU  | 内存 | 磁盘 | 数量 | 网络   | 备注                                    |
| :--------- | :--- | :--- | :--- | :--- | :----- | :-------------------------------------- |
| centos 7.x | 8C   | 32G  | 500G | 2    | 10Gbps | 磁盘使用ext4文件系统，两分片无复本，HDD |

###  生产环境

| 系统       | CPU  | 内存 | 磁盘 | 数量 | 网络   | 备注                                                         |
| :--------- | :--- | :--- | :--- | :--- | :----- | :----------------------------------------------------------- |
| centos 7.x | 32C  | 128G | 20T  | 4    | 10Gbps | 超大数据量场景，磁盘使用ext4文件系统，尽量使用SSD，否则HDD 使用RAID-10 |

##  环境配置

###   永久禁用swap

- 关闭swap分区

```shell
swapoff -a
```

- 修改配置文件`/etc/fstab`

​    删除 `/mnt/swap swap swap defaults 0 0` 这一行或者注释掉这一行

- 确认swap已经关闭   

```
free -m
```

-   调整 `swappiness` 参数永久生效

```shell
echo "vm.swappiness = 0" >> /etc/sysctl.conf
sysctl -p
```

###   禁用透明大页内存

####    临时禁用

```shell
echo never > /sys/kernel/mm/transparent_hugepage/enabled
echo never > /sys/kernel/mm/transparent_hugepage/defrag
```

####    永久禁用

```shell
 echo 'echo never > /sys/kernel/mm/transparent_hugepage/defrag' >> /etc/rc.d/rc.local
 echo 'echo never > /sys/kernel/mm/transparent_hugepage/enabled' >> /etc/rc.d/rc.local
```

####    验证生效

  

```shell
grep Huge /proc/meminfo
cat /proc/sys/vm/nr_hugepages
```



###   修改文件描述符

- 执行 $ ulimit -n 500000

- 执行 $ echo '* soft nofile 500000' >> /etc/security/limits.conf

- 执行 $ echo '* hard nofile 500000' >> /etc/security/limits.conf

- 执行 $ ulimit -u unlimited

- 编辑文件 $ vim /etc/security/limits.d/20-nproc.conf，修改为如下内容

  ```shell
  root soft nproc unlimited
  * soft nproc unlimited
  ```

-  修改`/etc/systemd/system.conf`文件，添加内容如下

  ```shell
  DefaultLimitCORE=infinity
  DefaultLimitNOFILE=500000
  DefaultLimitNPROC=500000
  ```

### 配置Host文件

所有机器的主机名配置到各个机器的`host`文件中

# 程序安装

```shell
mkdir -p /opt/clickhouse
```

将三个安装包拷贝到上述创建的目录，然后执行如下命令。

```shell
export LATEST_VERSION=21.3.9.83
tar -xzvf clickhouse-common-static-$LATEST_VERSION.tgz && ./clickhouse-common-static-$LATEST_VERSION/install/doinst.sh
tar -xzvf clickhouse-server-$LATEST_VERSION.tgz && ./clickhouse-server-$LATEST_VERSION/install/doinst.sh
tar -xzvf clickhouse-client-$LATEST_VERSION.tgz && ./clickhouse-client-$LATEST_VERSION/install/doinst.sh
```

 

# 程序配置

##  单机部署配置文件

   将单机配置`config.xml` 覆盖 `/etc/clickhouse-server/config.xml` 文件

## 集群配置文件

   将集群配置`config.xml` 覆盖 `/etc/clickhouse-server/config.xml` 文件

   将`users.xml`和`metrika.xml`文件拷贝到`/etc/clickhouse-server`目录下，注意不同的分片需要修改 `metrika.xml`文件中的分片号。

# 创建目录

##   开发环境

```shell
mkdir -p /usr/local/data/data1/clickhouse &&  chown -R clickhouse:clickhouse /usr/local/data/data1/clickhouse && chmod -R 755 /usr/local && chmod -R 755

/etc/clickhouse-server/*
```

##   测试和正式环境

```shell
mkdir -p /mnt/datadisk0/clickhouse && chown -R clickhouse:clickhouse /mnt/datadisk0/clickhouse &&  chmod -R 755 /etc/clickhouse-server/* &&  chown -R clickhouse:clickhouse /etc/clickhouse-server/*
```



# 程序启停

```shell
systemctl  enable  clickhouse-server.service

systemctl  start  clickhouse-server.service

systemctl  stop  clickhouse-server.service
```



# 创建数据库和表

上传数据库脚本并执行如下命令：

```shell
clickhouse-client --multiquery < /xxx/test.sql
```

# 创建用户和角色

 注意`users.xml`配置中的 `<access_management>1</access_management>` 必须启用，初始安装都是启用的。

上传用户脚本并执行如何命令:

```shell
clickhouse-client --multiquery < /xxx/role.sql
```

# 禁用default用户

后续的所有操作使用管理员帐号处理，`default`帐号不再具有集群管理功能，只具有单机操作能力

 注释掉配置节

```xml
<access_management>1</access_management>
```