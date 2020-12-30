# Mybatis传入集合

#### 固定写法

* mapper list集合xml解析写法

```bash
public List<User> list(List<String> ids) {}

public List<User> list(@Param("list") List<String> ids) {}
```

* 对应的mapper.xml中的collection为固定的list，map的key

```xml
<select id="list">
    <foreach index="index" item="id" collection="ids" open="(" separator="," close=")">
       id in #{id}
    </foreach>
</select>
```



#### 对象里面的集合

```java
public List<User> list(User u) {}
```

```xml
<select id="list">
    <foreach index="index" collection="user.ids" open="(" separator="," close=")">
       f.STAFF_ID = #{user.ids[${index}]}
    </foreach>
</select>
```
