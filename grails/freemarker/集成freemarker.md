Grails3集成freemarker模板引擎

开发环境：

```bash
| Grails Version: 3.1.5
| Groovy Version: 2.4.6
| JVM Version: 1.8.0_141
    
```

1、创建`grails`项目
    

2、在`build.gradle`的`dependencies`中引入依赖

```bash
runtime "org.freemarker:freemarker:latest.release"
    
```
    
3、在`grails-app/conf/spring/resources.groovy`中注入`dsl`

```groovy
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver

// Place your Spring DSL code here
beans = {
    freeMarkerConfigurer(FreeMarkerConfigurer) {
        // 模板直接放src/main/webapp下，存在安全问题
        // templateLoaderPath = "/"

        // 模板文件存放在src/main/webapp/WEB-INF/templates下面
        templateLoaderPath = "/WEB-INF/templates/"
    }
    viewResolver(FreeMarkerViewResolver) {
        cache = "false"
        prefix = "bin/freemarker"
        suffix = ".ftl"
        exposeSpringMacroHelpers = "true"
        // requestContextAttribute = "request"
    }
}
```     
    
4、在`src/main/weapp/WEB-INF/templates`下创建`test`目录，目录下创建`index.ftl`

```html
index.ftl
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
 
5、在`controller`下创建`TestController`

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

# 注意：
* 建议模板文件存放于`src/main/webapp/WEB-INF/templates/`下面
    * 若在`src/main/webapp/WEB-INF/templates/`下面，则配置为：
    ```groovy
    import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer
    import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver
    
    // Place your Spring DSL code here
    beans = {
        freeMarkerConfigurer(FreeMarkerConfigurer) {
            // 模板文件存放在src/main/webapp/WEB-INF/templates下面
            templateLoaderPath = "/WEB-INF/templates/"
        }
        viewResolver(FreeMarkerViewResolver) {
            cache = "false"
            prefix = "bin/freemarker"
            suffix = ".ftl"
            exposeSpringMacroHelpers = "true"
            // requestContextAttribute = "request"
        }
    }
    ```
    
* 直接存放在`src/main/webapp/`有安全问题，经测试，若直接存放于`webapp`下，可以通过直接访问`http://localhost:8080/xxx/index.ftl`得到源文件，可能造成安全隐患
* 控制器中`render view: "/view"`，若原项目存在`grails-app/views/index.gsp`文件，则访问的是这个`index.gsp`文件，需要删除后才能访问`index.ftl`模板文件
