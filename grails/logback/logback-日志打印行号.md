# 带行号输出

* 建议开发环境使用，带行号需要额外开销

```groovy
pattern = "%-4(%d{HH:mm:ss.SSS} [%thread]) %-5level %replace(%caller{1}){'\t|Caller.{1}0|\r\n', ''} - %msg%n"
```
