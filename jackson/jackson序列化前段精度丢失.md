# jackson序列化问题

> 前端无法接收后端json序列化的long值数据，因为前端只有int类型，存不了long值那么长，导致数据不正确，需要将其序列化为字符串

### 1. 全局配置bean

```java
@Bean
public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    return builder -> {
        builder.serializerByType(Long.class, ToStringSerializer.instance);
    };
}
```

### 2. 局部配置

> 在需要序列化类属性局部配置

```java
@JsonSerialize(using = ToStringSerializer.class)
private Long id;
```

