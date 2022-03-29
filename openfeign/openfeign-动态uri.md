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

```java
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.URI;

@FeignClient(name = "API-SERVICE", url = "url-placeholder")
public interface ApiClient {

    @PostMapping(value = "")
    String getData(URI uri);

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

