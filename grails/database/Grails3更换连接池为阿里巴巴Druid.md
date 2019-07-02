## Grails3更换连接池为阿里巴巴Druid

> Grails3默认数据库连接池为`tomcat-jdbc pool`，性能较`druid`、`HikariCP`差太多，所以想换成其他连接池

* 由于默认使用的是`tomcat-jdbc`连接池，需要去掉依赖

```groovy
//    runtime "com.h2database:h2"
//    runtime "org.apache.tomcat:tomcat-jdbc"

// 添加阿里巴巴druid连接池
compile group: 'com.alibaba', name: 'druid', version: '1.1.18'
compile 'mysql:mysql-connector-java:5.1.40'
```

* `grails-app/conf/applicaiton.yml`配置也要注释掉默认的连接池

```yaml
dataSource:
#    pooled: true
    jmxExport: true
    driverClassName: com.mysql.jdbc.Driver
    username: root
    password: root
    dialect: org.hibernate.dialect.MySQL5InnoDBDialect
    url: jdbc:mysql://localhost:3306/g3?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false
    # 为了兼容grails，让Hibernate自动建表，所以在dataSource下增加连接池相关的配置
    druid:
        minIdle: 1
        maxActive: 40
        maxWait: 60000
        initialSize: 20
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        removeAbandoned: true
        filters: stat,wall,slf4j
        validationQuery: select 'x'
        useGlobalDataSourceStat: true
        poolPreparedStatements: true
        minEvictableIdleTimeMillis: 300000
        timeBetweenEvictionRunsMillis: 60000
        connectionProperties: clientEncoding=UTF-8
        maxPoolPreparedStatementPerConnectionSize: 20
environments:
    development:
        dataSource:
            dbCreate: create-drop
            url: jdbc:mysql://localhost:3306/g3?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false
    test:
        dataSource:
            dbCreate: update
            url: jdbc:mysql://localhost:3306/g3?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false
    production:
        dataSource:
            dbCreate: update
            url: jdbc:mysql://localhost:3306/g3?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false
server:
  port: 8081  # TRANSACTION_READ_COMMITTED
```

* `grails-app/conf/spring/resources.groovy`配置

```groovy
// Place your Spring DSL code here
beans = {

    // 监控管理页面配置（管理用户名、密码）
    druidConsoleServlet(org.springframework.boot.web.servlet.ServletRegistrationBean) {
        servlet = bean(com.alibaba.druid.support.http.StatViewServlet)
        urlMappings = ["/druid/*"]
//        urlMappings = ["*.js","*.gif","*.jpg","*.png","*.css","*.ico","/druid/*"]
        initParameters = [
                "loginUsername": "root",
                "loginPassword": "123456",
                "allow": "127.0.0.1",
                "deny": ""
        ]
//        loadOnStartup = 2
    }


    // alibaba druid配置
    dataSource(com.alibaba.druid.pool.DruidDataSource) { bean ->
        bean.initMethod = 'init'
        bean.destroyMethod = 'close'
        driverClassName = grailsApplication.config.dataSource.driverClassName
        url = grailsApplication.config.dataSource.url
        username = grailsApplication.config.dataSource.username
        password = grailsApplication.config.dataSource.password
        initialSize = grailsApplication.config.dataSource.druid.initialSize
        minIdle = grailsApplication.config.dataSource.druid.minIdle
        maxActive = grailsApplication.config.dataSource.druid.maxActive
        maxWait = grailsApplication.config.dataSource.druid.maxWait
        timeBetweenEvictionRunsMillis = grailsApplication.config.dataSource.druid.timeBetweenEvictionRunsMillis
        minEvictableIdleTimeMillis = grailsApplication.config.dataSource.druid.minEvictableIdleTimeMillis
        validationQuery = grailsApplication.config.dataSource.druid.validationQuery
        removeAbandoned = grailsApplication.config.dataSource.druid.removeAbandoned
        testWhileIdle = grailsApplication.config.dataSource.druid.testWhileIdle
        connectionProperties = grailsApplication.config.dataSource.druid.connectionProperties
        testOnBorrow = grailsApplication.config.dataSource.druid.testOnBorrow
        poolPreparedStatements = grailsApplication.config.dataSource.druid.poolPreparedStatements
        testOnReturn = grailsApplication.config.dataSource.druid.testOnReturn
        useGlobalDataSourceStat = grailsApplication.config.dataSource.druid.useGlobalDataSourceStat
        maxPoolPreparedStatementPerConnectionSize = grailsApplication.config.dataSource.druid.maxPoolPreparedStatementPerConnectionSize
        filters = grailsApplication.config.dataSource.druid.filters
    }
}
```

* `grails-app/conf/logback.groovy`添加日志配置

```groovy
// 日志采集
logger('com.alibaba', DEBUG, ['STDOUT'], false)
```

* 启动项目，出现日志

```bash
2019-07-03 03:15:06.691  WARN --- [           main] com.alibaba.druid.pool.DruidDataSource   : removeAbandoned is true, not use in productiion.
2019-07-03 03:15:07.078  INFO --- [           main] com.alibaba.druid.pool.DruidDataSource   : {dataSource-1} inited
Grails application running at http://localhost:8081 in environment: development
```

* `web`监控地址

```
http://127.0.0.1:8081/druid/index.html
```



### 总结

> `web`监控无法记录`sql`日志，防火墙也没用起来，只是连接池生效了，如何测试连接池生效呢？关闭数据库服务，访问一下，控制台报无法连接数据库的错误，重启数据库服务，再次刷新页面，数据库连接上，能够取到数据，希望有兴趣的朋友可以研究下如何在web端能跟踪`sql`，以及把防火墙用起来。

* [项目地址](https://gitee.com/GntLee/grails3-security)

### 参考

* [参考地址1](https://blog.51cto.com/xinzhilian/2082954)
* [参考地址2](http://cn.voidcc.com/question/p-wsozecww-bma.html)
* [Grails2参考](http://www.tothenew.com/blog/grails-filter-at-top-of-filter-invocation-chain/)

****

