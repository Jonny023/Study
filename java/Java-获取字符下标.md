## 获取匹配的多个字符下标

[参考](https://www.cjavapy.com/article/260/)


```java

//xxx a.b.c ===> xxx a xxx a.b xxx a.b.c
public static void main(String[] args) {
    String str = "xxx a.b.c";
    Matcher matcher = Pattern.compile("\\.").matcher(str);
    while (matcher.find()) {
        //5 7
        System.out.println(matcher.start());
        System.out.println(str.substring(0, matcher.start()));
    }

    //[5,7]
    List<Integer> indexList = getIndexList(str, '.');
    for (int i = 0; i < indexList.size(); i++) {
        System.out.println(str.substring(0, indexList.get(i)));
    }
}

public static List<Integer> getIndexList(String s, char c) {
    return IntStream.range(0, s.length())
            .filter(index -> s.charAt(index) == c)
            .boxed()
            .collect(Collectors.toList());
}
```

## 直接获取

```java
// a -> [a]
// test a.b -> [test a,test a.b]

public static List<String> getIndexList(String s, char c) {
    List<String> list = IntStream.range(0, s.length())
            .filter(index -> s.charAt(index) == c)
            .mapToObj(i -> s.substring(0, i))
            .collect(Collectors.toList());
    list.add(s);
    return list;
}
```
