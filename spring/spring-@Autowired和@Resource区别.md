# @Autowired和@Resource区别

* `@Autowired` 自动装配
* `@Resource` 按名称装配

## 区别
* `@Autowired`会自动装配`bean`，若声明`@Autowired(required = false)`，则`按名称装配`，表示若找不到`bean`,注入的`bean`为`null`，默认值为`true`，为`true`
时若找不到`bean`，会抛出异常。与`@Qualifier`搭配使用，如：

```java
@Autowired
@Qualifier("userDao")
private UserDao userDao;
```

* `@Resource`按照名称装配，若找不到`bean`,直接抛出异常

```java
@Resource(name="userDao")
private UserDao userDao;
```
