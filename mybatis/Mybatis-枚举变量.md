## mybatis xml中使用枚举

```xml
<select id="pageCount" resultType="Integer">
    SELECT
        count(1)
    FROM
        tableName
    where xxx = #{hospitalId}
    <choose>
        <when test="type == @com.cnskytec.coding.enums.AppointmentQueryEnum@COMMUNICATE">
            and status in (-1, 0)
        </when>
        <otherwise>
            and status = ${@com.cnskytec.coding.enums.AppointmentQueryEnum@COMMUNICATE}
        </otherwise>
    </choose>
    order by add_time desc
</select>
```

## 枚举映射问题

```java
org.apache.ibatis.executor.resultset.DefaultResultSetHandler#applyAutomaticMappings
org.apache.ibatis.type.BaseTypeHandler#getResult(java.sql.ResultSet, java.lang.String)
com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler#valueOf
```
