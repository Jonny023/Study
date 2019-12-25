### springboot + jsp

* 依赖

```groovy
compile "javax.servlet:jstl"
compileOnly "org.apache.tomcat.embed:tomcat-embed-jasper"

compileOnly("org.springframework.boot:spring-boot-devtools")
```

* application.properties配置

```properties
spring.mvc.view.suffix=.jsp
spring.mvc.view.prefix=/WEB-INF/jsp/

#热部署生效
spring.devtools.restart.enabled= true
#设置重启的目录
spring.devtools.restart.additional-paths=src/main/java
#classpath目录下的WEB-INF文件夹内容修改不重启
spring.devtools.restart.exclude= WEB-INF/**
```

* 代码示例

```java
@RequestMapping("/")
public ModelAndView index() {
    return new ModelAndView("/index");
}
```
