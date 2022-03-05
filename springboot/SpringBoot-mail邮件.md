# SpringBoot通过JavaMailSender发送邮件

* qq邮箱用25或者587端口
* 发送延迟有点高，测试下大概在20s+

## pom.xml

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

## yaml

```yaml
spring:
  mail:
    port: 25
    host: smtp.qq.com
    default-encoding: UTF-8
    username: 342418262@qq.com
    password: xxxxxxxxxx
    protocol: smtp
    test-connection: true
    properties:
      mail:
        smtp:
          auth: true
        starttls:
          enable: true
          required: true
        socketFactory:
          port: 25
          class: javax.net.ssl.SSLSocketFactory
          fallback: false

```



## 发送邮件

```java
@RestController
@RequestMapping("/mail")
public class MainController {

    @Autowired
    JavaMailSender javaMailSender;

    @GetMapping("/send")
    public String send() {
        //创建SimpleMailMessage对象
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件发送人
        message.setFrom("342418262@qq.com");
        //邮件接收人
        message.setTo("342418262@qq.com");
        //邮件主题
        message.setSubject("test");
        //邮件内容
        message.setText("hello world!");
        //发送邮件
        javaMailSender.send(message);
        return "success";
    }

}
```

