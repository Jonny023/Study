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
