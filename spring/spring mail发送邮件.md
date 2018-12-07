> 主要依赖(spring自行百度)

```xml
<dependency>
    <groupId>javax.mail</groupId>
    <artifactId>mail</artifactId>
    <version>1.4.7</version>
</dependency>
```

> mail.properties

```xml
host=smtp.exmail.qq.com
port=587
username=mail@first-blood.cn
password=123456
```

> spring-mail.xml邮件配置文件
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="location">
            <value>mail.properties</value>
        </property>
    </bean>

    <bean id="mailSender" class="org.springframework.mail.javamail.JavaMailSenderImpl">
        <property name="host" value="${host}" />
        <property name="port" value="${port}" />
        <property name="username" value="${username}" />
        <property name="password" value="${password}" />

        <property name="javaMailProperties">
            <props>
                <prop key="mail.smtp.auth">true</prop>
                <prop key="mail.smtp.starttls.enable">true</prop>
            </props>
        </property>
    </bean>

    <bean id="mailMail" class="com.mail.MailBean">
        <property name="mailSender" ref="mailSender" />
    </bean>

</beans>
```

> MailBean类
```java
package com.mail;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * @Author Lee
 * @Date 2018/6/29 18:50
 */
public class MailBean {

    private MailSender mailSender;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String from, String to, String subject, String msg) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(msg);
        mailSender.send(message);
    }
}
```

> 测试类
```java
package com.test;

import com.mail.MailBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author Lee
 * @Date 2018/6/29 18:57
 */
public class MailTest {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-mail.xml");
        MailBean mm = (MailBean) context.getBean("mailMail");
        System.out.println(mm);
        mm.sendMail("mail@first-blood.cn",
                "342418262@qq.com",
                "spring mail",
                "spring mail \n\n 这是一封测试邮件");
    }
}

```
