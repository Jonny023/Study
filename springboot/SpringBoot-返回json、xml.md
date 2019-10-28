## 接口数据返回json、xml

* 依赖

```xml
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
</dependency>
```

* 代码
    * `@ResponseBody`返回json数据
    * 通过`@RequestMapping`注解参数`produces`来控制返回数据格式

```java
package com.example.demo.controller;

import com.example.demo.domain.User;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jonny
 * @description
 * @date 2019/10/28 0028
 */
@Controller
@RequestMapping("/user")
public class PostController {

    /**
     *  返回xml数据
     * @return
     */
    @RequestMapping(value = "/xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public List<User> xml() {
        List<User> users = new ArrayList<>();
        User u1 = new User(1L, "小明", "1234");
        User u2 = new User(2L, "小花", "123456");
        users.add(u1);
        users.add(u2);
        return users;
    }

    /**
     *  返回json数据
     * @return
     */
    @RequestMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<User> json() {
        List<User> users = new ArrayList<>();
        User u1 = new User(1L, "小明", "1234");
        User u2 = new User(2L, "小花", "123456");
        users.add(u1);
        users.add(u2);
        return users;
    }

}
```

# 总结

> 默认方法上加上`@ResponseBody`注解就能返回`json`，但是`xml`不行，若要返回`xml`，还是需要`jackson-dataformat-xml`这个依赖
