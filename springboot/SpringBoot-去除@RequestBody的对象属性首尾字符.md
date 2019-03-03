# 核心`Jackson`

* 重写`SimpleModule`

> 若项目没`Jackson`，则需引入依赖

* 重写类

```
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
