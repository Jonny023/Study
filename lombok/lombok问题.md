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