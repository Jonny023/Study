# Grails3中如何注册Filter、Servlet、bean

> `Springboot`提供了专门针对`servlet`和`filter`的注册类

* `org.springframework.boot.web.servlet.FilterRegistrationBean` - `Filter`注册
* `org.springframework.boot.web.servlet.ServletRegistrationBean` - `Servlet`注册

* 还可以在`grails-app/conf/spring`目录下创建`spring`配置，如`resources.xml`按照传统方式注入

```xml
<?xml version="1.0" encoding="gbk"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:mvc="http://www.springframework.org/schema/mvc" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:p="http://www.springframework.org/schema/p" xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
		http://www.springframework.org/schema/beans/spring-beans-4.1.xsd
		http://www.springframework.org/schema/context
		http://www.springframework.org/schema/context/spring-context-4.1.xsd
		http://www.springframework.org/schema/mvc
		http://www.springframework.org/schema/mvc/spring-mvc-4.1.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">

    <!-- 配置数据源-druid -->
    <!--<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">-->
        <!--<property name="url" value="${dataSource.url}" />-->
        <!--<property name="username" value="${dataSource.username}" />-->
        <!--<property name="password" value="${dataSource.password}" />-->
        <!--<property name="filters" value="stat,wall,slf4j" />-->
        <!--<property name="maxActive" value="20" />-->
        <!--<property name="initialSize" value="1" />-->
        <!--<property name="maxWait" value="60000" />-->
        <!--<property name="minIdle" value="1" />-->
        <!--<property name="timeBetweenEvictionRunsMillis" value="60000" />-->
        <!--<property name="minEvictableIdleTimeMillis" value="300000" />-->
        <!--<property name="testWhileIdle" value="true" />-->
        <!--<property name="testOnBorrow" value="false" />-->
        <!--<property name="testOnReturn" value="false" />-->
        <!--<property name="connectionProperties" value="druid.stat.mergeSql=true;druid.stat.slowSqlMillis=1" />-->
        <!--<property name="poolPreparedStatements" value="true" />-->
        <!--<property name="maxOpenPreparedStatements" value="20" />-->
    <!--</bean>-->

</beans>
```

### 普通`bean`注入

```groovy
beans = {
  userService(com.system.UserServiceImpl)
}
```

### 注册`Fervlet`

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

### 注册`Filter`

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
