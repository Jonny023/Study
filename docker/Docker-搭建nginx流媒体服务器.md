# docker搭建nginx流媒体服务器

[参考1](https://www.shangmayuan.com/a/25cab03162de4960ae05ecb2.html)

[参考2](https://blog.csdn.net/chy555chy/article/details/109778101)

[参考3](https://www.i4k.xyz/article/qq_35947262/103523175)



### docker国内镜像地址

```shell
# 删除原配置
cd /etc/docker/
rm -f daemon.json

# 创建新配置
tee daemon.json <<- 'EOF'
{
  "exec-opts": ["native.cgroupdriver=systemd"],
  "log-driver": "json-file",
  "log-opts": {
    "max-size": "100m"
  },
  "storage-driver": "overlay2",
  "storage-opts": [
    "overlay2.override_kernel_check=true"
  ],
  "registry-mirrors": ["https://docker.mirrors.ustc.edu.cn", "https://ustc-edu-cn.mirror.aliyuncs.com","https://7uuu3esz.mirror.aliyuncs.com"],
  "data-root": "/data/docker"
}

EOF

# 使配置生效
sudo kill -SIGHUP $(pidof dockerd)
```



### 安装推流模块`rtmp-hls`

#### 查看完整信息

```shell
docker ps -a --no-trunc
```

#### 安装及运行hls

```shell
# 搜索hls
docker search rtmp-hls

NAME                               DESCRIPTION                                     STARS     OFFICIAL   AUTOMATED
alqutami/rtmp-hls                  RTMP, HLS, DASH video streaming server with …   68
xiayanji/rtmp-hls-flv              rtmp推流, 可以使用rtmp,  hls, http-flv三种方…            4

# 下载（拉取）
docker pull alqutami/rtmp-hls

# 查看镜像
docker images

# 启动（基础命令）
sudo docker run --restart=always -d --name rtmp -p 1935:1935 -p 8080:8080 -v /usr/local/nginx-rtmp/conf/nginx.conf:/etc/nginx/nginx.conf alqutami/rtmp-hls

# 启动运行
sudo docker run -d --name rtmp -p 1935:1935 -p 8080:8080 alqutami/rtmp-hls

# 拷贝【容器配置】文件到【宿主主机】，可以做定制化配置
docker cp rtmp:/etc/nginx/nginx.conf /usr/local/nginx-rtmp/conf/

# 挂载配置文件: 挂载文件时文件必须先存在
# 方式1
docker run --name rtmp -p 1935:1935 -p 8080:8080 -v /usr/local/nginx-rtmp/conf:/etc/nginx -d alqutami/rtmp-hls

# 方式2
docker run --name rtmp -p 1935:1935 -p 8080:8080 -v /usr/local/nginx-rtmp/conf/nginx.conf:/etc/nginx/nginx.conf -d alqutami/rtmp-hls

# 重启生效
docker run --restart=always --name rtmp -p 1935:1935 -p 8080:8080 -v /usr/local/nginx-rtmp/conf/nginx.conf:/etc/nginx/nginx.conf -d alqutami/rtmp-hls

# 【指定时区】运行【==========推荐==========】
docker run --restart=always --name rtmp -p 1935:1935 -p 8080:8080 -v /usr/local/nginx-rtmp/conf/nginx.conf:/etc/nginx/nginx.conf -v /etc/localtime:/etc/localtime -d alqutami/rtmp-hls

# 创建多个目录
mkdir -p /usr/local/nginx-rtmp/{conf.d,html,logs}

# 查看运行中的容器
docker ps
docker ps -a
```

# 推流及播放

```shell
# hls推流
ffmpeg -re -i "rtsp://admin:1234@192.168.1.2/cam/realmonitor?channel=1&subtype=0 live=1" -c copy -f flv rtmp://192.168.1.2:1935/show/1

# 播放地址
http://192.168.1.2:808/hls/1.m3u8






# live推流
ffmpeg -re -i "rtsp://admin:1234@192.168.1.2/cam/realmonitor?channel=1&subtype=0 live=1" -c copy -f flv rtmp://192.168.1.2:1935/live/1

# 播放
vlc rtmp://192.168.1.2:1935/live/1

# live=1实时流
ffplay "rtmp://192.168.1.2:1935/live/1 live=1"

```



# 时区问题

## 方式1

> 构建镜像时指定宿主主机时区（需保证宿主主机时区正确）

```dockerfile
docker run -d -v /etc/localtime:/etc/localtime --rm -p 8888:8080 tomcat:latest

# 再次进入容器查看时间
docker exec -it f232d09a5 /bin/bash

root@f232d09a5d79:/usr/local/tomcat# date
Wed May  9 11:11:48 CST 2018

root@07ed300df372:/# date -R
Thu, 07 Oct 2021 14:09:29 +0800
```

## 方式2

* Dockerfile

```dockerfile
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
```





# ffmpeg常用

```shell
# windows直播推流（java runtime调用）
cmd /c start ffmpeg -i " + src + " -vcodec copy -acodec copy -f flv " + out
ffmpeg -i rtsp://admin:1234@192.168.1.2/Streaming/Channels/101 -vcodec copy -acodec copy -f flv rtmp://192.168.1.2:1935/hls/1

# linux直播推流
ffmpeg rtmp://192.168.1.2:8080/hls/1.m3u8
ffmpeg -rtsp_transport tcp -i "rtsp://admin:1234@192.168.1.2/cam/realmonitor?channel=1&subtype=0" -c:v libx264 -acodec copy -f flv rtmp://10.113.74.51:1935/hls/1

# 截图
ffmpeg -i rtsp://admin:daas1234@192.168.1.2/Streaming/Channels/101?live=1 -y -ss 0 -r 1 -vframes 1 -f image2 /home/testa_1_100_20210916135336011.png
```

