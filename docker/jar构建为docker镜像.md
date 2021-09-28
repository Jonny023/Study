# jar构建为docker镜像

## 1.方法一

> 先删除，然后在部署，自动添加时间戳

* 上传文件到特定目录

```java
mkdir /usr/local/project
```

* 在目录下创建文件`Dockerfile`

```dockerfile
# FROM colovu/ffmpeg-java:latest
WORKDIR /home
#RUN mkdir -p /home/ffmpeg_log
COPY test-1.0.jar app.jar
COPY dist dist
COPY config config
ENTRYPOINT [ "java", "-jar", "-Dspring.profiles.active=local", "app.jar" ]
```

* 在目录下创建shell脚本`build_start.sh`

```shell
current=`date "+%Y-%m-%d %H:%M:%S"`
timeStamp=`date -d "$current" +%s`
#将current转换为时间戳，精确到毫秒
currentTimeStamp=$((timeStamp*1000+`date "+%N"`/1000000))
echo $currentTimeStamp

docker rm -f $(docker ps -a | grep test:v1.0 | awk '{print $1}')
docker build -t test:v1.0_$currentTimeStamp .
docker run -itd --name test -p 8086:8086 -v /data/docker/volumes/images:/home/images test:v1.0_$currentTimeStamp
```



## 2.方法二

* 制作镜像`Dockerfile`

```dockerfile
FROM java:8
COPY test-1.0.jar app.jar
ENTRYPOINT [ "java", "-jar", "-Dspring.profiles.active=local", "app.jar" ]
```

* 运行
  * `-d` 后台运行
  * `-p` 端口映射，前一个为宿主主机端口，后一个为容器中的端口
  * `-v` 映射文件路径

```shell
docker run -d -p 8080:8080 helloworld

# 文件路径映射
docker run -d -p 8080:8080 -v /usr/local/upload:/usr/local/upload helloworld
```

