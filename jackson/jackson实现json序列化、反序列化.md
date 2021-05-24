* jackson序列化工具类

> 依赖

```xml
<dependency>
    <groupId>com.fasterxml.jackson.datatype</groupId>
    <artifactId>jackson-datatype-jsr310</artifactId>
</dependency>
```

> 工具类

```java
package com.common.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author: Jonny
 * @description: Jackson转换JSON工具类
 * @date:created in 2021/5/21 17:10
 * @modificed by:
 */
public class JsonUtils {

    protected static Logger log = LoggerFactory.getLogger(JsonUtils.class);

    public static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
    }


    public static String toJSONString(Object object) {
        String result = "";
        try {
            result = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        T result = null;
        try {
            result = mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <T> List toArray(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonParseException e) {
            log.error("decode(String, JsonTypeReference<T>)", e);
            e.printStackTrace();
        } catch (JsonMappingException e) {
            log.error("decode(String, JsonTypeReference<T>)", e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("decode(String, JsonTypeReference<T>)", e);
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Object> toObject(String json) {
        return toObject(json, Map.class);
    }

    public static List<Map<String, Object>> toArray(String json) {
        return toArray(json, Map.class);
    }

    public static <T> T toClass(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("cast to class failed {}", e);
            e.printStackTrace();
        }
        return null;
    }

}
```

