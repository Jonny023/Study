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
