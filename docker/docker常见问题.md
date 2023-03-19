# docker常见问题

## 1.docker修改挂载路径

> 已经挂载的路径如何修改

(1) 直接删除旧的容器，重新运行新的容器

```sh
docker rm -f oldContainerId

docker run -d -v xx:xxx 容器名
```

(2) 修改配置

```sh
# 在 /var/lib/docker/containers/container-ID/config.v2.json 中找到 MountPoints，并修改挂载路径

# 重启容器
docker restart <container-name/ID>
```



### 2.拉取镜像报错

> docker pull mcr.microsoft.com/mssql/server:2012-latest
> Error response from daemon: Head "https://mcr.microsoft.com/v2/mssql/server/manifests/2012-latest": dial tcp: lookup mcr.microsoft.com on 192.168.124.1:53: no such host

* 解决方法

  > 重启docker

  ```sh
  [root@bogon ~]# systemctl restart docker
  [root@bogon ~]# docker pull mcr.microsoft.com/mssql/server:2022-latest
  2022-latest: Pulling from mssql/server
  ```

  
