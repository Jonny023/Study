## mybatis调用存储过程

> 网上的示例代码都是直接调用`call xxxx(#{id})`,需要将参数抽取到parameterMap中，以下示例为sqlserver的存储过程

* mode="IN" 入参
* mode="OUT" 返回参数

```xml
<parameterMap id="parameter" type="com.example.DemoVO">
    <parameter property="tenantId" mode="IN" jdbcType="BIGINT"/>
    <parameter property="code" mode="IN" jdbcType="INTEGER"/>
    <parameter property="businessDate" mode="IN" jdbcType="VARCHAR"/>
    <parameter property="updateBy" mode="IN" jdbcType="BIGINT"/>
    <parameter property="updateName" mode="IN" jdbcType="VARCHAR"/>
    <parameter property="p_code" mode="OUT" jdbcType="INTEGER"/>
    <parameter property="p_message" mode="OUT" jdbcType="VARCHAR"/>
</parameterMap>

<select id="generatorNo" statementType="CALLABLE" parameterMap="parameter" resultType="string">
    EXEC dbo.generator_no #{tenantId},#{code},#{businessDate},#{updateBy},#{updateName},#{p_code},#{p_message};
</select>
```
