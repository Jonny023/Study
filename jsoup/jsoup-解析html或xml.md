# Java Jsoup解析xml、html

## 1.依赖

```xml
<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.10.2</version>
</dependency>
```

## 2.用例

```java
String tag = "<sites>\n" +
    "    <site>\n" +
    "        <name>RUNOOB</name>\n" +
    "        <url>www.runoob.com</url>\n" +
    "    </site>\n" +
    "    <site>\n" +
    "        <name>Google</name>\n" +
    "        <url>www.google.com</url>\n" +
    "    </site>\n" +
    "    <site>\n" +
    "        <name>Facebook</name>\n" +
    "        <url>www.facebook.com</url>\n" +
    "    </site>\n" +
    "</sites>";

Document document = Jsoup.parse(tag, "UTF-8");
Elements details = document.getElementsByTag("site");
for (Element detail : details) {
    Elements childrens = detail.children();
    for (Element children : childrens) {
        System.out.printf("%s: %s\n", children.tagName(), children.text());
    }
    System.out.println("==========");
}
```

