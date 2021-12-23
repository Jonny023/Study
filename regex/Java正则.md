# Java正则

* 替换所有斜杠“/”

```java
String d = "2019/3/21///-0/ 12:45:29";

System.out.println(d.replace("/","-"));
System.out.println(d.replaceAll("/","-"));
```

* 替换所有反斜杠

```java
String d = "2019\\3\\21\\-0/ 12:45:29";

System.out.println(d.replace("\\","-"));
System.out.println(d.replaceAll("\\\\","-"));
```

* replace方法坑，他的功能和replaceAll一样，只不过replaceAll支持正则

> {}需要转义，replaceFirst替换匹配的第一个

```java
"mod({0}, {0})".getTemplate().replaceFirst("\\{0\\}", "name"));
```

