# 去除数值首尾的0

```java
new StringBuffer(str.replaceAll("^(0+)", "")).reverse().toString().replaceAll("^(0+)", "");

System.out.println(reverseStr("00001000200"));
System.out.println(reverseStr("000012345"));
System.out.println(reverseStr("00001000200".replaceAll("^(0+)", "")));
System.out.println(reverseStr("1100100".replaceAll("^(0+)", "")));
System.out.println(reverseStr("021100100".replaceAll("^(0+)", "")));
```