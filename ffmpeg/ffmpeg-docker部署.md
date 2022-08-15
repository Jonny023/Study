
* Dockerfile

```dockerfile
FROM azul/zulu-openjdk-alpine:11.0.3-jre

#更新源，安装yasm ffmpeg
RUN echo -e 'https://mirrors.aliyun.com/alpine/v3.6/main/\nhttps://mirrors.aliyun.com/alpine/v3.6/community/' > /etc/apk/repositories \
  && apk update && apk upgrade && apk add yasm && apk add ffmpeg

WORKDIR /usr/local/app

#设置时区，解决时区问题
RUN echo "Asia/Shanghai" > /etc/timezone

ENV LANG C.UTF-8

VOLUME /tmp
VOLUME /usr/local/app/video /usr/local/app/video
ADD boot-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Xmx128m","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]
```

### 构建镜像

```sh
# 目录结构
[root@localhost app]# ll
total 17212
-rw-r--r--. 1 root root 17619211 Jul 19 16:13 boot-0.0.1-SNAPSHOT.jar
-rw-r--r--. 1 root root      596 Jul 19 16:59 Dockerfile
drwxr-xr-x. 2 root root      132 Jul 19 17:07 video
[root@localhost app]# pwd
/usr/local/app

# 构建镜像
docker build -t boot:v1 .
```

### 运行

```sh
# 运行
docker run -it -p 8080:8080 --name boot-v1 -d boot:v1

# 进入容器
docker exec -it boot-v1 sh
```

## ffmpeg

### 多视频合成

* [参考](https://www.cnblogs.com/nihaorz/p/14701637.html)

```sh
# 先转换mp4为ts
ffmpeg -i video_preview_h264.mp4 -vcodec copy -acodec copy -vbsf h264_mp4toannexb 1.ts
ffmpeg -i video_preview_h264 (1).mp4 -vcodec copy -acodec copy -vbsf h264_mp4toannexb 2.ts
ffmpeg -i video_preview_h2642.mp4 -vcodec copy -acodec copy -vbsf h264_mp4toannexb 3.ts
ffmpeg -i video_preview_h2641.mp4 -vcodec copy -acodec copy -vbsf h264_mp4toannexb 4.ts

# 将多个ts合成MP4
ffmpeg -i "concat:1.ts|2.ts|3.ts|4.ts" -acodec copy -vcodec copy -absf aac_adtstoasc -c copy output.mp4

# 删除ts
rm ./*.ts
```

