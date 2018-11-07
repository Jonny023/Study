___



### `@PropertySource`

* 加载指定的配置文件
* 只能加载`*.properties`文件，不能加载`yaml`文件

> 新建一个`user.properties`

```properties
user.nickname=张三
user.age=19
user.sex=男
user.maps.weight=70
user.maps.height=170
user.address.addr=重庆市渝中区
```

> `UserBean`

```java
@Component
@PropertySource(value = {"classpath:user.properties"})
@ConfigurationProperties(prefix = "user")
public class User {

    private String nickname;
    private Integer age;
    private char sex;

    private Map<String,Integer> maps;

    private Address address;
    
    ...
}
```



___



### `@ImportResource`

- 导入`Spring`的配置文件，让配置文件里面的内容生效

> SpringBoot中编写的Spring配置文件是不能自动识别的

**在主配置类上加入`@ImportResource`**

```java
@ImportResource(locations = {"classpath:beans.xml"})
```

___



### `SpringBoot`给容器添加组件的方式

##### 1、配置类  ==  Spring配置文件    通过`@Configuration`声明

##### 2、使用`@Bean`给容器添加组件，组件id默认为方法名

## 例子

``` java
package com.atgenee.demo.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Hello {

    @Bean
    public Hello helloService() {
        System.out.println("添加组件");
        return new Hello();
    }
}
```

## 测试

```java
package com.atgenee.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloServiceApplicationTests {
    
//	@Autowired
//  private Hello hello;
//
//  @Test
//  public void hello() {
//      hello.helloService();
//  }

    @Autowired
    private ApplicationContext ioc;

    @Test
    public void hello() {
        ioc.getBean("helloService");
    }
}

```

## 请注意

> 若配置类已经加了@Bean注解，此时配置类中的方法名不能跟类名一样，也就是上面的Hello类中不能定义hello()的方法，否则报错



___



# 通过自定义工厂来实现自定义yaml文件加载

#### 新建一个`cat.yml`文件

```yaml
cat:
  age: 3
  height: 20
  weight: 5
```



#### 工厂类

```java
package com.atgenee.demo.factory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.Nullable;

public class YamlPropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource resource) throws IOException {
        Properties propertiesFromYaml = loadYamlIntoProperties(resource);
        String sourceName = name != null ? name : resource.getResource().getFilename();
        return new PropertiesPropertySource(sourceName, propertiesFromYaml);
    }

    private Properties loadYamlIntoProperties(EncodedResource resource) throws FileNotFoundException {
        try {
            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
            factory.setResources(resource.getResource());
            factory.afterPropertiesSet();
            return factory.getObject();
        } catch (IllegalStateException e) {
            // for ignoreResourceNotFound
            Throwable cause = e.getCause();
            if (cause instanceof FileNotFoundException)
                throw (FileNotFoundException) e.getCause();
            throw e;
        }
    }
}
```

#### 新建配置类 `Cat`，配合`@PropertySource` 注解使用

```java
package com.atgenee.demo.config;

import com.atgenee.demo.factory.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:cat.yml")
@ConfigurationProperties(prefix = "cat")
public class Cat {

    private int age;

    private Double weight;

    private Double height;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "age=" + age +
                ", weight=" + weight +
                ", height=" + height +
                '}';
    }
}
```

#### Cat测试类

```java
package com.atgenee.demo;

import com.atgenee.demo.config.Cat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CatApplicationTests {

    @Autowired
    private Cat cat;

    @Test
    public void hei() {
        System.out.println(cat);
    }

}
```

#### 控制台输出

```java
Cat{age=3, weight=5.0, height=20.0}
```

