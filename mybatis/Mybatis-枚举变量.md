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
