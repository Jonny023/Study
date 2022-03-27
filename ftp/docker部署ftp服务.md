# docker部署ftp服务

## 下载

```shell
docker pull fauria/vsftpd 
```

## 启动

```sh
docker run -d -v /home/ftp:/home/vsftpd -p 2020:20 -p 2021:21 -p  21100-21110:21100-21110 -e FTP_USER=test -e FTP_PASS=test -e PASV_ADDRESS=192.168.56.2 -e PASV_MIN_PORT=21100 -e PASV_MAX_PORT=21110 --name vsftpd -e TZ=Asia/Shanghai --restart=always fauria/vsftpd

docker run -itd -v /home/ftp:/home/vsftpd \
-p 2020:20 \
-p 2021:21 \
-p 21100-21110:21100-21110 \
-e FTP_USER=test \
-e FTP_PASS=test \
-e PASV_ADDRESS=192.168.1.2 \
-e PASV_MIN_PORT=21100 \
-e PASV_MAX_PORT=21110 \
-e TZ=Asia/Shanghai \
-e LANG=zh_CN.utf8 \
-e LC_ALL=zh_CN.utf8 \
--name vsftpd \
--restart=always fauria/vsftpd
```

参数说明：

* /home/ftp:/home/vsftpd：映射 docker 容器 ftp 文件根目录（冒号前面是宿主机的目录）
* -p：映射 docker 端口（冒号前面是宿主机的端口）
* -e FTP_USER=test -e FTP_PASS=test ：设置默认的用户名密码（都为 test）
* PASV_ADDRESS：当前电脑ip，当需要使用被动模式时必须设置。
* PASV_MIN_PORT~ PASV_MAX_PORT：给客服端提供下载服务随机端口号范围，默认 21100-21110，与前面的 docker 端口映射设置成一样。



## 中文乱码

```sh
# 用xftp上传文件遇到乱码，可能是xftp软件编码问题，左侧windows编码默认为GBK2312,右侧linux编码应设置为
# Unicode(UTF-8)如果还不行则可能需要安装语言包,步骤如下

# 更新yum源(由于docker容器中没有yum源，所以需要将宿主机的文件拷贝到容器中)
docker cp /etc/yum.repos.d/CentOS-Base.repo ebadb0a06623:/etc/yum.repos.d/

yum clean all
yum makecache
yum update -y


# 查看语言
echo $LANG

# 查看系统支持语言
locale -a
locale

# 若没有zh_CN.UTF-8语言包
yum install kde-l10n-Chinese -y

# 设置语言
localedef -c -f UTF-8 -i zh_CN zh_CN.utf8
```



## 新增用户

```sh
docker exec -it vsftpd bash

# 创建用户(admin)目录
mkdir /home/vsftpd/admin

# 授权
chown -R ftp:ftp /home/vsftpd

# 奇数配置用户名，偶数配置密码
vi /etc/vsftpd/virtual_users.txt
test     #用户
test123  #密码
admin    #用户
admin123 #密码

# 写入db
/usr/bin/db_load -T -t hash -f /etc/vsftpd/virtual_users.txt /etc/vsftpd/virtual_users.db

# 重启容器
docker restart vsftpd
```



## web访问

> ftp://test:123456@ip:port