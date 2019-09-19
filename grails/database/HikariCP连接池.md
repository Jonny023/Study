# HikariCP连接池

* `build.gradle`中引入依赖

```groovy
compile 'com.zaxxer:HikariCP:3.3.1'
```

* 用了`HikariCP连接池`需要移除默认的`tomcat-jdbc`连接池

```groovy
configurations {
//    compile.exclude module: 'commons'
    all*.exclude group: 'org.apache.tomcat', module: 'tomcat-jdbc'
}
```

* 修改`application.yml`配置，连接池`properties`下面所有都注释掉

```yaml
dataSource:
    # 关闭默认连接池
    pooled: false
#    jmxExport: true
    driverClassName: '${jdbc.driver}'
    username: '${jdbc.username}'
    password: '${jdbc.password}'
    url: '${jdbc.url}'
    dialect: org.hibernate.dialect.MySQL5InnoDBDialect
```

* 在`grails-app/conf/spring/resources.groovy`中新增配置

```groovy
package spring

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import grails.util.Holders

// Place your Spring DSL code here
beans = {

    def config = Holders.config

    hikari(HikariDataSource, { bean ->
        Properties hp = new Properties()
        hp.username = config.dataSource.username
        hp.password = config.dataSource.password
        hp.connectionTimeout = 6000
        hp.maximumPoolSize = 60
        hp.jdbcUrl = config.dataSource.url
        hp.driverClassName = config.dataSource.driverClassName

        // Hikari连接池
        HikariConfig hc = new HikariConfig(hp)
        bean.constructorArgs = [hc]
    })

}

```

* 启动项目查看日志

```java
17:55:02.779 [main] INFO  com.zaxxer.hikari.HikariDataSource (HikariDataSource.java:80) - HikariPool-1 - Starting...
17:55:02.827 [main] INFO  com.zaxxer.hikari.HikariDataSource (HikariDataSource.java:82) - HikariPool-1 - Start completed.
```

### 用了`HikariCP`连接池还能实现自动重连，比如：数据库宕了，重启数据库，程序也能自动连接上数据库

## 注意

* grails3.2+应该这样用

```ymal
dataSource:
    pooled: false
    jmxExport: true
    driverClassName: '${jdbc.driver}'
    username: '${jdbc.username}'
    password: '${jdbc.password}'
    type: com.zaxxer.hikari.HikariDataSource
    properties:
        maximumPoolSize: 20
```
