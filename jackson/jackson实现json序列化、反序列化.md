# Jackson转换json

## jackson序列化工具类

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author: Jonny
 * @description: Jackson转换JSON工具类
 */
public class JsonUtils {

    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        mapper.registerModule(new JavaTimeModule());
    }

    public static String toJSONString(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public static <T> T toObject(String json, Class<T> clazz) throws JsonProcessingException {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        return mapper.readValue(json, clazz);
    }

    public static <T> List toArray(String json, Class<T> clazz) throws JsonProcessingException {
        try {
            if (!StringUtils.hasText(json)) {
                return null;
            }
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            log.error("decode(String, JsonTypeReference<T>)", e);
            throw e;
        }
    }

    public static Map<String, Object> toObject(String json) throws JsonProcessingException {
        return toObject(json, Map.class);
    }

    public static List<Map<String, Object>> toArray(String json) throws JsonProcessingException {
        return toArray(json, Map.class);
    }

    public static <T> T toClass(String json, Class<T> clazz) {
        try {
            if (Optional.ofNullable(json).orElse("").trim().isEmpty()) {
                return null;
            }
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("cast to class failed {}", e);
        }
        return null;
    }

    /**
     *  判断是否为有效json【严格模式】
     *  isContainerNode()去除“123”这种格式的无效json字符
     * @param data
     * @return
     */
    public static boolean isJson(String data) {
        try {
            return mapper.readTree(data).isContainerNode();
        } catch (JsonProcessingException e) {
            log.warn("{} is not json", data);
            return false;
        }
    }
}
```

## 配置

```java
ObjectMapper om = new ObjectMapper();

// 属性为Null的不进行序列化，只对pojo起作用，对map和list不起作用
om.setSerializationInclusion(JsonInclude.Include.NON_NULL);

// json进行换行缩进等操作
om.enable(SerializationFeature.INDENT_OUTPUT);

// json不进行换行缩进等操作  默认就是不进行操作，写了这行和没写的效果一样
om.disable(SerializationFeature.INDENT_OUTPUT);

// json是否允许属性名没有引号 ，默认是false
om.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

//json是否允许属性名为单引号 ，默认是false
om.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

// 遇到未知属性是否抛出异常 ，默认是抛出异常的
om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

// 当实体类没有setter方法时，序列化不报错，返回一个空对象
om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

// 所有的字母小写，下划线作为名字之间分隔符，例如 snake_case
om.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

// 所有名字（包括第一个字符）都以大写字母开头，后跟小写字母，没有分隔符，例如 UpperCamelCase
om.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);

// 第一个单词以小写字母开头，后续每个单词都是大写字母开头，没有分隔符，例如 lowerCamelCase
om.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);

// 所有的字母小写，没有分隔符，例如 lowercase
om.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE);

// “Lisp” 风格，采用小写字母、连字符作为分隔符，例如 “lower-case” 或 “first-name”
om.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
```

# 注意！！！！！！！！！！！！

* 使用jsckson序列化/反序列化时，如果`接收参数`和`返回参数`的`key`不一样，需要手动在`getter/setter`方法上指定

  ```java
  class Test {
      
      private String elementName;
  
      @JsonProperty(value = "elementName")
      public String getElementName() {
          return elementName;
      }
  
      @JsonProperty(value = "elename")
      public void setElementName(String elementName) {
          this.elementName = elementName;
      }
  }
  ```

  > 接收参数：{"elename":"xxx"}   ==>  JsonUtils.toObject(json, Test.class);
  >
  > 转换字符：{"elementName":"xxx"} ==> JsonUtils.toJSONString(test);

