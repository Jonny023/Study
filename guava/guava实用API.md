## guava实用API

### 字符串转list

> 去除空和空格

```java
List<String> result = Splitter.on(",").trimResults().omitEmptyStrings().splitToList("1,2,       3,4,,6");
```

### url参数解析

> 将url参数解析为map

```java
Map<String, String> params = Splitter.on("&").trimResults().withKeyValueSeparator("=").split("name=priceType10&id=123");
```

### list切片分批

```java
com.google.common.collect.Lists.partition(list, 2000);
```

### list转字符

将集合[1,2,3,4]转为"1,2,3,4"

```java
Joiner.on(StringPool.COMMA).join(list)
```
