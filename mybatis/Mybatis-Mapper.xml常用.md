## Mybatis.xml常用

### bind标签

> 防止sql注入

```xml
<bind name="keyword" value="'%'+param.keyword+'%'"/>
and xxx like #{keyword}
```

### Boolean类型判断true或false

```xml
<if test="params.isExcludeOperator != null and params.isExcludeOperator">

</if>
```

### list判断非空

```xml
<if test="params.userList != null and params.userList.size() > 0">
</if>
```

### sql in查询分批
> 数据库in查询有限制，通过or in()防止超过限制，下面sql里面的index % 1999 == 1998表示美2000条分为一批，在同一个in中

```sql
<if test="params.goodsIds != null and params.goodsIds.size() > 0">
  AND EXISTS (SELECT 1 FROM goods billgoods WHERE billgoods.bill_id = bill.id
  AND (billgoods.goods_id IN
  <foreach collection="params.goodsIds" index="index" open="(" close=")" item="item">
    <if test="index > 0">
      <choose>
        <when test="index % 1999 == 1998">) OR billgoods.goods_id IN (</when>
        <otherwise>,</otherwise>
      </choose>
    </if>
    #{item}
  </foreach>
  ))
</if>
```
