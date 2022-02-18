## SpringMVC伪静态

### 接口

```java
@GetMapping("/{uri:\\w*}.html")
public String index(@PathVariable("uri") String uri) {
    LOG.info("{}", uri);
    return uri;
}
```

### 访问

> localhost:8080/123.html
>
> localhost:8080/abcZfGh.html
>
> localhost:8080/a_b_c.html

```shell
2022-02-18 13:40:18.895  INFO 22468 --- [nio-8080-exec-2] c.e.s.controller.MainController          : 123
2022-02-18 13:40:27.363  INFO 22468 --- [nio-8080-exec-3] c.e.s.controller.MainController          : abcZfGh
2022-02-18 13:40:28.210  INFO 22468 --- [nio-8080-exec-3] c.e.s.controller.MainController          : a_b_c
```

