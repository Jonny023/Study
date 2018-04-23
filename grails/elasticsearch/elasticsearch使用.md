1、新建一个grails项目（此步骤略）

2、打开build.gradle，在dependencies中加入依赖：
```
//全文检索
compile "org.grails.plugins:elasticsearch:1.2.1"
compile "org.elasticsearch:elasticsearch:2.3.3"
```
注：此处必须添加两个依赖，不然启动会报错，研究半天才发现需要同时添加两个依赖才不会报错。官方文档github里面只添加了compile "org.grails.plugins:elasticsearch:1.2.1"，启动的时候会报：Caused by: java.lang.ClassNotFoundException: org.elasticsearch.cluster.health.ClusterHealthStatus这个错误。

3、在grails-app/conf下面新建一个application的groovy脚本
```
//management.health.elasticsearch.enabled=false
elasticSearch {
    //MongoDB数据库用：mongoDatastore，Hibernate用：hibernateDatastore
    datastoreImpl = 'hibernateDatastore'
}
```
4、在application.yml中添加
```
elasticSearch:
    date:
        formats: ["yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"]
    client.hosts:
        - {host: localhost, port: 8088}
    defaultExcludedProperties: ['password']
    disableAutoIndex: false
    index:
        compound_format: true
    unmarshallComponents: true
    searchableProperty:
        name: searchable
    includeTransients: true
```
5、在需要支持全文检索的domain中启用检索支持：
```
package com.lee

class Student {

    //启用检索支持
    static searchable = true

    String stuName
    String sex
    Integer age

    static constraints = {
        stuName nullable: true
        sex nullable: true
        age nullable: false
    }
}
```
6、在控制器中注入service

//注入全文检索服务
```
def elasticSearchService
```
7、调用检索方法（两种方法）
```
def index(Integer max) {

    //第一种
//        elasticSearchService.search("张").searchResults.each {result->
//            println result.stuName
//        }

    //第二种
    println "======="+ Student.search("三")

    params.max = Math.min(max ?: 10, 100)
    respond Student.list(params), model:[studentCount: Student.count()]
}
    
```
