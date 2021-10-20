# docker打包镜像

* Dockerfile

```shell
FROM java:8
# WORKDIR /home
COPY spring-boot-nacos-1.0.jar app.jar
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT [ "java", "-jar", "-Dspring.profiles.active=local", "app.jar" ]
```

* shell脚本

```shell
current=`date "+%Y-%m-%d %H:%M:%S"`
timeStamp=`date -d "$current" +%s`
#将current转换为时间戳，精确到毫秒
currentTimeStamp=$((timeStamp*1000+`date "+%N"`/1000000))
echo $currentTimeStamp
#项目名称
projectName=springboot-nacos
#版本号
version=v1.0
name=${projectName}_${version}_${currentTimeStamp}

docker rm -f $(docker ps -a | grep ${projectName}_${version} | awk '{print $1}')
docker build -t $name .
docker run -itd --name test --network=host --restart=always -p 8086:8086 -v /usr/local/project/boot_nacos:/usr/local/project/boot_nacos $name
```