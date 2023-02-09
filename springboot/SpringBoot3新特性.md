## springboot3新特性



### 自定义starter

* springboot2
  * 在`META-INF/spring.factories`中配置`org.springframework.boot.autoconfigure.EnableAutoConfiguration=`
* springboot3
  * `META-INF/spring.factories`中配置如`org.springframework.context.ApplicationListener=`监听器、过滤器等
  * `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`中配置用户自定义配置类，如：`com.example.spring3demo.config.JacksonConfig`
