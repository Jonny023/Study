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

<!-- 此方式获取不到返回值，也就是无法获取out返回数据-->
<select id="generatorNo" statementType="CALLABLE" parameterMap="parameter" resultType="string">
    EXEC dbo.generator_no #{tenantId},#{code},#{businessDate},#{updateBy},#{updateName},#{p_code},#{p_message};
</select>
```


## 推荐用法

> 所有的参数都在RequestVO中定义好，入参IN和出参OUT都要定义
> 想要获取到返回参数，如code,message需要定义mode=OUT，而且必须定义到select标签里面，执行完成后会将结果返回到入参RequestVO里面，也就是引用传递（修改引用变量的值）
> 执行结果和code及message分开的，我的方法为`String call(RequestVO param)`,返回结果为String，而code、message状态码等在param参数中

```xml
<select id="generateBillNo" statementType="CALLABLE" parameterType="xxx.RequestVO" resultType="string">
    EXEC dbo.generator_no #{tenantId},#{code},#{businessDate},#{updateBy},#{updateName},#{code,mode=OUT,jdbcType=INTEGER},#{message,mode=OUT,jdbcType=VARCHAR}
</select>
```
