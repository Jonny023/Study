# Mybatis入门篇

* 数据库
```sql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sex` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `details` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '男', '阿三', '{\"addr\": \"北京市\", \"area\": \"深圳市\"}');
INSERT INTO `user` VALUES (2, '女', '小丽', '{\"addr\": \"北京市\", \"area\": \"深圳市12311\"}');
INSERT INTO `user` VALUES (3, '男', '常州工业集团', '{\"addr\": \"北京市\", \"area\": \"上海市1\"}');
INSERT INTO `user` VALUES (4, '男', '茕茕孑立', '{\"addr\": \"北京市\", \"area\": \"重庆\"}');
INSERT INTO `user` VALUES (5, '男', '植物园', '{\"addr\": \"北京市\", \"area\": \"重庆\"}');
INSERT INTO `user` VALUES (6, '男', '动物园', '{\"addr\": \"北京市\", \"area\": \"重庆\"}');
INSERT INTO `user` VALUES (7, '男', 'Springboot', '{\"addr\": \"北京市\", \"area\": \"重庆\"}');
INSERT INTO `user` VALUES (8, '男', '欣欣向荣', '{\"addr\": \"北京市\", \"area\": \"重庆\"}');
INSERT INTO `user` VALUES (9, '男', '阿三', '{\"addr\": \"北京市\", \"area\": \"重庆\"}');
INSERT INTO `user` VALUES (10, '男', 'Java', '{\"addr\": \"北京市\", \"area\": \"重庆\"}');
INSERT INTO `user` VALUES (11, '男', 'Grails', '{\"addr\": \"北京市\", \"area\": \"重庆\"}');
INSERT INTO `user` VALUES (12, '男', 'html', '{\"addr\": \"北京市\", \"area\": \"重庆\"}');
INSERT INTO `user` VALUES (13, '男', 'css', '{\"addr\": \"北京市\", \"area\": \"重庆\"}');

SET FOREIGN_KEY_CHECKS = 1;
```

* 新建一个`springboot`项目

* 在`resources`下创建配置文件`mybatis.xml`

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/test"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="mapper/UserMapper.xml"/>
    </mappers>
</configuration>
```

 * 在`resources`下创建映射文件`resource/mapper/UserMapper.xml`
 
 ```java
 <?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.mybatis.dao.UserMapper">
    <select id="get" resultType="com.example.mybatis.entity.User">
        select * from user where id = #{id}
    </select>
    <update id="update" >
        update user set details = json_replace(details,'$.area',#{addr}) where id = '${id}'
    </update>
</mapper>
 ```
 
 * 新建模型类`User`
 
 ```java
 package com.example.mybatis.entity;

/**
 * @Author Lee
 * @Description
 * @Date 2019年04月24日 21:56
 */
public class User {

    private Integer id;
    private char sex;
    private String nickname;
    private String details;

    public User() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", sex=" + sex +
                ", nickname='" + nickname + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
 ```
 
 * 新建`UserMapper`类
 
 ```java
 package com.example.mybatis.dao;

import com.example.mybatis.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * @Author Lee
 * @Description
 * @Date 2019年04月24日 21:58
 */
public interface UserMapper {

    User get(@Param("id") Integer id);

    int update(@Param("id") Integer id, @Param("addr") String addr);
}
 ```
 
 * 创建测试类
 
 ```java
 package com.example.mybatis;

import com.example.mybatis.dao.UserMapper;
import com.example.mybatis.entity.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class MybatisApplicationTests {

    @Test
    public void contextLoads() throws IOException {

        String resource = "mybatis.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User user = userMapper.get(2);
        System.out.println("修改前的值："+user);

        int rows = userMapper.update(2,"深圳市12311");
        try {
            if(rows!=0) {
                throw new Exception("error");
            }
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("影响行数:"+rows);
        System.out.println("修改后的值："+userMapper.get(2));
    }

}
 ```
