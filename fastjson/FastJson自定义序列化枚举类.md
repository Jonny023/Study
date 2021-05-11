# FastJson自定义序列化枚举类

* 期望结果

```json
{"status":"yes"}
```

* 未处理前得到结果

```
{"status":"YES"}
```



# 加工

---



* 自定义序列化反序列化类

```java
package cn.com.test;

import cn.com.test.JsonTest.REnum;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.IOException;
import java.lang.reflect.Type;

public class JsonConvertor implements ObjectSerializer, ObjectDeserializer {

  /**
   *  反序列化过程
   */
  @Override
  public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
    Object value = parser.parse();
    return value == null ? null : (T) REnum.convert(TypeUtils.castToString(value)).get();
  }

  @Override
  public int getFastMatchToken() {
    return 0;
  }

  /**
   *  序列化过程
   */
  @Override
  public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
    serializer.write(((REnum)object).getCode());
  }
}
```

* 使用时指定`@JSONField(serializeUsing = JsonConvertor.class, deserializeUsing = JsonConvertor.class)`

```java
package cn.com.test;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import java.util.Arrays;
import java.util.Optional;

public class JsonTest {

  public static void main(String[] args) {
    System.out.println(JSONObject.toJSONString(REnum.YES));
    Student stu = new Student();
    stu.setStatus(REnum.YES);
    System.out.println(JSONObject.toJSONString(stu));
  }

  public static class Student {

    public Student() {}

    public Student(REnum status) {
      this.status = status;
    }

    @JSONField(serializeUsing = JsonConvertor.class, deserializeUsing = JsonConvertor.class)
    private REnum status;

    public REnum getStatus() {
      return status;
    }

    public void setStatus(REnum status) {
      this.status = status;
    }
  }

  enum REnum {

    YES("yes", "正确"),
    NO("no", "错误");

    private String code;
    private String name;

    REnum(String code, String name) {
      this.code = code;
      this.name = name;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public static Optional<REnum> convert(String value) {
      return Arrays.stream(REnum.values()).filter(rEnum -> rEnum.getCode().equalsIgnoreCase(value)).findFirst();
    }

    @Override
    public String toString() {
      return code;
    }
  }

}
```

