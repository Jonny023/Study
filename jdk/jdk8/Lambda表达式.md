## forEach()方法

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

// 直接打印结果
list.forEach(System.out::println);
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

> 数组（Array）

```java
String[] words = new String[]{"lianggzone", "spring", "summer", "autumn", "winter"};
Arrays.sort(words, (x, y) -> x.compareToIgnoreCase(y));
```

## stream()方法
* filter() 根据条件过滤

> 基本用法
```java
List list = Arrays.asList("语文", "数学", "英语");
long count = list.stream().filter(o -> o.toString().indexOf("语") != -1).count();

// 输出2
System.out.println(count);

```

> 条件过滤

```java
List list = Arrays.asList("语文", "数学", "英语");
Stream stream = list.stream().filter(o -> o.toString().indexOf("语") != -1);

// 输出 语文 英语
stream.forEach(o-> System.out.println(o));
```
