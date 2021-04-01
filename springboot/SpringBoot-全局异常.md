# 全局异常捕获

* yaml 捕获spring mvc异常

```yaml
spring:
  mvc:
    throw-exception-if-no-handler-found: true
```

```java
@ResponseBody
@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(code = 500)
    public Result exception(HttpServletRequest request, Exception e) {
        
    }
}
```
