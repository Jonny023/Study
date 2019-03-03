# 核心`Jackson`

## 应用场景

* 前台通过ajax传递的`application/json`数据
* `user`对象中有三个属性，分别是:`username`,`nickname`,`age`，封装的时候这三个属性两侧都存在空格，如下

```json
{
	"username": "   zhangsan   ",
	"nickname": "  龙飞凤舞  ",
	"age": "20    "
}
```

* 控制器方法 

```java
@ResponseBody
@RequestMapping("/user/get")
public User get(@Valid @RequestBody User user) {
    return user;
}
```

* 重写`SimpleModule`

> 若项目没`Jackson`，则需引入依赖

* 重写类

```java
package com.example.demo.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author Lee
 * @Description
 * @Date 2019年03月03日 16:33
 */
@Component
public class StringTrimModule extends SimpleModule {

    public StringTrimModule() {
        addDeserializer(String.class, new StdScalarDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException,
                    JsonProcessingException {
                return jsonParser.getValueAsString().trim();
            }
        });
    }
}
```

[参考地址](https://stackoverflow.com/questions/6852213/can-jackson-be-configured-to-trim-leading-trailing-whitespace-from-all-string-pr/24077444)
