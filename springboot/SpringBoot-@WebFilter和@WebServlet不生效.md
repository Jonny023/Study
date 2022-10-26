# SpringBoot-@WebFilter和@WebServlet不生效

> 原因：包没被扫描到

### 解决方法，在主类上加上注解`@ServletComponentScen`
```java
package com.jonny.demo;

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

# HttpServletRequest多次读取

> 默认获取过一次request中的body对象之后，接口对象获取不到参数了，需要重写HttpServletRequest让其支持多次读，如下方式读取之后，接口对象获取不到数据了

```java
	/**
     * 获取请求body
     *
     * @param request
     * @param encoding
     * @return
     */
    public static String getRequestBody(HttpServletRequest request, String encoding) {
        // 请求内容RequestBody
        String reqBody = null;
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte buffer[] = new byte[contentLength];
        try {
            for (int i = 0; i < contentLength; ) {
                int readlen = request.getInputStream().read(buffer, i, contentLength - i);
                if (readlen == -1) {
                    break;
                }
                i += readlen;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reqBody = new String(buffer, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return reqBody;
    }
```


### 解决方法

* BodyReaderWrapper

```java
package com.example.springbootdemo.servlet;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 重写request支持多次读：https://blog.csdn.net/hai330/article/details/123409987
 * https://juejin.cn/post/6984981793145356301
 */
public class BodyReaderWrapper extends HttpServletRequestWrapper {

    /**
     * 用于将流保存下来
     */
    private byte[] requestBody;

    public BodyReaderWrapper(HttpServletRequest request) throws IOException {
        super(request);
        requestBody = StreamUtils.copyToByteArray(request.getInputStream());
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        final ByteArrayInputStream bais = new ByteArrayInputStream(requestBody);

        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
}
```

* BodyReaderFilter

```java
package com.example.springbootdemo.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 缓存body数据
 */
@Slf4j
@Order(Integer.MIN_VALUE)
@WebFilter(filterName = "bodyReaderFilter", urlPatterns = "/*")
public class BodyReaderFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("==================执行过滤器================");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String contentType = httpRequest.getContentType();


	//这里如果不执行request.getParameterMap()，后续如果是url传参获取不到数据
        if (contentType != null && contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
            //如果是application/x-www-form-urlencoded, 参数值在request body中以 a=1&b=2&c=3...形式存在，
            //若直接构造BodyReaderHttpServletRequestWrapper，在将流读取并存到copy字节数组里之后,
            //httpRequest.getParameterMap()将返回空值！
            //若运行一下 httpRequest.getParameterMap(), body中的流将为空! 所以两者是互斥的！
            httpRequest.getParameterMap();
        }
        ServletRequest requestWrapper = null;
        if (request instanceof HttpServletRequest) {
            requestWrapper = new BodyReaderWrapper((HttpServletRequest) request);
        }
        if (requestWrapper == null) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, response);
        }
    }

    @Override
    public void destroy() {

    }
}
```

* 拦截器

```java
package com.example.springbootdemo.interceptor;

import com.example.springbootdemo.servlet.BodyReaderWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("======================前置处理=======================");
        BodyReaderWrapper requestWrapper = new BodyReaderWrapper(request);
        String body = StreamUtils.copyToString(requestWrapper.getInputStream(), Charset.defaultCharset());
        log.info("请求体1:{}", body);

        String body2 = StreamUtils.copyToString(requestWrapper.getInputStream(), Charset.defaultCharset());
        log.info("请求体2:{}", body2);

        //校验签名、token等
        String token = request.getHeader("token");
        if (StringUtils.hasText(token)) {
            return true;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("======================后置处理=======================");
        //HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
```

* web配置

```java
package com.example.springbootdemo.config;

import com.example.springbootdemo.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;

@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {

    @Resource
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
    }
}
```

* 启动类加配置

```
@SpringBootApplication
@ServletComponentScan
public class Application {
}
```


