> 数值型字符排序(传统方式)

```java
public static void main(String[] args) {
    List list = Arrays.asList("2", "1", "3","200","100","-100","0");

    Comparator<String> comparator = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return Integer.valueOf(o1) - Integer.valueOf(o2);
        }
    };

    list.sort(comparator);
    System.out.println(list);
    // 输出[-100, 0, 1, 2, 3, 100, 200]
}
```

> Lambda表达式

```java
public static void main(String[] args) {
    List list = Arrays.asList("2", "1", "3","20","10000","0");

    // 升序
    // list.sort((o1, o2) -> Integer.valueOf(String.valueOf(o1)).compareTo(Integer.valueOf(String.valueOf(o2))));
    // list.sort((o1, o2) -> Integer.valueOf(String.valueOf(o1)) - Integer.valueOf(String.valueOf(o2)));

    // 不能对负数排序
    list.sort(Comparator.comparing(String::hashCode));

    // 降序
    // list.sort((o1, o2) -> Integer.valueOf(String.valueOf(o2)).compareTo(Integer.valueOf(String.valueOf(o1))));
    
    // 可以包含负整数
    List list2 = Arrays.asList("2", "1", "3","20","10000","500","-1","800");
    list2.sort(Comparator.<String,Integer>comparing(Integer::valueOf).reversed());
    System.out.println(list2);

    System.out.println(list);
}
```

> join前后加字符

```java
String[] strs = new String[]{ "a", "b", "c"};
Stream<String> strs1 = Stream.of(strs);
String collect = strs1.collect(Collectors.joining("\",\"", "\"", "\""));
System.out.println(collect);
```
