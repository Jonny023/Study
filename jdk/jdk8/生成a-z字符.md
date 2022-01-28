## 生成a-z字符

```java
List<Character> list1 = IntStream.rangeClosed('a', 'z').mapToObj(s -> (char) s).collect(Collectors.toList());
        System.out.println(list1);

List<Character> list2 = Stream.iterate('a', i -> ++i).limit(26).collect(Collectors.toList());
System.out.println(list2);
```

## 字符串反转

> 通过栈的后进现出的特点
>
> StringBuffer或StringBuffer的reverse方法
```java
String str = "abcdefgijklmnopqrstuvwxyz";

Stack<Character> stack = new Stack<>();
char[] chars = str.toCharArray();
for (char s : chars) {
	stack.push(s);
}

StringBuilder sb = new StringBuilder();
while (stack.size() > 0) {
	sb.append(stack.pop());
}
System.out.println(sb);
```

