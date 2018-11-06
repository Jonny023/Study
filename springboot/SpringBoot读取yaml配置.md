## SpringBoot读取yaml配置的值

> 在`application.yml`中添加配置

```yaml
user:
  name: 张三
  age: 19
  sex: 男

  maps: {weight: 70,height: 170}

  address:
    addr: 重庆市渝中区

  lists:
    - 京东
    - 淘宝
    - 坑多多
```



> 新建Address类

```java
package com.atgenee.demo.config;

public class Address {

    private String addr;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addr='" + addr + '\'' +
                '}';
    }
}

```

> 新建`User`类

```java
package com.atgenee.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "user")
public class User {

    private String name;
    private Integer age;
    private char sex;

    private Map<String,Integer> maps;

    private Address address;

    private List<String> lists;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public Map<String, Integer> getMaps() {
        return maps;
    }

    public void setMaps(Map<String, Integer> maps) {
        this.maps = maps;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<String> getLists() {
        return lists;
    }

    public void setLists(List<String> lists) {
        this.lists = lists;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", maps=" + maps +
                ", address=" + address +
                ", lists=" + lists +
                '}';
    }
}
```

## 新建测试类

```java
package com.atgenee.demo;

import com.atgenee.demo.config.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserApplicationTests {

    //注入配置类
    @Autowired
    User user;

    @Test
    public void contextLoads() {
        System.out.println(user);
    }

}
```
## 测试结果

```java
User{name='Administrator', age=19, sex=男, maps={weight=70, height=170}, address=Address{addr='重庆市渝中区'}, lists=[京东, 淘宝, 坑多多]}
```

### 注意

* `yaml`格式要求严格，以键值形式配置，键后冒号后面的空格一定不要丢（如: `username: admin`）

* `@ConfigurationProperties`注解依赖

  ```xml
  <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
        <optional>true</optional>
  </dependency>
  ```
