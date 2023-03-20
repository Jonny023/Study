## Mybatis.xml常用

* bind标签

> 防止sql注入

```xml
<bind name="keyword" value="'%'+param.keyword+'%'"/>
and xxx like #{keyword}
```

* Boolean类型判断true或false

```xml
<if test="params.isExcludeOperator != null and params.isExcludeOperator">

</if>
```

* list判断非空

```xml
<if test="params.userList != null and params.userList.size() > 0">
</if>
```
