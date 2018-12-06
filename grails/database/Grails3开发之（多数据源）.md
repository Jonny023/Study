配置application.yml
```yaml
---
hibernate:
    cache:
        queries: false
        use_second_level_cache: true
        use_query_cache: false
        region.factory_class: 'org.hibernate.cache.ehcache.EhCacheRegionFactory'

#多个数据源
dataSources:
    dataSource:
        pooled: true
        jmxExport: true
        driverClassName: org.h2.Driver
        username: sa
        password:
    #数据源一
    dataSource_one:
        pooled: true
        jmxExport: true
        dialect: org.hibernate.dialect.MySQLInnoDBDialect
        driverClassName: com.mysql.jdbc.Driver
        username: root
        password: root
    #数据源二
    dataSource_two:
        pooled: true
        jmxExport: true
        dialect: org.hibernate.dialect.MySQLInnoDBDialect
        driverClassName: com.mysql.jdbc.Driver
        username: root
        password: root

environments:
    development:
        dataSources:
            dataSource:
                dbCreate: create-drop
                url: jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE
            #数据源一的url
            dataSource_one:
                dbCreate: update
                url: jdbc:mysql://localhost:3306/shop

            #数据源二的url
            dataSource_two:
                dbCreate: update
                url: jdbc:mysql://localhost:3306/guns
    test:
        dataSources:
            dataSource:
                dbCreate: update
                url: jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE
    production:
        dataSources:
            dataSource:
                dbCreate: update
                url: jdbc:h2:./prodDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE
                properties:
                    jmxEnabled: true
                    initialSize: 5
                    maxActive: 50
                    minIdle: 5
                    maxIdle: 25
                    maxWait: 10000
                    maxAge: 600000
                    timeBetweenEvictionRunsMillis: 5000
                    minEvictableIdleTimeMillis: 60000
                    validationQuery: SELECT 1
                    validationQueryTimeout: 3
                    validationInterval: 15000
                    testOnBorrow: true
                    testWhileIdle: true
                    testOnReturn: false
                    jdbcInterceptors: ConnectionState
                    defaultTransactionIsolation: 2 # TRANSACTION_READ_COMMITTED
            dataSource_one:
                dbCreate: update
                url: jdbc:mysql://localhost:3306/shop
                properties:
                    jmxEnabled: true
                    initialSize: 5
                    maxActive: 50
                    minIdle: 5
                    maxIdle: 25
                    maxWait: 10000
                    maxAge: 600000
                    timeBetweenEvictionRunsMillis: 5000
                    minEvictableIdleTimeMillis: 60000
                    validationQuery: SELECT 1
                    validationQueryTimeout: 3
                    validationInterval: 15000
                    testOnBorrow: true
                    testWhileIdle: true
                    testOnReturn: false
                    jdbcInterceptors: ConnectionState
                    defaultTransactionIsolation: 2 # TRANSACTION_READ_COMMITTED

            dataSource_two:
                dbCreate: update
                url: jdbc:mysql://localhost:3306/guns
                properties:
                    jmxEnabled: true
                    initialSize: 5
                    maxActive: 50
                    minIdle: 5
                    maxIdle: 25
                    maxWait: 10000
                    maxAge: 600000
                    timeBetweenEvictionRunsMillis: 5000
                    minEvictableIdleTimeMillis: 60000
                    validationQuery: SELECT 1
                    validationQueryTimeout: 3
                    validationInterval: 15000
                    testOnBorrow: true
                    testWhileIdle: true
                    testOnReturn: false
                    jdbcInterceptors: ConnectionState
                    defaultTransactionIsolation: 2 # TRANSACTION_READ_COMMITTED

---
---
grails:
    profile: web
    codegen:
        defaultPackage: grails_multi_datasource
    spring:
        transactionManagement:
            proxies: false
info:
    app:
        name: '@info.app.name@'
        version: '@info.app.version@'
        grailsVersion: '@info.app.grailsVersion@'
spring:

    groovy:
        template:
            check-template-location: false

---
grails:
    mime:
        disable:
            accept:
                header:
                    userAgents:
                        - Gecko
                        - WebKit
                        - Presto
                        - Trident
        types:
            all: '*/*'
            atom: application/atom+xml
            css: text/css
            csv: text/csv
            form: application/x-www-form-urlencoded
            html:
              - text/html
              - application/xhtml+xml
            js: text/javascript
            json:
              - application/json
              - text/json
            multipartForm: multipart/form-data
            pdf: application/pdf
            rss: application/rss+xml
            text: text/plain
            hal:
              - application/hal+json
              - application/hal+xml
            xml:
              - text/xml
              - application/xml
    urlmapping:
        cache:
            maxsize: 1000
    controllers:
        defaultScope: singleton
    converters:
        encoding: UTF-8
    views:
        default:
            codec: html
        gsp:
            encoding: UTF-8
            htmlcodec: xml
            codecs:
                expression: html
                scriptlets: html
                taglib: none
                staticparts: none
endpoints:
    jmx:
        unique-names: true
```

> 使用数据源的时候请注意，官网文档还有网上很多解释都是用def dataSource_one这样来注入数据源，经过几番测试，事实并不是这样，默认的数据源def > dataSource可以在service和controller这样来注入，但是如果定义了多数据源，比如在service或者controller中注入数据源，必须以"dataSource_"开头来注入，> 其他方式注入都为null，注入方式如下：

```groovy
package grails_multi_datasource

import groovy.sql.Sql

class TestService {

    //注入数据源一,我自定一的数据源配置为dataSource_one
    //必须以dataSource_开头来注入，默认的dataSource可以直接def dataSource就行
    def dataSource_dataSource_one

    def one() {
        Sql sql = new Sql(dataSource_dataSource_one)
        return sql.rows("SELECT * FROM `t_role`")
    }

}
```

在domain中指定单个数据源：

```groovy
package com.test

class Test {

    String name
    String title
    String context

    static constraints = {

    }

    static mapping = {
        datasource "dataSource_one"
    }
}
```

在domain中指定多个数据源：

```groovy
package com.test

class Test {

    String name
    String title
    String context

    static constraints = {

    }

    static mapping = {
        datasources (["dataSource_one","dataSource_two"])
    }
}

//or 如果配置文件dataSource还在，并且要在所有数据库生成表，用这个
class ZipCode {

   String code

   static mapping = {
      datasource 'ALL' //所有数据库都生成此表
   }
}

//or
class ZipCode {

   String code

   static mapping = {
      datasources(['lookup', 'DEFAULT']) //DEFAULT代表默认的dataSource
   }
}
```

在程序中应用指定的数据源：(一)

```groovy
class DatabaseService {

    static datasource = 'raw'
    DataSource dataSource

    //这样也可以，不过是获取当前使用的数据源
    def sql = new Sql(sessionFactory.currentSession.connection())

}
```

(二)

```groovy
class DatabaseService {

    @Autowired 
    @Qualifier('dataSource_raw')
    DataSource dataSource

    @Autowired
    @Qualifier("transactionManager_asi")
    PlatformTransactionManager transactionManager

}
```

(三)

```groovy
class DatabaseService {

    @Resource(name = "dataSource_raw")
    DataSource dataSource

}
```

在controller和service的使用：

1、def dataSource这种方式注入他是按照domain mapping里面datasource ["one","two"]这个顺序来的，如果注入dataSource，则访问的数据源为one,如需访问two这个数据源的数据，使用如下：

```groovy
class TestController{

   def index() {
       //访问对应数据源的方式为：domain.dataSource_+数据源名.方法
       render Test.dataSource_two.list()
   }
  
}
```
