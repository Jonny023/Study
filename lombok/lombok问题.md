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


### builder问题

> 没有继承其他类不要用@SuperBuilder来构建，要用@Builder构建，否则报错：`Cause: java.lang.IllegalArgumentException: argument type mismatch`，意思
> 是没有找到对应的构造函数，如果继承了父类，子类要用builder()方法构建对象，子类则需要添加`@SuperBuilder`注解
