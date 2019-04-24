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

* 替换所有“\”为“/”

```java
url.replaceAll("/\\/", "");
```
