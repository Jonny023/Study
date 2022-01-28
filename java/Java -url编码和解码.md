# url编码和解码

```java
String UTF8 = "UTF-8";

String encoding = java.net.URLEncoder.encode("编码", UTF8);
System.out.println("编码后: " + encoding);
// output %E7%BC%96%E7%A0%81

String str = java.net.URLDecoder.decode("%E7%BC%96%E7%A0%81", UTF8);
System.out.println("解码后： " + str);
// output 编码
```

