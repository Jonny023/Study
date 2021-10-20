# SpringCloud Nacos获取宿主ip

> 不做配置的情况下，docker部署获取到的ip为容器内部ip，也就是`172.0.0.x`，需要配置

## 方式1

> 直接在配置文件中配置`spring.cloud.nacos.preferred-networks`或者`spring.cloud.nacos.discovery.ip`指定ip，视版本而定

```yaml
spring:
  application:
    name: docker-nacos
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.56.101:8848
#        namespace: 1d7caa28-e11e-4263-8f30-05c772b28dce
#    inetutils:
      #      ignored-interfaces: # 忽略网卡，eth.*，正则表达式
      #        - lo.*
      #        - docker*
#      preferred-networks: 192.168.*   # 首选网络地址，可以写具体的地址，也可也写正则
```



## 方式2

> 通过docker配置

* `--network=host`直接使用宿主主机ip

```shell
docker run -itd --name test --network=host --restart=always -p 8086:8086 -v /usr/local/project/boot_nacos:/usr/local/project/boot_nacos $name
```



# 命令打包

## Dockerfile

```dockerfile
FROM java:8
# WORKDIR /home
COPY spring-boot-nacos-1.0.jar app.jar
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT [ "java", "-jar", "-Dspring.profiles.active=local", "app.jar" ]
```

## shell脚本

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
#映射端口
port=8088
name=${projectName}_${version}_${currentTimeStamp}

docker rm -f $(docker ps -a | grep ${projectName}_${version} | awk '{print $1}')
docker build -t $name .
docker run -itd --name test --network=host --restart=always -p ${port}:${port} -v /usr/local/project/boot_nacos:/usr/local/project/boot_nacos $name
```

