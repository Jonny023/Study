#### Lombok问题

1. 集成swagger时实体类属性和swagger-ui返回属性对不上（解析错误）

  > 如：将`uName`解析成了`UName`

2. 解决方法，通过`@JsonProperty`指定映射名称

  ```java
  @JsonProperty("uName")
  @ApiModelProperty("用户名")
  private String uName;
  ```

 * `@Accessors(chain = true)`注解允许链式调用
 
 ```java
 new User().name("test").age(20);
 ```

## 2. builder的坑
### 2.1 builder问题1

> 没有继承其他类不要用@SuperBuilder来构建，要用@Builder构建，否则报错：`Cause: java.lang.IllegalArgumentException: argument type mismatch`，意思
> 是没有找到对应的构造函数，如果继承了父类，子类要用builder()方法构建对象，子类则需要添加`@SuperBuilder`注解


### 2.2 builder问题2

> 在适用@Builder注解通过build模式构建实例时，需要同时使用如下注解，不然可能导致报错，我们开发时遇到过mybati查询返回model只添加@Data和@Builder报错了，提示下标越界
> 问题分析：原因是用了@Data和@Builder相当于只有一个固定顺序的有参构造器，而某些地方使用的有参构造的参数顺序和默认的有参构造不一致

* 解决方法，使用到@Builder注解的类同时添加有参和无参构造器

```java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
```

### 2.3 builder问题3

> mybatis查询后一直报错

* [参考](https://blog.51cto.com/u_15057807/4229426)

```sh
org.springframework.jdbc.UncategorizedSQLException: Error attempting to get column 'totalNumber' from result set.  Cause: com.microsoft.sqlserver.jdbc.SQLServerException: 不支持从 decimal 到 TIMESTAMP 的转换。
; uncategorized SQLException; SQL state [null]; error code [0]; 不支持从 decimal 到 TIMESTAMP 的转换。; nested exception is com.microsoft.sqlserver.jdbc.SQLServerException: 不支持从 decimal 到 TIMESTAMP 的转换。
```

* 原因分析
> 我的java模型类里面需要设置默认值为0，用了@Data和@Builder这两个注解，编译没有报错问题，运行时出现如上错误，结果最终发现是这两个注解不会生成无参构造导致的
> 所有用了builder模式，一定要同时添加无参构造和有参构造的注解
