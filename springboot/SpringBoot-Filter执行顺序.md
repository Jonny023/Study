# SpringBoot - 控制Filter执行顺序

* 请求后端执行顺序

```java
Client > Listener > ServletContainer > Filter > Servlet > Interceptor
```

## 1.如何使`@WebFilter`注解生效?

> 解决方法，在启动类上加上注解`@ServletComponentScen`

```java
package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
```

* filter实现类

> 拦截所有请求`/*`，如果用`/**`无效，还可以拦截其他如：`*.js `、`/login/*`等

```java
package com.fiter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"}, filterName = "encodingFilter")
public class EncodingFilter implements Filter {

    private final Logger LOG = LoggerFactory.getLogger(EncodingFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        LOG.info("执行encodingFilter过滤，uri：{}, url:{}", request.getRequestURI(), request.getRequestURL());
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
```

## 2.如何控制Filter的执行顺序？

> 网上铺天盖地的讲`@Order`和实现`Ordered`可以控制，事实真的是这样么？经过测试，这些方法配合`@WebFilter`并没什么卵用，有用的方法如下

### 方法1：注册为Bean

> 实现：`@Component`+`@Order`或实现`Ordered`
>
> 缺点：无法指定拦截指定请求，需要在方法中自定义实现
>
> 优点：相对来说，没有多余的配置，比较简单，不需要`@ServletComponentScan`注解

* filter1

```java
package com.fiter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Order(1)
@Component
public class EncodingFilter implements Filter {

    private final Logger LOG = LoggerFactory.getLogger(EncodingFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        LOG.info("执行encodingFilter过滤，uri：{}, url:{}", request.getRequestURI(), request.getRequestURL());
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
```

* filter2

```java
package com.fiter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Order(2)
@Component
public class XssFilter implements Filter {

    private final Logger LOG = LoggerFactory.getLogger(XssFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        LOG.info("执行xssFilter过滤，uri：{}, url:{}", request.getRequestURI(), request.getRequestURL());
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
```

### 方法2：通过配置类注入

>实现：在配置类中注册`org.springframework.boot.web.servlet.FilterRegistrationBean`对应的Bean
>
>优点：可过滤指定url请求
>
>缺点：需要对每个filter都进行配置注入

* filter1

```java
package com.fiter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class XssFilter implements Filter {

    private final Logger LOG = LoggerFactory.getLogger(XssFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        LOG.info("执行xssFilter过滤，uri：{}, url:{}", request.getRequestURI(), request.getRequestURL());
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
```

* filter2

```java
package com.fiter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class EncodingFilter implements Filter {

    private final Logger LOG = LoggerFactory.getLogger(EncodingFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        LOG.info("执行encodingFilter过滤，uri：{}, url:{}", request.getRequestURI(), request.getRequestURL());
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
```

* 配置类

```java
package com.fiter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<XssFilter> xssFilter() {
        FilterRegistrationBean<XssFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        XssFilter xssFilter = new XssFilter();
        filterRegistrationBean.setFilter(xssFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("name", "xss过滤器");//设置init参数
        filterRegistrationBean.setName("xssFilter");
        filterRegistrationBean.setOrder(1);//执行次序
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<EncodingFilter> encodingFilter() {
        FilterRegistrationBean<EncodingFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        EncodingFilter encodingFilter = new EncodingFilter();
        filterRegistrationBean.setFilter(encodingFilter);
        filterRegistrationBean.addUrlPatterns("/*", "*.html");
        filterRegistrationBean.addInitParameter("name", "编码过滤器");//设置init参数
        filterRegistrationBean.setName("encodingFilter");
        filterRegistrationBean.setOrder(2);//执行次序
        return filterRegistrationBean;
    }

}
```

# 若想做到精细的请求过滤，推荐用方法2

[参考地址](https://www.javadevjournal.com/spring-boot/spring-boot-add-filter/)

