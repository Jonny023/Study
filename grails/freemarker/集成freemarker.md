1、创建grails项目
build.gradle的dependencies中引入依赖
```
runtime "org.freemarker:freemarker:latest.release"
```
3、在grails-app/conf/spring/resources.groovy中注入dsl
```
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver

// Place your Spring DSL code here
beans = {
    freeMarkerConfigurer(FreeMarkerConfigurer) {
        templateLoaderPath = "/"
    }
    viewResolver(FreeMarkerViewResolver) {
        cache = "false"
        prefix = "bin/freemarker"
        suffix = ".ftl"
        exposeSpringMacroHelpers = "true"
//        requestContextAttribute = "request"
    }
}
```
4、在src/main/weapp下创建test目录，目录下创建index.ftl

index.ftl
```
<!DOCTYPE html>
<html class="loginHtml">
<head>
    <meta charset="utf-8">
    <title>test</title>
</head>
<body>
用户名：${user!}
<br/>
系统版本：${os!}
<br/>
JDK版本：${java_version!}
<br/>
Tomcat版本：${tomcat_version!}
</body>
</html>
```
5、在controller下创建TestController
```
package test

import org.apache.catalina.util.ServerInfo

class TestController {

    def index() {

        // 传值方式一
        //render view: "/test/index", model:[user: "test"]

        // 传值方式二
        request.setAttribute("user","test")

        //获取操作系统名称
        request.setAttribute("os",System.getProperty("os.name"))

        //获取JDK版本
        request.setAttribute("java_version",System.getProperty("java.version"))

        //获取服务器版本
        request.setAttribute("tomcat_version",request.getServletContext().getServerInfo())
        request.setAttribute("tomcat_version",ServerInfo.getServerInfo())
        render view: "/test/index"
    }
}
```
6、启动项目，访问http://localhost/test/index
