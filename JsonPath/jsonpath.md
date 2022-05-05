# JsonPath

## jayway json-path

```xml
<dependency>
    <groupId>com.jayway.jsonpath</groupId>
    <artifactId>json-path</artifactId>
    <version>2.7.0</version>
</dependency>
```

### 示例

```java
String jsonStr = "{\"name\": \"zhangsan\", \"data\": \"{}\", \"arr\": [{\"id\":1},{\"id\":2}]}";
//String jsonStr = "{\"name\": \"zhangsan\", \"data\": \"{}\", \"arr\": [{\"date\":\"2022-01-20\"},{\"date\":\"2022-01-20\"}]}";
LinkedHashMap<String, Object> jsonMap = JSONUtil.toBean(jsonStr, LinkedHashMap.class);
for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
    System.out.println(entry.getKey() + ":" + entry.getValue());
}

//Object read = JsonPath.read(jsonStr, "$.arr[*].id");
Object read = JsonPath.read(jsonStr, "$..*");
System.out.println(read);
```



## fastjson JSONPath

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.80</version>
</dependency>
```

### 示例

```java
String jsonStr = "{\"name\": \"zhangsan\", \"data\": \"{}\", \"arr\": [{\"id\":1},{\"id\":2}]}";
Object name = JSONPath.read(jsonStr, "$.name");
System.out.println(name);

List<Object> list = (List<Object>) JSONPath.read(jsonStr, "$.arr[*].id");
System.out.println(list);
```

