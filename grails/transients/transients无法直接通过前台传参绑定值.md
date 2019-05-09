## transients无法直接通过前台传参绑定值

> transients声明的字段无法持久化，而且无法通过`def save(Object obj)`直接获取到，需要配置约束

```java
password2(bindable: true, nullable:true, blank: false, validator: {password, obj -> }
```
