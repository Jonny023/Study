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

* MutableHttpServletRequest

```java
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 *  多次读取request
 */
public class MutableHttpServletRequest extends HttpServletRequestWrapper {

    private final String body;
    private byte[] bytes;
    private final Map<String, String> customHeaders;

    public MutableHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        this.customHeaders = new HashMap<String, String>();
        this.bytes = StreamUtils.copyToByteArray(request.getInputStream());
        this.body = new String(this.bytes, StandardCharsets.UTF_8);
    }

    public void putHeader(String name, String value) {
        this.customHeaders.put(name, value);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (bytes == null) {
            bytes = new byte[0];
        }
        final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        return new ServletInputStream() {
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

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }

    @Override
    public String getHeader(String name) {
        String headerValue = customHeaders.get(name);

        if (headerValue != null) {
            return headerValue;
        }
        return ((HttpServletRequest) getRequest()).getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> set = new HashSet<String>(customHeaders.keySet());

        @SuppressWarnings("unchecked")
        Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (e.hasMoreElements()) {
            String n = e.nextElement();
            set.add(n);
        }
        return Collections.enumeration(set);
    }
}
```

* ReadRequestBodyFilter

```java
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Order(Integer.MIN_VALUE)
@WebFilter(filterName = "readRequestBody-filter", urlPatterns = "/*")
public class ReadRequestBodyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfiguration) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request = new MutableHttpServletRequest((HttpServletRequest) request);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
```

* 启动类加配置

```
@ServletComponentScan
public class Application {
}
```


