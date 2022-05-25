# 文件

## 输出字符到文件

```java
String json = "{\"name\":\"张三\"}";
FileUtil.writeBytes(json.getBytes(StandardCharsets.UTF_8), new File("d:/out/a.json"));
```
