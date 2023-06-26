#### Lombok问题

* 集成swagger时实体类属性和swagger-ui返回属性对不上（解析错误）

  > 如：将`uName`解析成了`UName`

* 解决方法，通过`@JsonProperty`指定映射名称

  ```java
  @JsonProperty("uName")
  @ApiModelProperty("用户名")
  private String uName;
  ```

 * `@Accessors(chain = true)`注解允许链式调用
 
 ```java
 new User().name("test").age(20);
 ```


### builder问题1

> 没有继承其他类不要用@SuperBuilder来构建，要用@Builder构建，否则报错：`Cause: java.lang.IllegalArgumentException: argument type mismatch`，意思
> 是没有找到对应的构造函数，如果继承了父类，子类要用builder()方法构建对象，子类则需要添加`@SuperBuilder`注解


### builder问题2

> 在适用@Builder注解通过build模式构建实例时，需要同时使用如下注解，不然可能导致报错，我们开发时遇到过mybati查询返回model只添加@Data和@Builder报错了，提示下标越界
> 问题分析：原因是用了@Data和@Builder相当于只有一个固定顺序的有参构造器，而某些地方使用的有参构造的参数顺序和默认的有参构造不一致

* 解决方法，使用到@Builder注解的类同时添加有参和无参构造器

```java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
```
