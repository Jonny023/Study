## mybatis级联查询

* mapper.xml

> 级联查询是先查询主体，然后循环调用查询内部子查询，级联查询的mapper select方法可以不用在Mapper接口中声明

```sql
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.springbootmybatis.mapper.UserMapper">

    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="com.example.springbootmybatis.entity.User">
        <id column="id" property="id" />
        <result column="name" property="name" />
        <result column="role_id" property="roleId" />
        <!--一对一-->
        <association property="role" column="role_id" javaType="com.example.springbootmybatis.entity.Role" select="getRole"/>
        <!--一对多-->
        <collection property="depts" column="id" ofType="com.example.springbootmybatis.entity.Dept" select="getDeptList" fetchType="lazy"/>
    </resultMap>

    <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
        id, name, role_id
    </sql>

    <select id="getUserList" resultMap="BaseResultMap">
        select * from user
    </select>

    <select id="getRole" resultType="com.example.springbootmybatis.entity.Role">
        select * from role where id = #{role_id}
    </select>

    <select id="getDeptList" resultType="com.example.springbootmybatis.entity.Dept">
        SELECT d.* FROM `user` u LEFT JOIN `user_dept` ud ON u.id = ud.user_id LEFT JOIN dept d ON d.id = ud.dept_id WHERE u.id = #{id}
    </select>

</mapper>
```

* User类

> 级联查询必须加注解：@JsonIgnoreProperties(value = "handler")，不然jackson解析不了

```java
package com.example.springbootmybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
@JsonIgnoreProperties(value = "handler")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer roleId;

    @TableField(exist = false)
    private Role role;

    @TableField(exist = false)
    private List<Dept> depts;
}

```
