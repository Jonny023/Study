## Shiro基本用法

* 获取当前登录用户
```java
User user = (User) SecurityUtils.getSubject().getPrincipal();
```
