## SpringBoot静态资源乱码

> 通过浏览器访问localhost:8080/xxx.js乱码

```yaml
server:
  servlet:
    encoding:
      force-response: true
```

