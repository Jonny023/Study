build.gradle
```
compile group: 'com.oracle', name: 'ojdbc6', version: '11.2.0.3'
```


```
dataSource:
    pooled: true
    jmxExport: true
    driverClassName: oracle.jdbc.driver.OracleDriver
    username: root
    password: root

environments:
    development:
        dataSource:
            dbCreate: update
            url: jdbc:oracle:thin:@localhost:1521:test
```
