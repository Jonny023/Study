# 使用spring-session时，动态修改cookie的max-age

不论是使用spring提供的`spring-session`，还是使用servle容器实现的`http session`。原理都是把`session-id`以`cookie`的形式存储在客户端。每次请求都带上`cookie`。服务器通过`session-id`，找到`session`。

## spring-session的使用

 

> springboot中整合使用spring-session spring-session 它可以替代 HttpSesession。而且改动极小，对应用透明。底层可以使用内存，Redis等存储Session信息。通过Redis这种方式可以做到Session共享，在集群环境中所有节点共享Session。 文档 [https://docs.spring.io/spring-session/docs/c…](https://docs.spring.io/spring-session/docs/current/reference/html5/)

## 由“记住我”引发的一个问题

用户登录的时候，通常需要一个【记住我】的选择框，表示是否要长期的保持会话。

【记住我】×
一般会把`cookie`的`max-age`设置为` -1`，表示在浏览器关闭的时候，就自动的删除cookie。对于客户端而言关闭了浏览器，就是丢失了会话，需要重新的登录系统。特别在公共场合登陆了某些系统后，忘记执行‘退出’操作，直接关闭了浏览器，后面使用电脑的人打开浏览器，也必须先登录才可以访问系统。这样在一定的程度上保证了安全性。

【记住我】√
一般在自己私人电脑上选择，目的是为了避免重复的登录操作。登录成功，一般会把`max-age`的值设置为比较长，就算是关闭了浏览器。重新打开，也不需要再次执行登录操作。

### spring-session 配置cookie的max-age属性

使用`spring-session`时，可以通过yml配置，或者代码配置的形式来设置`max-age`的属性。但是问题在于所有的session创建，都是使用同样的属性。在【记住我】这个功能上会出现一些问题

固定设置：**`max-age=-1`**，那么就算是勾选了【记住我】，也会因为浏览器关闭删除`cookie`，而丢失会话。下次打开浏览器还是需要重新执行登录

固定设置： **`max-age=604800`**(7天)，那么用户在未勾选【记住我】的情况下，关闭浏览器。`cookie`并不会被立即删除，任何人再次打开这个系统。都不需要登录就可以直接操作系统。

**总的来说就是，固定的`max-age`属性，会导致【记住我】功能失效。**

## 使用spring-session时的解决方案

`spring-session` 通过接口 **`CookieSerializer`**，来完成对客户端`cookie`的读写操作。并且提供了一个默认的实现类: **`DefaultCookieSerializer`**。我们想要动态的修改`cookie`的`max-age`属性，核心方法在于。

```java
@Override
public void writeCookieValue(CookieValue cookieValue) {
	...
	StringBuilder sb = new StringBuilder();
	sb.append(this.cookieName).append('=');
	...
	int maxAge = getMaxAge(cookieValue);  // 读取maxAge属性
	if (maxAge > -1) {
		sb.append("; Max-Age=").append(cookieValue.getCookieMaxAge());
		ZonedDateTime expires = (maxAge != 0) ? ZonedDateTime.now(this.clock).plusSeconds(maxAge)
				: Instant.EPOCH.atZone(ZoneOffset.UTC);
		sb.append("; Expires=").append(expires.format(DateTimeFormatter.RFC_1123_DATE_TIME));
	}
	...
}
private int getMaxAge(CookieValue cookieValue) {
	int maxAge = cookieValue.getCookieMaxAge();
	if (maxAge < 0) {
		if (this.rememberMeRequestAttribute != null
				&& cookieValue.getRequest().getAttribute(this.rememberMeRequestAttribute) != null) {
			cookieValue.setCookieMaxAge(Integer.MAX_VALUE);
		}
		else if (this.cookieMaxAge != null) {
			cookieValue.setCookieMaxAge(this.cookieMaxAge);  // 如果 DefaultCookieSerializer 设置了maxAge属性，则该属性优先
		}
	}
	return cookieValue.getCookieMaxAge(); // cookieValue 默认的maxAge属性 = -1
}
```

> 可以看出，`spring-session`并没使用`servlet`提供的`cookie api`来响应`cookie`。而是自己构造`Cookie`头。而且还提供了`Servlet`还未实现的，`Cookie`的新属性：**`sameSite`**，可以用来防止**`csrf`**攻击。

### 覆写 DefaultCookieSerializer

```java
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.session.web.http.DefaultCookieSerializer;

// @Component
public class DynamicCookieMaxAgeCookieSerializer extends DefaultCookieSerializer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicCookieMaxAgeCookieSerializer.class);
	
	public static final String COOKIE_MAX_AGE = "cookie.max-age";
	
	@Value("${server.servlet.session.cookie.max-age}")
	private Integer cookieMaxAge;
	
	@Override
	public void writeCookieValue(CookieValue cookieValue) {
		HttpServletRequest request = cookieValue.getRequest();
		// 从request域读取到cookie的maxAge属性
		Object attribute = request.getAttribute(COOKIE_MAX_AGE);
		if (attribute != null) {
			cookieValue.setCookieMaxAge((int) attribute);
		} else {
			// 如果未设置，就使用默认cookie的生命周期
			cookieValue.setCookieMaxAge(this.cookieMaxAge);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("动态设置cooke.max-age={}", cookieValue.getCookieMaxAge());
		}
		super.writeCookieValue(cookieValue);
	}
}
```

> 原理就是，把`cookie`的`maxAge`属性存储到`request`域。在响应客户端之前，动态的设置。

### 添加到IOC

```java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;

import com.video.manager.spring.session.DynamicCookieMaxAgeCookieSerializer;

@Configuration
public class SpringSessionConfiguration {
	
	@Value("${server.servlet.session.cookie.name}")
	private String cookieName;
	
	@Value("${server.servlet.session.cookie.secure}")
	private Boolean cookieSecure;
	
//	@Value("${server.servlet.session.cookie.max-age}")
//	private Integer cookieMaxAge;
	
	@Value("${server.servlet.session.cookie.http-only}")
	private Boolean cookieHttpOnly;

	@Value("${server.servlet.session.cookie.same-site}")
	private String cookieSameSite;
	
	@Bean
	public CookieSerializer cookieSerializer() {
		DynamicCookieMaxAgeCookieSerializer serializer = new DynamicCookieMaxAgeCookieSerializer();
		serializer.setCookieName(this.cookieName);
		// serializer.setCookieMaxAge(this.cookieMaxAge);
		serializer.setSameSite(this.cookieSameSite);
		serializer.setUseHttpOnlyCookie(this.cookieHttpOnly);
		serializer.setUseSecureCookie(this.cookieSecure);
		return serializer;
	}
}
```

> 使用 `@Value`，读取`yml`配置中的`Cookie`属性。

### 测试接口

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.video.manager.spring.session.DynamicCookieMaxAgeCookieSerializer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/test")
public class TestController {

	static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);
	
	@GetMapping("/session")
	public ModelAndView session(HttpServletRequest request, 
			@RequestParam("remember")Boolean remember) {
		
		HttpSession httpSession = request.getSession();
		LOGGER.debug("httpSession={}", httpSession);
		
		if (!remember) {  // 不记住我
			// 设置cookie的生命周期为 -1
			request.setAttribute(DynamicCookieMaxAgeCookieSerializer.COOKIE_MAX_AGE, -1);
			// 设置session仅缓存30分钟
			httpSession.setMaxInactiveInterval(60 * 30);
		}
		
		ModelAndView modelAndView = new ModelAndView("test/test");
		return modelAndView;
	}
}
```

### 【记住我】√

<http://localhost/test/session?remember=true>

响应Cookie，存储时间是 7 天

![image](https://springboot.io/uploads/default/original/1X/982f706786473809574d09c665b35d399d2173b1.png)

`redis`的`session`存储，缓存时间是7天

![](https://springboot.io/uploads/default/original/1X/1eebb037fd086c7bb3ab92422bb509d4fb62ee88.png)

### 【记住我】×

<http://localhost/test/session?remember=false>

响应Cookie，存储时间是：-1，临时会话设置成功，浏览器关闭Cookie删除

![](https://springboot.io/uploads/default/original/1X/827cc05d7073e530dce781fdf058c76f16bf15e8.png)



`redis`的`session`存储，缓存时间是`30`分钟，超过`30`分钟不活动，自动删除

![](https://springboot.io/uploads/default/original/1X/681773fb40a0a9bbc5732cacbaebd5c65a2bfcd8.png)



## End

`spring-session` 动态的设置`Cookie`的`max-age`属性，我目前就想到了这种解决方式。你如果有更优雅的方案。记得告诉我。![:see_no_evil:](https://springboot.io/images/emoji/apple/see_no_evil.png?v=9)



### 配置



```yaml
server:
  servlet:
    session:
      cookie:
        name: PHPSESSIONID
        secure: ${server.ssl.enabled:false}
        # 最大7天
        max-age: 604800
        http-only: true
        same-site: Strict

spring:
  session:
    # ${server.servlet.session.cookie.max-age}
    timeout: 604800
    store-type: REDIS
    redis:
      namespace: "spring:session"
      flush-mode: IMMEDIATE
      cleanup-cron: "0 * * * * *"
```

