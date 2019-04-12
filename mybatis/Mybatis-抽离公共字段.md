## 公共配置`common.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>   
<!DOCTYPE mapper    
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"    
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">  
   
<mapper namespace="Common">  
    <sql id="Common.pagingStart">  
    </sql>  
    <sql id="Common.pagingEnd">  
        <![CDATA[ limit #{startWith,jdbcType=INTEGER},#{rows,jdbcType=INTEGER} ]]>  
    </sql>  
</mapper>  

```

## 在对应的mapper.xml中通过namespace进行引用

```xml
<select id="queryPage" resultMap="clientPage" parameterType="java.util.Map">  
    <include refid="Common.pagingStart"/>  
    <include refid="commonSelect"/>  
        <!-- 这里有个额外的1是为了避免额外处理最后一个”,“ -->  
    <include refid="commonFrom"/>  
    <include refid="commonWhere"/>  
      <if test="clientId != null" >  
        and CLIENT_ID = #{clientId,jdbcType=VARCHAR}  
      </if>  
      <if test="clientName != null" >  
        and CLIENT_NAME like '%${clientName}'  
      </if>  
      <if test="telephone != null" >  
        and TELEPHONE = #{telephone,jdbcType=VARCHAR}  
      </if>  
    order by client_id  
    <include refid="Common.pagingEnd"/>  
</select>  
```
