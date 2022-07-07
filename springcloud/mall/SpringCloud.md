## SpringCloud

### 项目无法读取bootstrap.yml或bootstrap.properties

> 项目无法读取nacos配置中心的配置，启动报错，@Value值无法加载
>
> 原因：高版本移除了自动读取bootstrap文件的依赖，需要手动引入依赖

```xml
<!--添加读取bootstrap配置文件的依赖-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>
```

* 配置中心的配置优先级最高，若配置中心有配置，优先使用配置中心的配置



### 配置

> namespace：项目隔离
>
> group：环境隔离

```properties
spring.application.name=gulimall-coupon
spring.cloud.nacos.config.server-addr=127.0.0.1:8848
spring.cloud.nacos.config.namespace=f78217d4-8a4f-4b0d-a0a6-18feaeb4e32f
#spring.cloud.nacos.config.group=dev
spring.cloud.nacos.config.extension-configs[0].data-id=datasource.yml
spring.cloud.nacos.config.extension-configs[0].group=dev
spring.cloud.nacos.config.extension-configs[0].refresh=true

spring.cloud.nacos.config.extension-configs[1].data-id=mybatis.yml
spring.cloud.nacos.config.extension-configs[1].group=dev
spring.cloud.nacos.config.extension-configs[1].refresh=true

spring.cloud.nacos.config.extension-configs[2].data-id=other.yml
spring.cloud.nacos.config.extension-configs[2].group=dev
spring.cloud.nacos.config.extension-configs[2].refresh=true

spring.cloud.nacos.config.extension-configs[3].data-id=gulimall-coupon.properties
spring.cloud.nacos.config.extension-configs[3].group=dev
spring.cloud.nacos.config.extension-configs[3].refresh=true

```



### Gateway

#### 跨域配置

```java
package com.atguigu.gulimall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GuliMallCorsConfiguration {

    /**
     *  跨域配置
     * @return
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsWebFilter(source);
    }
}

```

#### 路由

> 精确路由需要放到最前面

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: baidu_route
          uri: https://www.baidu.com
          predicates:
            - Query=url,baidu
        - id: qq_route
          uri: https://qq.com
          predicates:
            - Query=url,qq

        - id: product_route
            uri: lb://gulimall_product
            predicates:
              - Path=/api/product/**
            filters:
              - RewritePath=/api/product/(?<segment>.*),/product/$\{segment}
        - id: admin_route
          uri: lb://renren-fast
          predicates:
            - Path=/api/** # 前端项目/api
          filters:
            - RewritePath=/api/(?<segment>.*),/renren-fast/$\{segment}


```

