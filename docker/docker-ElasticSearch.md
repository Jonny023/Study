# ElasticSearch

## 1.安装es

### 拉取镜像

```shell
docker pull elasticsearch
```

### 创建配置

```shell
# 创建配置目录和配置文件
mkdir -p /usr/local/docker/elasticsearch/config &&
echo "http.host: 0.0.0.0" >> /usr/local/docker/elasticsearch/config/elasticsearch.yml
```

### 运行

```shell
docker run -itd --name es -p 9200:9200 -p 9300:9300 \
-e ES_JAVA_OPTS="-Xms512m -Xmx512m" \
-e "discovery.type=single-node" \
-v /usr/local/docker/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
-v /usr/local/docker/elasticsearch/data:/usr/share/elasticsearch/data \
-v /usr/local/docker/elasticsearch/plugins:/usr/share/elasticsearch/plugins \
elasticsearch
```

### 跨域配置

> 配置文件中新增配置：`/usr/local/docker/elasticsearch/config/elasticsearch.yml`

```shell
http.cors.enabled: true
http.cors.allow-origin: "*"
network.bind_host: 0.0.0.0
```

* 配置好后重启

  * ```shell
    docker restart es
    ```

    

### 操作

```shell
# 查看运行的容器
docker ps
# 查看日志
docker logs es
# 后台运行日志
docker logs -f -t --tail 200 es
# 重启
docker restart es
# 开机自启
docker update --restart=always es
# 取消开机自启
docker update --restart=no es
# 停止
docker stop es

# 进入容器
docker exec -it es bash
```

### 测试

> 浏览器访问：http://ip:9200/

```json
{
  "name" : "hihOfOb",
  "cluster_name" : "elasticsearch",
  "cluster_uuid" : "FFeD7P0HQTuoln-8oTLmyA",
  "version" : {
    "number" : "5.6.12",
    "build_hash" : "cfe3d9f",
    "build_date" : "2018-09-10T20:12:43.732Z",
    "build_snapshot" : false,
    "lucene_version" : "6.6.1"
  },
  "tagline" : "You Know, for Search"
}
```

### 问题

```shell
ERROR: [1] bootstrap checks failed
[1]: max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
```

> 编辑文件：`vim /etc/sysctl.conf`，添加配置

```properties
vm.max_map_count=655360
```

> 并执行：`sysctl -p`，执行后重启`elasticsearch`

## 2.安装es-head可视化插件

> http://ip:9100，管理界面配置es的Api地址：http://ip:9200

```shell
docker pull tobias74/elasticsearch-head

# 运行
docker run -d -p 9100:9100 tobias74/elasticsearch-head
```

## 3.安装kibana

> http://ip:5601

```shell
docker pull kibana

# 因为es也是通过docker容器运行的，这里指定es容器所在的ip
# ELASTICSEARCH_URL地址必须是一个http协议的地址
# ip通过命令查看：docker exec es ip a
docker run -d -p 5601:5601 -e ELASTICSEARCH_URL=http://172.17.0.5:9200 --name kibana kibana
```

* 若需要修改es配置，则修改容器内部配置文件：`/etc/kibana/kibana.yml`

