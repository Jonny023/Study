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
-e PASV_ADDRESS=192.168.56.2 \
-e PASV_MIN_PORT=21100 \
-e PASV_MAX_PORT=21110 \
-e TZ=Asia/Shanghai \
--name vsftpd \
--restart=always fauria/vsftpd
```

参数说明：

* /home/ftp:/home/vsftpd：映射 docker 容器 ftp 文件根目录（冒号前面是宿主机的目录）
* -p：映射 docker 端口（冒号前面是宿主机的端口）
* -e FTP_USER=test -e FTP_PASS=test ：设置默认的用户名密码（都为 test）
* PASV_ADDRESS：当前电脑ip，当需要使用被动模式时必须设置。
* PASV_MIN_PORT~ PASV_MAX_PORT：给客服端提供下载服务随机端口号范围，默认 21100-21110，与前面的 docker 端口映射设置成一样。

