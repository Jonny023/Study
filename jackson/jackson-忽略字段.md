# jackson忽略返回字段

> @JsonIgnore不生效可以用@JsonIgnoreProperties，@JsonIgnore作用在字段上

```java
// 类注解
@JsonIgnoreProperties({ "id", "organizationParentId", "organizationAllPathId", "total", "dyAuthTotal", "ksAuthTotal" })
```
