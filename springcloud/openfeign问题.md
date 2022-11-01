## openfeign相关问题

### 1.openfeign启动异常

> org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'postController': Injection of resource dependencies failed; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'com.example.springcloudfeign.feign.UserFeignClient': Unexpected exception during bean creation; nested exception is java.lang.IllegalStateException: No Feign Client for loadBalancing defined. Did you forget to include spring-cloud-starter-loadbalancer?

* 需要添加负载均衡依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
<!--配合openfeign使用-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-loadbalancer</artifactId>
</dependency>
```



### 2.openfeign调用时报错

> java.lang.AbstractMethodError: org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient.choose(Ljava/lang/String;Lorg/springframework/cloud/client/loadbalancer/Request;)Lorg/springframework/cloud/client/ServiceInstance;
> 	at org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient.execute(FeignBlockingLoadBalancerClient.java:88) ~[spring-cloud-openfeign-core-3.0.7.jar:3.0.7]

* 移除nacos服务发现依赖里面的ribbon

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    <exclusions>
        <exclusion>
            <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
            <groupId>org.springframework.cloud</groupId>
        </exclusion>
    </exclusions>
</dependency>
```



### 3.openfeign无法捕获异常

> org.springframework.web.client.UnknownContentTypeException: Could not extract response: no suitable HttpMessageConverter found for response type [class java.lang.Object] and content type [text/plain;charset=UTF-8]

* openfeign用了Object类型接收响应数据，导致无法解析，需要指定对应的类型，用确定的对象接收



### 4.openfeign异常处理

#### 方式1

> 通过openfeign异常处理类处理

* 新增配置类：FeignErrorDecoder

> 异常处理类只能处理Http状态码为非200的数据

```java
package com.example.springcloudfeign.feign;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.springcloudfeign.ex.BizException;
import com.fasterxml.jackson.core.JsonEncoding;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

/**
 *  此类只在HttpStatus不等于200时才会执行
 */
@Slf4j
@Configuration
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        //调试时断点不要打在response.body().asReader(Util.UTF_8)上，会导致报错：response.body() stream is closed
        try (InputStream inputStream = response.body().asInputStream()) {
            String message = IOUtils.toString(inputStream, JsonEncoding.UTF8.getJavaName());
            try {
                JSONObject jsonObject = JSONObject.parseObject(message);
                String msg = jsonObject.getString("message");
                return new BizException(jsonObject.getInteger("code"), msg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            log.error("openfeign callback error: ", e);
        }
        String errorMsg = "openfeign call failed.";
        throw new BizException(500, errorMsg);
    }
}
```

* openfeign接口类

```java
package com.example.springcloudfeign.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(name = "demo", fallback = FeignErrorDecoder.class)
public interface UserFeignClient {

    @GetMapping("/api/test/get")
    String get(String id);

    @GetMapping("/api/test/get1")
    String get1(String id);
}
```



#### 方式2

>通过mvc全局异常处理拦截

```java
package com.example.springcloudfeign.handler;

import com.alibaba.fastjson.JSON;
import com.example.springcloudfeign.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.charset.Charset;

@Slf4j
@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(feign.FeignException.class)
    public R customException(feign.FeignException e) {
        log.warn("feign call error", e);
        String message = e.getMessage();
        //String json = new String(((FeignException.InternalServerError) e).responseBody().get().array(), Charset.defaultCharset());
        //return R.error(message);
        String json = new String(e.responseBody().get().array(), Charset.defaultCharset());
        return JSON.parseObject(json, R.class);
    }
}
```



#### 方式3

> 通过业务代码自己try catch



### 5.openfeign调用超时

* 异常处理

```java
@ExceptionHandler(feign.RetryableException.class)
public R retryableException(feign.RetryableException e) {
    log.warn("feign call timeout", e);
    String message = e.getMessage();
    return R.error("feign调用超时");
}
```

* 超时配置

```yaml
feign:
  client:
    config:
      default:
        read-timeout: 10000 # 请求处理超时时间，默认为60，单位毫秒
        connect-timeout: 10000 # 连接建立超时时间，单位毫秒
```



### 5.携带请求头

```java
//单个请求头
@GetMapping("/api/test/login")
void login(@RequestHeader("token") String token);

//多个请求头
@GetMapping("/api/test/login")
void login(@RequestHeader MultiValueMap<String, String> headers);


//从配置文件读取，如bootstrap.yml或application.yml
@GetMapping(value = "/api/test/login", headers = {"Content-Type=application/json;charset=UTF-8", "token=${user.token}"})
    void config();


//======================================================================================
//可能需要配置：spring.main.allow-bean-definition-overriding: true
//这种方式需要配合配置类使用，用了这种方式就不能和mvc的RequestMapping/GetMapping/PostMapping混用
//他们是互斥的，否则抛出异常：GetMapping that is not used by contract Default
package com.example.springcloudfeign.feign;

import feign.HeaderMap;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient(name = "demo", configuration = FeignConfig.class)
public interface LoginFeignClient {

    @RequestLine("GET /api/test/login")
    @Headers({"Content-Type: application/json;charset=UTF-8", "token: {token}"})
    void request(@Param("token") String token);

    @RequestLine("GET /api/test/login")
    void requestMap(@HeaderMap org.springframework.http.HttpHeaders headers);
}
//===============================================================================

package com.example.springcloudfeign.feign;

import feign.Contract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public Contract feignContract() {
        return new Contract.Default();
    }
}
//==============================================================================




//======================================自动添加请求头======================================
package com.example.springcloudfeign.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header("token", "auto-sign");
    }
}

//指定用自定义的FeignRequestInterceptor配置
package com.example.springcloudfeign.feign;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient(name = "demo", configuration = FeignRequestInterceptor.class)
public interface AuthFeignClient {

    //配合前面的配置类可以实现携带多个请求头，固定的请求头+自定义请求头实现多个请求头传递
    @RequestLine("GET /api/test/login")
    @Headers({"Content-Type: application/json;charset=UTF-8", "token1: {token1}"})
    void request(@Param("token1") String token1);
}
```

