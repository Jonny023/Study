## SpringBoot配置优先级

* [官网文档](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
* [参考文档](https://www.yuque.com/renyong-jmovm/dadudu/tpfm4q#uPRDu)

> 一共14种优先级，官网文档优先级从小到大列出，加载时是从大到小查找，有配置则直接获取使用，否则向下查找

1. 通过启动类设置

```java
SpringApplication springApplication = new SpringApplication(Spring3demoApplication.class);
Map<String, Object> map = new HashMap<>();
map.put("server.port", 8888);
springApplication.setDefaultProperties(map);
springApplication.run(args);
```

2. 指定以properties配置文件

```java
server.properties配置server.port=8881
@PropertySource("classpath:server.properties")
```

3. application.yml和application.properties配置
> properties优先级高于yml文件

4. @Value注解

> 通过random.*生成随机值

```java
//100以内的随机值
@Value("${random.int}")
private int value;

@Resource
private Environment environment;

@GetMapping("/")
public String index() {
    return "hello springboot3." + value + " " + environment.getProperty("random.int");
}
```

5. OS environment variables.

> 操作系统环境变量， 如idea的配置里面environment variables配置，也可以在环境变量里面配置server.port 8086

```shell
# 配置项目端口为8086
server.port=8086
```

6. vm参数配置

```shell
-Dserver.port=20001

# 多个参数
-Dserver.port=8087 -Da=100 -Db=200
```

10. vm配置SPRING_APPLICATION_JSON

> 配置的参数为json字符

```shell
-DSPRING_APPLICATION_JSON="{\"server.port\": 8888}"
-Dspring.application.json="{\"server.port\": 8888}"
```

11. 命令行参数（program arguments）
> 在idea里面配置program arguments

```shell
--server.port=8087 --a=100 --b=200

java -jar xxx.jar --server.port=8087 --a=100 --b=200
```

## spring.config.nane

> 配置-Dspring.config.name=bootstrap会从bootstrap.properties查找配置，这就是springcloud默认查找bootstrap.yml的原理
> 此配置会修改springboot默认查找，默认为application配置，修改后就会查找bootstrap

## 配置文件优先级顺序
```shell
spring.config.location #指定后默认的配置失效
spring.config.import #在默认基础上新增
spirng.config.additational #在默认基础上新增

#配置文件加载优先级降序
#指定绝对路径-Dspring.config.location=optional:file:D:\xxx\
#指定相对路径【项目路径下】-Dspring.config.import=optional:file:./xxx/
classpath:/xxx/ #通过-Dspring.config.import=optional:file:./xxx/会覆盖默认的配置
file:./config/*/
file:./config
file:./
classpath:/config/
classpath:/
```