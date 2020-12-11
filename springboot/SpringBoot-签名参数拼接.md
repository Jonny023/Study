* Map参数拼接

```java
/**
 * 将参数进行拼接
 * 返回结果为: key1=value1&key2=value2&key3=value3
 *
 * @param map HashMap参数
 * @return String
 */
public static String mapToString(Map<String, Object> map) {
    StringBuffer builder = new StringBuffer();
    map.forEach((key, value) -> builder.append(key).append("=").append(value).append("&"));
    builder.deleteCharAt(builder.length() - 1);
    return builder.toString();
}
```
