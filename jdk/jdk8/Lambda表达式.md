> 集合

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
