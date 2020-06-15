### Profile是Spring对不同环境提供不同配置功能的支持，可以通过不同需求激活指定环境配置

##### 1、多Profile文件定义形式

* `application-{profile}.properties`或者`application-{profile}.yml`
  + `application-dev.properties` 或`application-dev.yml`
  + `application-test.properties` 或`application-test.yml`
  + `application-prod.properties` 或`application-prod.yml`

##### 2、多profile文档块形式

```yaml
---
server:
  port: 8080
spring:
  profiles: prod

---
server:
  port: 8081
spring:
  profiles: test
  
---
server:
  port: 8082
spring:
  profiles: dev
```

### 激活方式

##### 1、在yml或者properties中通过配置激活

```yaml
spring:
  profiles:
    active: dev # 激活开发环境
```

##### 2、命令行激活

```bash
--spring.profiles.active=dev
```

* 此命令式在`IDEA`中`Program arguments`输入框中设置

* 部署到本地激活方式

  ```bash
  java -jar demo-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
  ```

##### 3、JVM虚拟机参数激活

* 在`IDEA`中`VM Options`中输入框中设置

  ```bash
  -Dspring.profiles.active=dev
  ```

* 部署到本地激活方式

  ```bash
  java -jar -Dspring.profiles.active=test demo-0.0.1-SNAPSHOT.jar
  ```

* 指定启动端口号

```bash
java -jar xxx.jar --server.port=80
```
