## ElasticSearch

[官网地址](https://www.elastic.co/cn/elasticsearch/)

[文档地址](https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html)

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

# 增大内存
docker run --name es -p 9200:9200 -p 9300:9300 \
-e TZ=Asia/Shanghai \
-e "discovery.type=single-node" \
-e ES_JAVA_OPTS="-Xms64m -Xmx512m" \
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



#### 进阶检索

[官方文档](https://www.elastic.co/guide/en/elasticsearch/reference/7.5/getting-started-search.html)

##### 检索

* 检索1

> url参数

```sh
GET /bank/_search?q=*&&sort=account_number:asc
```

* 检索2(QueryDSL)

> 请求体

```sh
GET /bank/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "account_number": "asc",
      "age": "desc"
    }
  ]
}
```

##### 分页

> from，size

```json
GET /bank/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "balance": {
        "order": "desc"
      }
    }
  ],
  "from": 5,
  "size": 5
}
```

##### 返回字段

> _source

```sh
GET /bank/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "balance": {
        "order": "desc"
      }
    }
  ],
  "from": 5,
  "size": 5,
  "_source": [
    "firstname",
    "age",
    "balance"
  ]
}
```

##### match

> 全文检索

```sh
GET /bank/_search
{
  "query": {
    "match": {
      "address": "Kings"
    }
  }
}
```

##### match_parse

> 匹配短语，不可分割

```sh
GET /bank/_search
{
  "query": {"match_phrase": {
    "address": "mill lane"
  }}
}
```

##### multi_match

> 多字段匹配，会分词

```sh
GET /bank/_search
{
  "query": {
    "multi_match": {
      "query": "mill",
      "fields": [
        "address",
        "city"
      ]
    }
  }
}

# 属性.keyword实现精确查询
GET /bank/_search
{
  "query": {
    "multi_match": {
      "query": "Nogal",
      "fields": [
        "address.keyword",
        "city.keyword"
      ]
    }
  }
}
```

##### 复合查询

```sh
GET /bank/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "gender": "M"
          }
        },
        {
          "match": {
            "address": "mill"
          }
        }
      ],
      "must_not": [
        {
          "match": {
            "age": "20"
          }
        }
      ],
      "should": [
        {
          "match": {
            "lastname": "Holland"
          }
        }
      ]
    }
  }
}
```

##### filter(结果过滤)

> 不会计算相关性得分

```sh
GET /bank/_search
{
  "query": {
    "bool": {
      "filter": [
        {
          "range": {
            "age": {
              "gte": 20,
              "lte": 30
            }
          }
        }
      ]
    }
  }
}
```

##### term

> 精确查询用term(非text字段)，如age，**全文检索用match，非全文检索用term**

```sh
GET /bank/_search
{
  "query": {
    "term": {
      "state": "va"
    }
  }
}
```

##### aggregations(聚合)

[官方文档](https://www.elastic.co/guide/en/elasticsearch/reference/7.5/search-aggregations-metrics.html)

```sh
## 按年龄聚合，平均工资，平均年龄，size: 0只查看聚合数据
GET /bank/_search
{
  "query": {
    "match": {
      "address": "mill"
    }
  },
  "aggs": {
    "aggAgg": {
      "terms": {
        "field": "age",
        "size": 10
      }
    },
    "ageAvg": {
      "avg": {
        "field": "age"
      }
    },
    "balanceAvg": {
      "avg": {
        "field": "balance"
      }
    }
  },
  "size": 0
}

## 按照年龄聚合，并求这些人的平均工资
GET /bank/_search
{
  "query": {
    "match_all": {}
  },
  "aggs": {
    "ageAgg": {
      "terms": {
        "field": "age",
        "size": 1000
      },
      "aggs": {
        "blanceAvg": {
          "avg": {
            "field": "balance"
          }
        }
      }
    }
  },
  "size": 0
}

## 统计不同年龄人数，不同年龄男女人数，以及男女平均工资
GET /bank/_search
{
  "query": {
    "match_all": {}
  },
  "aggs": {
    "avgAgg": {
      "terms": {
        "field": "age"
      },
      "aggs": {
        "genderAgg": {
          "terms": {
            "field": "gender.keyword",
            "size": 10
          },
          "aggs": {
            "blanceAgg": {
              "avg": {
                "field": "balance"
              }
            }
          }
        }
      }
    }
  },
  "size": 0
}

```

#### Mapping

[官方文档](https://www.elastic.co/guide/en/elasticsearch/reference/7.5/mapping-types.html)

> 6.0以后移除了类型的概念，去除type为了提高ES效率
>
> Mapping相当于声明数据字段类型

```sh
## 查看映射类型
GET /bank/_mapping

## 创建索引并映射
PUT /my-index
{
  "mappings": {
    "properties": {
      "age": {"type": "integer"},
      "email": {"type": "keyword"},
      "name": {"type": "text"}
    }
  }
}

## 给索引添加新字段
PUT /my-index/_mapping
{
  "properties": {
    "employee-id": {
      "type": "keyword",
      "index": false
    }
  }
}

## 想要更新映射需要创建新的索引，然后通过数据迁移把数据迁移过来
PUT /newbank
{
  "mappings": {
    "properties": {
      "account_number": {
        "type": "long"
      },
      "address": {
        "type": "text"
      },
      "age": {
        "type": "long"
      },
      "balance": {
        "type": "long"
      },
      "city": {
        "type": "text"
      },
      "email": {
        "type": "text"
      },
      "employer": {
        "type": "text"
      },
      "firstname": {
        "type": "text"
      },
      "gender": {
        "type": "text"
      },
      "lastname": {
        "type": "text"
      },
      "state": {
        "type": "text"
      }
    }
  }
}
```

##### 数据迁移

```sh
##固定写法
POST _reindex 

## 将bank下的account迁移到newbank
POST _reindex
{
  "source": {
    "index": "bank",
    "type": "account"
  },
  "dest": {
    "index": "newbank"
  }
}
```

#### 分词器

[官方测试](https://www.elastic.co/guide/en/elasticsearch/reference/7.5/test-analyzer.html)

> 默认只支持英文分词，中文分词需要安装分词器

[IK分词器](https://github.com/medcl/elasticsearch-analysis-ik)





##### 安装分词器

> 分词器要和es版本一致，可以通过http://ip:9200查看es版本

```sh
# 安装wget
yum install wget

# 可以直接命令下载，也可以外部下载再上传
wget https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.4.2/elasticsearch-analysis-ik-7.4.2.zip

# 查看zip文件内容
unzip -l elasticsearch-analysis-ik-7.4.2.zip

# 解压
unzip elasticsearch-analysis-ik-7.4.2.zip -d /mydata/elasticsearch/plugins/ik

# 删除文件，不然无法启动
rm -f elasticsearch-analysis-ik-7.4.2.zip

# 修改权限
chmod 777 -R ik

# 重启es
```



##### 使用分词器

```sh
# 内置分词器
POST /_analyze
{
  "analyzer": "standard",
  "text": "Hello world , how are you?"
}

# ik分词
POST /_analyze
{
  "analyzer": "ik_smart",
  "text": "好好学习天天向上"
}

POST /_analyze
{
  "analyzer": "ik_max_word",
  "text": "好好学习天天向上"
}
```



##### 安装nginx

```sh
mkdir -p /mydata/nginx

# 启动一个容器
docker run -p 80:80 --name nginx -d nginx:1.10

cd /mydata/nginx

# 拷贝配置到容器外部
docker cp nginx:/etc/nginx .

# 删除容器
docker rm -f nginx

mv nginx conf

chmod 777 -R /mydata/nginx/

# 启动一个新容器
docker run -p 80:80 --name nginx \
-v /mydata/nginx/html:/usr/share/nginx/html \
-v /mydata/nginx/logs:/var/log/nginx \
-v /mydata/nginx/conf:/etc/nginx \
-d nginx:1.10
```

##### 创建es分词资源

```sh
cd /mydata/nginx/html

mkdir -p es

cd es

vi fenci.txt

# 输入内容
尚硅谷
人民政府

# 访问地址
ip:80/es/fenci.txt
```

#### 自定义分词

```sh
vi /mydata/elasticsearch/plugins/ik/config/IKAnalyzer.cfg.xml

# 配置远程分词地址
<entry key="remote_ext_dict">http://192.168.56.111/es/fenci.txt</entry>

# 重启docker容器
docker restart es
```

