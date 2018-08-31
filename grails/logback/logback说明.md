> 表达式
```
pattern = "%-4(%d{HH:mm:ss.SSS} | [%thread]) %-5level | %logger | \\(%class:%line\\) - %msg%n"
```
* %logger 打印完整包名路径com.custom.LogingFilter
* %logger{10} 缩写式包名路径c.c.LoginFilter
