build.gradle
```
compile 'mysql:mysql-connector-java:5.1.38'
```

```
dataSource:
    pooled: true
    jmxExport: true
    driverClassName: com.mysql.jdbc.Driver
    username: root
    password: root

environments:
    development:
        dataSource:
            dbCreate: update
            url: jdbc:mysql://localhost/test?createDatabaseIfNotExist=true&useUnicode=true&amp;characterEncoding=UTF-8&useSSL=false
```
