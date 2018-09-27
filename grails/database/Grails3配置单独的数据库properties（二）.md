* 在grails-app/conf下创建application-jdbc.properties
```
username=root
password=password
driver=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/test?characterEncoding=UTF-8&useSSL=false
```
* 然后在application.yml中添加配置
```
spring:
    profiles:
        include:
            test,api,jdbc

---

dataSource:
    pooled: true
    jmxExport: true
    driverClassName: ${driver}
    username: ${username}
    password: ${password}

environments:
    development:
        dataSource:
            dbCreate: create-drop
            url: ${url}
```

### 请注意，spring.profiles.include这部分放到application.yml最前面
