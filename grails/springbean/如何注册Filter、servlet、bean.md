# Grails3中如何注册Filter、Servlet、bean

> `Springboot`提供了专门针对`servlet`和`filter`的注册类

* `org.springframework.boot.web.servlet.FilterRegistrationBean` - `Filter`注册
* `org.springframework.boot.web.servlet.ServletRegistrationBean` - `Servlet`注册

* 普通`bean`注入

```groovy
beans = {
  userService(com.system.UserServiceImpl)
}
```

* 注册`Fervlet`

```groovy
druidConsoleServlet(org.springframework.boot.web.servlet.ServletRegistrationBean) {
    servlet = bean(com.alibaba.druid.support.http.StatViewServlet)
    urlMappings = ["/druid/*"]
    initParameters = [
            "loginUsername": "root",
            "loginPassword": "123456",
            "allow"        : "127.0.0.1",
            "deny"         : ""
    ]
}
```

* 注册`Filter`

```groovy
webStatFilter(org.springframework.boot.web.servlet.FilterRegistrationBean) {
    filter = bean(com.alibaba.druid.support.http.WebStatFilter)
    // 对应web.xml中的init-param
    initParameters = [
            "exclusions"          : "*.js,*.gif,*.jpg,*.png,*.svg,*.css,*.ico,/druid/*",
            "sessionStatMaxCount" : "2000",
            "sessionStatEnable"   : "true",
            "principalCookieName": "verifyCode",
            "profileEnable"       : "true"
    ]
    // 对应web.xml中的url-pattern
    urlPatterns = ["/*"]
}

```
