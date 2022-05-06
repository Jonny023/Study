# openfeign动态url

[参考](https://www.cnblogs.com/lshan/p/15541012.html)

## 依赖

> springboot 2.6.5需要3.0+

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
    <version>3.1.1</version>
</dependency>
```

## 启用openfeign

> @EnableFeignClients启用feign

```java
@SpringBootApplication
@EnableFeignClients
public class SpringBootOpenfeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootOpenfeignApplication.class, args);
    }

}
```

## 接口

> 通过URI参数传入http接口地址，调用时会自动将uri参数作为请求地址，url必须填写，只是占位，因为url必须要传
> post请求动态参数用示例中的3种都可以，如果是确定的对象也可以用  `(@RequestBody User user)`

```java
package com.example.springbootopenfeign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.util.Map;

@FeignClient(name = "API-SERVICE", url = "url-placeholder")
public interface ApiClient {

    @GetMapping(value = "")
    Object getData(URI uri, @RequestParam Map<String, Object> map);

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    Object postData(URI uri, String param);

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    Object postData(URI uri, Object param);

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    Object postData(URI uri, Map param);
    
    @PostMapping
    Object getPostData(URI uri, @RequestBody Map param);
    
    /**
     * 多参数、多请求头
     * https://localcoder.org/using-headers-with-dynamic-values-in-feign-client-spring-cloud-brixton-rc2
     * @param uri
     * @param headers
     * @param param
     * @return
     */
    @PostMapping
    Object getPostData(URI uri, @RequestHeader Map<String, Object> headers, @RequestBody Map param);

}
```

## 调用

```java
@RestController
public class PostController {

    @Resource
    private ApiClient apiClient;

    @GetMapping()
    public Object get() {
        String url = "http://api.k780.com/?app=weather.history&weaId=1&date=20150720&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";
        return apiClient.getData(URI.create(url));
    }
}
```

* 判断数据类型

```java
	@GetMapping("parse")
    public Object parse() {
        String url = "https://i.news.qq.com/trpc.qqnews_web.pc_base_srv.base_http_proxy/NinjaPageContentSync?pull_urls=news_top_2018";
        Map<String, Object> map = new HashMap<>();
        String jsonStr = apiClient.getData(URI.create(url), map);
        //https://www.cnblogs.com/Durant0420/p/14963310.html
        Object object = JsonPath.read(jsonStr, "$.data[*]");
        if (object instanceof JSONArray) {
            Set<? extends Map.Entry<?, ?>> entries = ((LinkedHashMap<?, ?>) ((JSONArray) object).get(0)).entrySet();
            for (Map.Entry<?, ?> entry : entries) {
                //entry.getValue() instanceof Integer
                //System.out.println(entry.getValue().getClass());
                System.out.println(ofType(entry.getValue()));

            }
        }
        return object;
    }

    private <T> ColumnDataType ofType(T input) {
        if (input instanceof Number) {
            return ColumnDataType.number;
        } else if (input instanceof Date) {
            return ColumnDataType.date;
        } else {
            return ColumnDataType.string;
        }
    }

    public enum ColumnDataType {
        number, string, date
    }
```

# 方式2

> 用@RequestLine注解

## RequestLine

> `RequestLine`不能和`GetMapping`、`PostMapping`一起使用，也就是用`RequestLine`的类中只能用`@RequestLine`注解，不然会报如下错误

```bash
Caused by: java.lang.IllegalStateException: Method ApiClient#getData(URI,Map) not annotated with HTTP method type (ex. GET, POST)
Warnings:
- Class ApiClient has annotations [FeignClient] that are not used by contract Default
- Method getData has an annotation GetMapping that is not used by contract Default
```

> 添加配置类，然后在interface中配置configuration（经测试这一步可以不要）

```java
package com.example.springbootopenfeign.config;

import feign.Contract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Bean
    public Contract feignContract() {
        return new Contract.Default();
    }
}
```

### 接口类

> 动态url

```java
package com.example.springbootopenfeign.service;

import feign.Headers;
import feign.QueryMap;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

import java.net.URI;
import java.util.Map;

@FeignClient(name = "API-SERVICE", url = "url-placeholder")
public interface ApiClient {

    @RequestLine("GET")
    @Headers({
        "Content-Type: application/json",
        "Accept: application/json"
    })
    Object getPostData(URI uri, @RequestBody Map<String, Object> queryMap);

}
```



