# ElasticSearch

[前往官网下载](https://www.elastic.co/cn/downloads/elasticsearch)

1.下载tar.gz并且上传解压

```shell
tar -xvf elasticsearch-7.14.0-linux-x86_64.tar.gz
```

2.创建专用账号和组

```shell
groupadd elk
useradd elk -d /home/elk -s /bin/sh -g elk

# 权限
chown -R elk:elk /usr/local/elasticsearch-7.14.0 && chmod -R 755 /usr/local/elasticsearch-7.14.0
```

3.解压安装后，运行elasticsearch服务前，需要进行基础配置，vim命令打开解压目录下的`/usr/local/elasticsearch-7.14.0/config/elasticsearch.yml`配置文件

```shell
vim /usr/local/elasticsearch-7.14.0/config/elasticsearch.yml
```

* 主要配置项：

  * cluster.name

  * node.name

  * path.data

  * path.logs

  * network.host

  * http.port

  * cluster.initial_master_nodes

    > **path.data**、**path.logs**指定的目录需要给运行`Elasticsearch`服务的专用账号进行授权,**cluster.initial_master_nodes**一定要指明，否则启动会报错

```yaml
# ======================== Elasticsearch Configuration =========================
#
# NOTE: Elasticsearch comes with reasonable defaults for most settings.
#       Before you set out to tweak and tune the configuration, make sure you
#       understand what are you trying to accomplish and the consequences.
#
# The primary way of configuring a node is via this file. This template lists
# the most important settings you may want to configure for a production cluster.
#
# Please consult the documentation for further information on configuration options:
# https://www.elastic.co/guide/en/elasticsearch/reference/index.html
#
# ---------------------------------- Cluster -----------------------------------
#
# Use a descriptive name for your cluster:
#
cluster.name: my-es-app
#
# ------------------------------------ Node ------------------------------------
#
# Use a descriptive name for the node:
#
node.name: node-1
#
# Add custom attributes to the node:
#
#node.attr.rack: r1
#
# ----------------------------------- Paths ------------------------------------
#
# Path to directory where to store the data (separate multiple locations by comma):
#
path.data: /usr/local/elasticsearch-7.14.0/data
#
# Path to log files:
#
path.logs: /usr/local/elasticsearch-7.14.0/logs
#
# ----------------------------------- Memory -----------------------------------
#
# Lock the memory on startup:
#
#bootstrap.memory_lock: true
#
# Make sure that the heap size is set to about half the memory available
# on the system and that the owner of the process is allowed to use this
# limit.
#
# Elasticsearch performs poorly when the system is swapping the memory.
#
# ---------------------------------- Network -----------------------------------
#
# By default Elasticsearch is only accessible on localhost. Set a different
# address here to expose this node on the network:
#
#network.host: 192.168.0.1
network.host: 0.0.0.0
#
#
# By default Elasticsearch listens for HTTP traffic on the first free port it
# finds starting at 9200. Set a specific HTTP port here:
#
http.port: 9200
#
# For more information, consult the network module documentation.
#
# --------------------------------- Discovery ----------------------------------
#
# Pass an initial list of hosts to perform discovery when this node is started:
# The default list of hosts is ["127.0.0.1", "[::1]"]
#
#discovery.seed_hosts: ["host1", "host2"]
#
# Bootstrap the cluster using an initial set of master-eligible nodes:
#
#cluster.initial_master_nodes: ["node-1", "node-2"]
cluster.initial_master_nodes: ["node-1"]
#
# For more information, consult the discovery and cluster formation module documentation.
#
# ---------------------------------- Various -----------------------------------
#
# Require explicit names when deleting indices:
#
#action.destructive_requires_name: true
```

4.加大运行Elasticsearch服务的专用账号可创建的文件描述符(descriptor)数量到超过65535：

```bash
[root@localhost elasticsearch-7.14.0]# vim /etc/security/limits.conf
[root@localhost elasticsearch-7.14.0]# cat /etc/security/limits.conf
# /etc/security/limits.conf
#
#This file sets the resource limits for the users logged in via PAM.
#It does not affect resource limits of the system services.
#
#Also note that configuration files in /etc/security/limits.d directory,
#which are read in alphabetical order, override the settings in this
#file in case the domain is the same or more specific.
#That means for example that setting a limit for wildcard domain here
#can be overriden with a wildcard setting in a config file in the
#subdirectory, but a user specific setting here can be overriden only
#with a user specific setting in the subdirectory.
#
#Each line describes a limit for a user in the form:
#
#<domain>        <type>  <item>  <value>
#
#Where:
#<domain> can be:
#        - a user name
#        - a group name, with @group syntax
#        - the wildcard *, for default entry
#        - the wildcard %, can be also used with %group syntax,
#                 for maxlogin limit
#
#<type> can have the two values:
#        - "soft" for enforcing the soft limits
#        - "hard" for enforcing hard limits
#
#<item> can be one of the following:
#        - core - limits the core file size (KB)
#        - data - max data size (KB)
#        - fsize - maximum filesize (KB)
#        - memlock - max locked-in-memory address space (KB)
#        - nofile - max number of open file descriptors
#        - rss - max resident set size (KB)
#        - stack - max stack size (KB)
#        - cpu - max CPU time (MIN)
#        - nproc - max number of processes
#        - as - address space limit (KB)
#        - maxlogins - max number of logins for this user
#        - maxsyslogins - max number of logins on the system
#        - priority - the priority to run user process with
#        - locks - max number of file locks the user can hold
#        - sigpending - max number of pending signals
#        - msgqueue - max memory used by POSIX message queues (bytes)
#        - nice - max nice priority allowed to raise to values: [-20, 19]
#        - rtprio - max realtime priority
#
#<domain>      <type>  <item>         <value>
#

#*               soft    core            0
#*               hard    rss             10000
#@student        hard    nproc           20
#@faculty        soft    nproc           20
#@faculty        hard    nproc           50
#ftp             hard    nproc           0
#@student        -       maxlogins       4

elk hard nofile 65536
elk soft nofile 65536
# End of file
```

5.修改最大虚拟内存区

> 永久有效需设置`/etc/sysctl.conf`

```bash
# 临时有效
[root@localhost elasticsearch-7.14.0]# sysctl -w vm.max_map_count=262145
vm.max_map_count = 262145

# 永久生效
[root@localhost elasticsearch-7.14.0]# vim /etc/sysctl.conf
[root@localhost elasticsearch-7.14.0]# cat /etc/sysctl.conf
# sysctl settings are defined through files in
# /usr/lib/sysctl.d/, /run/sysctl.d/, and /etc/sysctl.d/.
#
# Vendors settings live in /usr/lib/sysctl.d/.
# To override a whole file, create a new file with the same in
# /etc/sysctl.d/ and put new settings there. To override
# only specific settings, add a file with a lexically later
# name in /etc/sysctl.d/ and put new settings there.
#
# For more information, see sysctl.conf(5) and sysctl.d(5).

vm.max_map_count=262145
```

6.如有防火墙，添加防火墙白名单

```shell
firewall-cmd --zone=public --add-port=9200/tcp --permanent
firewall-cmd --zone=public --add-port=9300/tcp --permanent
firewall-cmd --reload
```

7.创建服务脚本

> vim /etc/systemd/system/elasticsearch.service

```shell
[Unit]
Description=elasticsearch service
Requires=network-online.target
After=network-online.target

[Service]
Type=simple
LimitNOFILE=65536
ExecStart=/usr/local/elasticsearch-7.14.0/bin/elasticsearch 
ExecStop=/usr/local/elasticsearch-7.14.0/bin/elasticsearch 
User=elk
Group=elk
Restart=always
TimeoutSec=600

[Install]
WantedBy=multi-user.target
```

8.启动服务

```shell
systemctl daemon-reload
systemctl enable elasticsearch.service
systemctl start elasticsearch.service
```

9.浏览器访问9200端口测试

```
http://ip:9200/
```

