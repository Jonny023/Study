# docker安装nacos

[参考地址](https://www.cnblogs.com/serendipity-fzx/articles/15400618.html)

## 下载

```shell
docker pull nacos/nacos-server
```

## 单机部署

```shell
docker run -d \
--name nacos \
-p 8848:8848 \
--env MODE=standalone \
--env NACOS_SERVER_IP=192.168.56.101 \
nacos/nacos-server

mkdir -p /data/nacos/{logs,conf,data}
docker cp nacos:/home/nacos/logs/  /data/nacos/
docker cp nacos:/home/nacos/conf/  /data/nacos/
docker cp nacos:/home/nacos/data/  /data/nacos/

docker rm -f nacos

docker run -d \
--name nacos \
-p 8848:8848 \
--env MODE=standalone \
--env NACOS_SERVER_IP=192.168.56.101 \
--restart=always \
-v /data/nacos/logs:/home/nacos/logs \
-v /data/nacos/conf:/home/nacos/conf \
-v /data/nacos/data:/home/nacos/data \
nacos/nacos-server
```

### 访问

> http://ip:8848/nacos, 用户名密码：nacos



## 集群版部署

准备两台或多台服务器(三台服务器保持nacos镜像的版本一致); 然后分别执行以下内容 注意`NACOS_SERVER_IP` 参数需要修改为各个服务器自己的IP地址

```shell
docker run -d --name nacos-cluster -p 8848:8848 \
  --env NACOS_SERVERS=1.16.246.115,1.16.246.116,1.16.246.116 \
  --env NACOS_SERVER_IP=192.168.56.102 \
  --env SPRING_DATASOURCE_PLATFORM=mysql \
  --env MYSQL_SERVICE_HOST=1.16.246.115 \
  --env MYSQL_SERVICE_DB_NAME=nacos \
  --env MYSQL_SERVICE_USER=root \
  --env MYSQL_SERVICE_PASSWORD=123456 \
  --env MYSQL_DATABASE_NUM=1 \
  nacos/nacos-server
```

参数说明:

- `NACOS_SERVERS`: 集群节点信息
- `NACOS_SERVER_IP`: 当前服务的IP
- `SPRING_DATASOURCE_PLATFORM` : 使用数据库类型
- `MYSQL_SERVICE_HOST`: 数据库IP地址
- `MYSQL_SERVICE_DB_NAME`: 数据库名称
- `MYSQL_SERVICE_USER`: 数据库用户名
- `MYSQL_SERVICE_PASSWORD`: 数据库密码
- `MYSQL_DATABASE_NUM`: 数据库数量,默认为1
