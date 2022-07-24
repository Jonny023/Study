## ElasticSearch

[官网地址](https://www.elastic.co/cn/elasticsearch/)



### 概念

#### Index(索引)

* 相当于mysql的库

#### Type(类型)

* 相当于mysql的表

#### Document(文档)

* 数据记录
* 属性==>列名
* 数据都是json格式

#### 倒排索引

* 分词索引
* 相关性得分



### docker安装

```sh
# 下载镜像
docker pull elasticsearch:7.4.2
docker pull kibana:7.4.2

# 创建实例
mkdir -p /mydata/elasticsearch/config
mkdir -p /mydata/elasticsearch/data
mkdir -p /mydata/elasticsearch/plugins
echo "http.host: 0.0.0.0" >> /mydata/elasticsearch/config/elasticsearch.yml

# 修改权限
chmod -R 777 /mydata/elasticsearch/

# 启动
docker run --name es -p 9200:9200 -p 9300:9300 \
-e TZ=Asia/Shanghai \
-e "discovery.type=single-node" \
-e ES_JAVA_OPTS="-Xms64m -Xmx128m" \
-v /mydata/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
-v /mydata/elasticsearch/data:/usr/share/elasticsearch/data \
-v /mydata/elasticsearch/plugins:/usr/share/elasticsearch/plugins \
-d elasticsearch:7.4.2
```

#### 安装

##### 测试es

[http://192.168.56.111:9200/](http://192.168.56.111:9200/)

##### 启动Kibana

```
docker run --name kibana -e ELASTICSEARCH_HOSTS=http://192.168.56.111:9200 -p 5601:5601 \
-d kibana:7.4.2
```

[http://192.168.56.111:5601](http://192.168.56.111:5601)

##### Dev Tools

#### 检索

##### 1._cat

```sh
# 查看所有节点
GET /_cat/nodes

# 查看es健康状况
GET /_cat/heath

# 查看主节点
GET /_cat/master

# 查看所有索引 show databases;
GET /_cat/indices
```

##### 2.put

> 索引一个文档
>
> put请求更新，必须带id
>
> post不带id为新增，否则为修改

```json
PUT /sys/user/1
{
	"name": "张三"
}

# 乐观锁修改
PUT /sys/user/1?if_seq_no=4&if_primary_term=1
{
	"name": "张三"
}
```

##### 3.get

> 查询文档
>
> _seq_no和_primary_term是做并发控制的的字段，基于乐观锁



##### 4.更新文档

```sh
# 对比原来的属性，不一致才修改
POST /sys/user/1/_update
{
	“doc”:{
        "name": "张三"
    }
}

# 每次都更新，版本号增加
POST /sys/user/1
{
	"name": "张三"
}

# 与post一样
PUT /sys/user/1
{
	"name": "张三"
}


```

##### 5.删除文档&索引

```sh
# 删除文档
DELETE /sys/user/1

# 删除索引
DELETE sys
```

##### 6.bulk批量API

[测试数据下载](https://download.elastic.co/demos/kibana/gettingstarted/accounts.zip)

> 类型为text，不是application/json

```sh
# 批量新增
POST /sys/user/_bulk
{"index":{"_id":"2"}}
{"name": "李四"}
{"index":{"_id":"3"}}
{"name": "王五"}

# 复杂批量操作
POST /_bulk
{"delete": {"_index": "sys", "_type": "user", "_id": "4"}}
{"create": {"_index": "sys", "_type": "user", "_id": "4"}}
{"name": "小明"}
{"index": {"_index": "sys", "_type": "user"}}
{"name": "小明"}
{"update": {"_index": "sys", "_type": "user", "_id": "4"}}
{"doc": {"name": "小丽"}}
```





## 注意

| 关键词 | 描述                                                         |
| ------ | ------------------------------------------------------------ |
| must   | 查询的结果必须匹配查询条件，并且计算score。                  |
| filter | 查询的结果必须匹配查询条件，和must不太一样的是，不会计算score |
| should | 查询结果必须符合查询条件should中的一个或者多个，minimum_should_match参数定义了至少满足几个子句。会计算score。 |
| must   | 查询的结果必须不符合查询条件。                               |

* 必须匹配的条件写到must下面
* 其他查询条件放到filter
  * range 区间查询
* term精确匹配

* nested嵌入式的，不指定会拉平（相当于java的flatMap）,查询的时候也需要指定嵌入式查询
* highlight 高亮
