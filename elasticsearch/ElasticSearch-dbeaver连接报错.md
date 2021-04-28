* 查看es信息

```
curl -X get "http://localhost:9200/_license"
```



* 官方说JDBC客户端需要只是`白金级`才能连接
  * [修改成30天试用版](https://www.elastic.co/guide/en/elasticsearch/reference/master/start-trial.html)

* 修改为试用

```
curl -X post "http://localhost:9200/_license/start_trial?acknowledge=true&pretty"
```

* 客户端连接驱动地址

```
https://www.elastic.co/cn/downloads/jdbc-client
```

