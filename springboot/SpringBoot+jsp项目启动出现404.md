> 通过maven创建springboot项目启动出现404

* `application.properties`配置

```properties
spring.mvc.view.prefix=/WEB-INF/jsp/
spring.mvc.view.suffix=.jsp
```

* 项目结构 

![](https://javaweb-community.oss-cn-beijing.aliyuncs.com/2019/0228/006e89c6fc8d481c967d2f82f877bb7b.png)

* 控制器方法

```java
package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }
}

```

* 启动项目访问`localhost:8080`，出现`404`

```
Whitelabel Error Page
This application has no explicit mapping for /error, so you are seeing this as a fallback.

Thu Feb 28 22:59:29 CST 2019
There was an unexpected error (type=Not Found, status=404).
No message available
```

## 解决方法

* `pom.xml`添加依赖

```
<dependency>
	<groupId>org.apache.tomcat.embed</groupId>
	<artifactId>tomcat-embed-jasper</artifactId>
</dependency>
<dependency>
	<groupId>javax.servlet</groupId>
	<artifactId>jstl</artifactId>
</dependency>
```

* `clean`并刷新`maven`

![](https://javaweb-community.oss-cn-beijing.aliyuncs.com/2019/0228/14914e59655743f5b077444b2304bddb.png)

* 重启并访问`localhost:8080`

![](https://javaweb-community.oss-cn-beijing.aliyuncs.com/2019/0228/6fb719894d184feeb19fa280e6c38e22.png)