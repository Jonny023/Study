> 集合（List）

```java
// 无条件判断
List list = Arrays.asList("语文","数学","英语");
list.forEach(obj-> System.out.println(obj));

// 条件判断
list.forEach(obj-> {
    if("数学".equals(obj)) {
        System.out.println("今天有数学课。");
    }
});
```

> 键值对（Map）

```java
Map<String, Object> map = new HashMap<String, Object>();
map.put("name","张三");
map.put("age",20);
map.put("sex",'男');
map.forEach((k, v) -> System.out.println("键：" + k + "，值：" + v));
System.out.println("");
map.forEach((k, v) -> {
    System.out.println("键：" + k + "，值：" + v);
});
```
