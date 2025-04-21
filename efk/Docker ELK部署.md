# Docker EFK部署

[参考地址](https://blog.csdn.net/wangchange/article/details/144371858)

## 1.准备

```sh
# 创建elasticsearch、kibana、filebeat相关data、log、conf目录
sudo mkdir -p /usr/local/elk/elasticsearch/config
sudo mkdir -p /usr/local/elk/elasticsearch/data
sudo mkdir -p /usr/local/elk/elasticsearch/logs
sudo mkdir -p /usr/local/elk/kibana/config
sudo mkdir -p /usr/local/elk/kibana/data
sudo mkdir -p /usr/local/elk/kibana/logs
sudo mkdir -p /usr/local/elk/filebeat/config
sudo mkdir -p /usr/local/elk/filebeat/data
sudo mkdir -p /usr/local/elk/filebeat/logs

# 创建网络
cd /usr/local/elk/ && docker network create elk-net

# 拉取镜像
docker pull elasticsearch:8.6.2
docker pull kibana:8.6.2
docker pull elastic/filebeat
```

## 2.配置

> 由于docker运行时配置文件在容器中，需要存储到宿主机避免容器异常或删除后配置及数据丢失，需要临时运行下容器将容器配置文件拷贝到宿主机

```sh
# 运行es
docker run -d \
--name elasticsearch \
--env cluster.name=es-app-cluster \
--env bootstrap.memory_lock=true \
--env node.name=node-01 \
--env discovery.type=single-node \
--env xpack.security.enabled=true \
--env xpack.security.http.ssl.enabled=false \
--env xpack.security.transport.ssl.enabled=false \
--env ingest.geoip.downloader.enabled=false \
--env ELASTIC_USERNAME=elastic \
--env ELASTIC_PASSWORD=elastic \
--env ES_JAVA_OPTS="-Xms1g -Xmx1g" \
--ulimit memlock=-1:-1 \
--volume /etc/localtime:/etc/localtime:ro \
--volume /etc/timezone:/etc/timezone:ro \
--publish 9200:9200 \
--publish 9300:9300 \
--network elk-net \
--restart always \
--privileged \
elasticsearch:8.6.2

# 运行kibana
docker run -d \
--name kibana \
--env ELASTICSEARCH_HOSTS=http://elasticsearch:9200 \
--env ELASTICSEARCH_USERNAME=kibana_system \
--env ELASTICSEARCH_PASSWORD=elastic \
--env XPACK_SECURITY_ENABLED=true \
--env SERVER_NAME=kibana \
--volume /etc/localtime:/etc/localtime:ro \
--volume /etc/timezone:/etc/timezone:ro \
--publish 5601:5601 \
--network elk-net \
--restart always \
--privileged \
kibana:8.6.2

# 运行filebeat
docker run -d \
--name filebeat \
--volume /etc/localtime:/etc/localtime:ro \
--volume /etc/timezone:/etc/timezone:ro \
--network elk-net \
--restart always \
--privileged \
--user root \
elastic/filebeat:8.6.2

# 将elasticsearch容器目录复制到宿主机对应目录
docker cp elasticsearch:/usr/share/elasticsearch/config /usr/local/elk/elasticsearch/
docker cp elasticsearch:/usr/share/elasticsearch/data /usr/local/elk/elasticsearch/
docker cp elasticsearch:/usr/share/elasticsearch/logs /usr/local/elk/elasticsearch/

# 将kibana容器目录复制到宿主机对应目录
docker cp kibana:/usr/share/kibana/config /usr/local/elk/kibana/
docker cp kibana:/usr/share/kibana/data /usr/local/elk/kibana/
docker cp kibana:/usr/share/kibana/logs /usr/local/elk/kibana/

# 将filebeat容器目录复制到宿主机对应目录
docker cp filebeat:/usr/share/filebeat/filebeat.yml /usr/local/elk/filebeat/config/
docker cp filebeat:/usr/share/filebeat/data /usr/local/elk/filebeat/
docker cp filebeat:/usr/share/filebeat/logs /usr/local/elk/filebeat/
```

## 3.修改配置

```yaml
# ===============================================================
# 进入es目录并编辑配置
cd /usr/local/elk/elasticsearch/config/ && rm -rf elasticsearch.yml && vim elasticsearch.yml 

# 修改elasticsearch配置文件
# elasticsearch 配置文件 elasticsearch.yml内容
cluster.name: "es-app-cluster"
# 确保Elasticsearch监听所有接口
network.host: 0.0.0.0
node.name: node-01
path.data: /usr/share/elasticsearch/data
path.logs: /usr/share/elasticsearch/logs
http.port: 9200
discovery.type: single-node
xpack.security.enabled: true
bootstrap.memory_lock: true
# 禁用证书检查
xpack.security.http.ssl.enabled: false
xpack.security.transport.ssl.enabled: false
# GeoIP数据库用于将IP地址映射到地理位置信息,关闭它
ingest.geoip.downloader.enabled: false


# ===============================================================
# 进入kibana目录并编辑配置文件
cd /usr/local/elk/kibana/config/ && rm -rf kibana.yml && vim kibana.yml

# 修改kibana配置文件
# #Kibana 服务监听的网络接口地址
server.host: "0.0.0.0"
# Kibana 服务在接收到关闭信号后等待的时间
server.shutdownTimeout: "10s"
elasticsearch.hosts: [ "http://elasticsearch:9200" ]
# 启用或禁用 Kibana 监控界面中对 Elasticsearch 容器的监控
monitoring.ui.container.elasticsearch.enabled: true
# 界面汉化
i18n.locale: "zh-CN"
# 启用或禁用 Kibana 报告功能的角色管理
xpack.reporting.roles.enabled: false


# ===============================================================
# 进入filebeat目录并编辑配置文件
cd /usr/local/elk/filebeat/config/ && rm -rf filebeat.yml && vim filebeat.yml

# 修改filebeat配置文件
filebeat.inputs:
- type: filestream
  id: myserver-api #id要唯一
  enabled: true
  paths:
    - /usr/local/server/log/*.log #你的某个项目日志文件路径
  fields_under_root: true
  fields:
    type: myserver-api
    project: myserver
    app: myserver
 
output.elasticsearch:
  hosts: ["http://elasticsearch:9200"]
  username: elastic
  password: elastic
  indices:
    - index: "myserver-api-%{+yyyy.MM.dd}"
      when.equals:
        type: "myserver-api"
 
setup.template.name: "myserver"  # 设置模板名称
setup.template.pattern: "myserver-api-*"  # 设置模板模式
setup.ilm.enabled: false #如果你不需要自动管理索引生命周期，或者 Elasticsearch 集群没有配置 ILM 策略，建议禁用
 
setup.kibana:
  host: "kibana:5601"
```

## 4.运行容器

> 运行前先删除之前运行的临时容器

```sh
docker rm -f elasticsearch kibana filebeat
```



### 4.1 方式1(docker run)

```sh
# 运行es
docker run -d \
--name elasticsearch \
--env cluster.name=es-app-cluster \
--env bootstrap.memory_lock=true \
--env node.name=node-01 \
--env discovery.type=single-node \
--env xpack.security.enabled=true \
--env xpack.security.http.ssl.enabled=false \
--env xpack.security.transport.ssl.enabled=false \
--env ingest.geoip.downloader.enabled=false \
--env ELASTIC_USERNAME=elastic \
--env ELASTIC_PASSWORD=elastic \
--env ES_JAVA_OPTS="-Xms1g -Xmx1g" \
--ulimit memlock=-1:-1 \
--volume /usr/local/elk/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
--volume /usr/local/elk/elasticsearch/data:/usr/share/elasticsearch/data \
--volume /usr/local/elk/elasticsearch/logs:/usr/share/elasticsearch/logs \
--volume /etc/localtime:/etc/localtime:ro \
--volume /etc/timezone:/etc/timezone:ro \
--publish 9200:9200 \
--publish 9300:9300 \
--network elk-net \
--restart always \
--privileged \
elasticsearch:8.6.2

# 运行kibana
docker run -d \
--name kibana \
--env ELASTICSEARCH_HOSTS=http://elasticsearch:9200 \
--env ELASTICSEARCH_USERNAME=kibana_system \
--env ELASTICSEARCH_PASSWORD=elastic \
--env XPACK_SECURITY_ENABLED=true \
--env SERVER_NAME=kibana \
--volume /usr/local/elk/kibana/config/kibana.yml:/usr/share/kibana/config/kibana.yml \
--volume /usr/local/elk/kibana/data:/usr/share/kibana/data \
--volume /usr/local/elk/kibana/logs:/usr/share/kibana/logs \
--volume /etc/localtime:/etc/localtime:ro \
--volume /etc/timezone:/etc/timezone:ro \
--publish 5601:5601 \
--network elk-net \
--restart always \
--privileged \
kibana:8.6.2

# 运行filebeat
docker run -d \
--name filebeat \
--volume /usr/local/elk/filebeat/config/filebeat.yml:/usr/share/filebeat/filebeat.yml \
--volume /usr/local/elk/filebeat/data:/usr/share/filebeat/data \
--volume /usr/local/elk/filebeat/logs:/usr/share/filebeat/logs \
--volume /usr/local/server/log:/usr/local/server/log \
--volume /etc/localtime:/etc/localtime:ro \
--volume /etc/timezone:/etc/timezone:ro \
--network elk-net \
--restart always \
--privileged \
--user root \
elastic/filebeat:8.6.2
```

### 4.2 方式2(docker-compose总)

> docker-compose -f efk-docker-compose.yml up -d

```yaml
version: '3'

services:
  # ----------------------
  # Elasticsearch 服务
  # ----------------------
  elasticsearch:
    image: elasticsearch:8.6.2
    container_name: elasticsearch
    environment:
      - cluster.name=es-app-cluster
      - bootstrap.memory_lock=true
      - node.name=node-01
      - discovery.type=single-node
      - xpack.security.enabled=true
      - xpack.security.http.ssl.enabled=false
      - xpack.security.transport.ssl.enabled=false
      - ingest.geoip.downloader.enabled=false
      - ELASTIC_USERNAME=elastic
      - ELASTIC_PASSWORD=elastic
      - ES_JAVA_OPTS="-Xms256m -Xmx256m"  # 内存配置需根据实际环境调整
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - /usr/local/elk/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml
      - /usr/local/elk/elasticsearch/data:/usr/share/elasticsearch/data
      - /usr/local/elk/elasticsearch/logs:/usr/share/elasticsearch/logs
      - /etc/localtime:/etc/localtime:ro
      - /etc/timezone:/etc/timezone:ro
    ports:
      - "9200:9200"  # 外部访问端口:容器内端口
      - "9300:9300"  # 内部通信端口，一般无需对外暴露
    restart: always
    privileged: true
    networks:
      - elk-network  # 自定义网络，确保容器间通信

  # ----------------------
  # Kibana 服务
  # ----------------------
  kibana:
    image: kibana:8.6.2
    container_name: kibana
    environment:
      - ELASTICSEARCH_HOSTS=http://elasticsearch:9200  # 通过服务名访问 Elasticsearch
      - ELASTICSEARCH_USERNAME=kibana_system
      - ELASTICSEARCH_PASSWORD=elastic
      - XPACK_SECURITY_ENABLED=true
      - SERVER_NAME=kibana
    volumes:
      - /usr/local/elk/kibana/config/kibana.yml:/usr/share/kibana/config/kibana.yml
      - /usr/local/elk/kibana/data:/usr/share/kibana/data
      - /usr/local/elk/kibana/logs:/usr/share/kibana/logs
      - /etc/localtime:/etc/localtime:ro
      - /etc/timezone:/etc/timezone:ro
    ports:
      - "5601:5601"  # Kibana 访问端口
    restart: always
    privileged: true
    depends_on:
      - elasticsearch  # 确保 Elasticsearch 先启动
    networks:
      - elk-network

  # ----------------------
  # Filebeat 服务
  # ----------------------
  filebeat:
    image: elastic/filebeat:8.6.2
    container_name: filebeat
    user: root  # 确保有文件读取权限
    volumes:
      - /usr/local/elk/filebeat/config/filebeat.yml:/usr/share/filebeat/filebeat.yml
      - /usr/local/elk/filebeat/data:/usr/share/filebeat/data
      - /usr/local/elk/filebeat/logs:/usr/share/filebeat/logs
      - /usr/local/server/log:/usr/local/server/log:ro  # 挂载日志目录（只读）
      - /etc/localtime:/etc/localtime:ro
      - /etc/timezone:/etc/timezone:ro
    restart: always
    privileged: true
    depends_on:
      - elasticsearch  # 确保 Elasticsearch 先启动
    networks:
      - elk-network

# ----------------------
# 自定义网络
# ----------------------
networks:
  elk-network:  # 容器间通过服务名直接通信（如 elasticsearch:9200）
    driver: bridge
```



## 5.修改es用户密码

```sh
# 进入elasticsearch容器
docker exec -it elasticsearch /bin/bash

# 密码至少6位，这里我设置的elastic
./bin/elasticsearch-setup-passwords interactive

# 重启
docker restart elasticsearch kibana filebeat
```

## 6.访问

* elasticsearch: http://192.168.56.8:9200
  * 用户名和密码：`elastic`
* kibana: http://192.168.56.8:5601
  * 用户名和密码：`elastic`

## 7.创建视图

> 进入首页》Management》索引管理》数据管理 能看到定义的日志信息则表示正常运行

* 创建数据视图
  * 索引为：索引-*

## 8.查询

> 左侧菜单Discover进入日志查询界面