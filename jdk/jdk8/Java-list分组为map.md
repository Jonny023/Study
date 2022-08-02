## java8把list转为map

> list按指定字段分组，多个value逗号拼装

```java
List<Test1> list = Arrays.asList(
        Test1.builder().id(1).name("技术").build(),
        Test1.builder().id(1).name("Daas").build(),
        Test1.builder().id(2).name("产品").build()
);
Map<Integer, String> collect = list.stream().collect(Collectors.toMap(Test1::getId, Test1::getName, (k1, k2) -> String.format("%s,%s", k1, k2)));
```
