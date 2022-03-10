# docker部署sentinel

## 拉取镜像

```shell
docker pull bladex/sentinel-dashboard:latest
```

## 运行

```shell
docker run --name sentinel --restart=always -d -p 9100:8858 bladex/sentinel-dashboard:latest
```

## 访问

> 访问地址：`http://宿主ip:8858`
>
> 账号密码都为：`sentinel`

