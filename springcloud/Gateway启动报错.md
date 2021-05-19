# 加入Gateway依赖启动报错

```bash
Parameter 0 of method modifyRequestBodyGatewayFilterFactory in org.springframework.cloud.gateway.config.GatewayAutoConfiguration required a bean of type 'org.springframework.http.codec.ServerCodecConfigurer' that could not be found.
```

***原因：***

> 依赖冲突: spring-cloud-starter-gateway与spring-boot-starter-web和spring-boot-starter-webflux依赖冲突

***解决：***

> 排除 spring-boot-starter-web和spring-boot-starter-webflux依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```