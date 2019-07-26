# 以war形式部署springboot项目

> 把springboot以war形式部署到Servlet容器

### 修改maven项目的打包方式

```xml
<packaging>war</packaging>
```

### 修改maven项目中，嵌入式Servlet容器的依赖范围

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-tomcat</artifactId>
	<scope>provided</scope>
</dependency>
```

### 继承类 `SpringBootServletInitializer`

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class JavawebApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(JavawebApplication.class, args);
	}

	/**
	 * 覆写方法
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		// 指定@SpringBootApplication类
		return application.sources(JavawebApplication.class);
	}
}
```

[文章来自](https://springboot.io/t/topic/97)
