## 格式化字符串

> 格式化补位
* `%nd` 输出的整型宽度至少为`n`位，右对齐，`%5d`即宽度至少为`5`位，位数大于`5`则输出实际位数(n指代具体数值)
* `%0nd` 用得比较多，表示输出的整型宽度至少为`n`位，不足`n`位用`0`填充(n指代具体数值)
* `%n` 换行字符

[字符释义](https://www.cnblogs.com/Dhouse/p/7776780.html)

```java
String.format("%05d%n",1)
String.format("%05d",2)
String.format("%05d",20)
```
