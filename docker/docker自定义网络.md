# docker自定义网络

* 查看网络：`docker network ls`

## 1. 创建自定义网络

> docker 默认是使用bridge，容器每次启动，IP都会发生变动，不利于维护管理, `172.17.0.0/16` 是docker默认网段

```shell
docker network create --subnet=172.18.0.0/16 my_net 
```

> `--network my_net --ip 172.18.0.2` 使用之前创建的网络, `172.18.0.1`是网关

```shell
docker run --network my_net --ip 172.18.0.2 --name xx -p 8086:8086 -itd xx
```

