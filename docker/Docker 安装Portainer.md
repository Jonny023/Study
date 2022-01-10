# Docker 安装Portainer

* 搜索相关Portainer镜像，以免错过更好的第三方镜像

```shell
docker search portainer
```

* 下载选定的Portainer镜像，这里我们选择下载量最多的官方镜像，如果未指定版本则默认为最新版本，`latest`版本

```shell
docker pull portainer/portainer:latest
```

运行镜像

- 本机模式

  ```shell
  docker run -d -p 9000:9000 -v /var/run/docker.sock:/var/run/docker.sock --restart=always --name prtainer portainer/portainer
  ```

- 远程模式

  ```shell
  docker run -d -p 9000:9000 --restart=always --name prtainer portainer/portainer
  ```

访问Portainer容器：`http://IP:9000`

- 首次登录需要设置`admin`的密码
- 选择docker连接
  - 选择`Local`，代表本地模式，portainer仅管理本机的docker容器
  - 选择`Remote`，代表远程模式，名称随意，在`Endpoint URL`中填写`docker节点的地址:docker远程端口`（docker安装教程中的设置的`-H 0.0.0.0:2375`中的`2375`）



# 这里我下载的lihaixin

```shell
docker pull lihaixin/portainer

# 本机模式
docker run -d -p 9000:9000 -v /var/run/docker.sock:/var/run/docker.sock --restart=always --name prtainer lihaixin/portainer

# 远程模式
docker run -d -p 9000:9000 --restart=always --name prtainer lihaixin/portainer
```

