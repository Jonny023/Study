> 依赖

```java
compile group: 'com.microsoft.sqlserver', name: 'sqljdbc4', version: '4.0'
```

> 数据库配置

```yaml
dataSource:
    pooled: true
    jmxExport: true
    driverClassName: com.microsoft.sqlserver.jdbc.SQLServerDriver
    username: sa
    password: root

environments:
    development:
        dataSource:
            dbCreate: update
            url: jdbc:sqlserver://192.168.1.11:1433;DatabaseName=test
```
