在build.gradle中加入bootRun，这是加在最后面的，这里随意，貌似会加载application开头的properties的配置文件。

```groovy
bootRun {
    systemProperties = System.properties
}
```

在grails-app/conf下面新建application.properties，定义好数据库信息

```
jdbc.username=root
jdbc.password=root
jdbc.created=update
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/test?useUnicode=true&amp;characterEncoding=UTF-8
```

接下来就是在grails-app/conf/application.yml中动态调用application.properties配置

```yaml
---
hibernate:
    cache:
        queries: false
        use_second_level_cache: true
        use_query_cache: false
        region.factory_class: 'org.hibernate.cache.ehcache.EhCacheRegionFactory'

dataSource:
    pooled: true
    jmxExport: true
    driverClassName: '${jdbc.driver}'
    username: '${jdbc.username}'
    password: '${jdbc.password}'

environments:
    development:
        dataSource:
            dbCreate: '${jdbc.created}'
            url: '${jdbc.url}'
    test:
        dataSource:
            dbCreate: '${jdbc.created}'
            url: '${jdbc.url}'
    production:
        dataSource:
            dbCreate: '${jdbc.created}'
            url: '${jdbc.url}'
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
        defaultPackage: frame
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

用了这种方式加载配置文件，如果要在后台获取配置文件中定义的数据可以用grailsApplication来获取。

```groovy
grailsApplication.config.getProperty('jdbc.url')
```
over!
