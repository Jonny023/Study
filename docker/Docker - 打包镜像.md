# docker打包镜像

* Dockerfile

```shell
FROM colovu/ffmpeg-java:latest
WORKDIR /home
RUN mkdir -p /home/ffmpeg_log
COPY test-1.0.jar app.jar
COPY dist dist
COPY config config
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT [ "java", "-jar", "-Dspring.profiles.active=dev", "app.jar" ]
```

* shell脚本

```shell
current=`date "+%Y-%m-%d %H:%M:%S"`
timeStamp=`date -d "$current" +%s`
#将current转换为时间戳，精确到毫秒
currentTimeStamp=$((timeStamp*1000+`date "+%N"`/1000000))
echo $currentTimeStamp

docker rm -f $(docker ps -a | grep test:v1.0" | awk '{print $1}')
docker build -t test:v1.0_$currentTimeStamp .
docker run -itd --name test -p 8086:8086 -v /data/docker/volumes/images:/home/images test:v1.0_$currentTimeStamp
```