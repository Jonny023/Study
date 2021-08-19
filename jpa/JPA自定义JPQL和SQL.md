# JPA自定义JPQL和SQL查询

## 1.自定义SQL

### 1.查询结果为Hibernate类【带有`@Entity`注解的类】

```java
//方式1 生成EntityManger
protected EntityManager em;
//执行原生SQL
Query nativeQuery = em.createNativeQuery(String sql);
//指定返回对象类型1
nativeQuery.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(Class resultType));
//指定查询参数,返回map
query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("createuser",userInfo.getUsercode()).setParameter("type", type); 

//返回对象1
List<T> resultList = nativeQuery.getResultList();

//返回对象2
em.createNativeQuery("select id, name, age from t_user", User.Class); 



//方式2 repository中直接写sql
@Query(value = "select name from test where name = ?", nativeQuery = true)
```

### 查询结果为自定义VO类

```java
//必须是hibernate的实entity类
//entityManager.createNativeQuery(sql, EntityClass.class)

//必须通过addScalar方法设置字段类型【普通的java类，如VO】
Query nativeQuery = entityManager.createNativeQuery("select id, username from user");
org.hibernate.query.Query query = nativeQuery.unwrap(NativeQuery.class)
    .addScalar("id", LongType.INSTANCE)
    .addScalar("username", StringType.INSTANCE)
    .setResultTransformer(Transformers.aliasToBean(UserVO.class));
System.out.println(JSON.toJSONString(query.list()));
```



## 2.JPQL语句

### 2.1 在repository中实现

```java
//返回指定实体类
@Query(value = "select new com.xxx.TestVO(a.id, a.name, max(a.index), count(a.id)) " +
            " from Test a where a.id = ?1 and a.name = ?2 " +
            "and a.type = ?3 group by a.parkNo order by time desc ")
List<TestVO> listObject(Long id, String name);

//返回map
@Query(value = "select new map(a.id, a.name, max(a.index) as index, count(a.id) as total) " +
            " from Test a where a.id = ?1 and a.name = ?2 " +
            "and a.type = ?3 group by a.parkNo order by time desc ")
List<Map<String, Object>> listMap(Long id, String name);

//实际使用的时候最好给参数指定 @Param 别名，然后使用 SpringEL 表达式 :#{#customer.firstname} 的方式获取对象参数的属性。
@Query("select u from User u where u.firstname = :#{#customer.firstname}")
List<User> findUsersByCustomersFirstname(@Param("customer") Customer customer);
```

### 2.2 repository中自定义方法名称

> @NamedQuery的name属性必须为：EntityName.methodName(实体类.方法名)

```java
//实体类
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
//@NamedQuery(name = "Student.findByName", query = "select stu from Student stu where stu.stuName = ?1")
@NamedQueries({
        @NamedQuery(name = "Student.findByName", query = "select stu from Student stu where stu.stuName = ?1"),
        @NamedQuery(name = "Student.queryMap", query = "select new map(stu.id as id, stu.stuName as name) from Student stu where stu.stuName = ?1"),
        @NamedQuery(name = "Student.queryStudentVO", query = "select new com.jonny.entity.vo.StudentVO(stu.id, stu.stuName) from Student stu where stu.stuName = ?1")
})
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String stuName;
    private char sex;
    private String address;
}



// repository
import com.jonny.entity.Student;
import com.jonny.entity.vo.StudentVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Map;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findByName(String stuName);
    Map<String, Object> queryMap(String stuName);
    StudentVO queryStudentVO(String stuName);
}
```

### 2.3 通过xml配置或properties查询语句

> 缺点：通过xml配置或者properties定义JPQL/SQL不能拆分到多个文件中

[参考1](https://examples.javacodegeeks.com/enterprise-java/jpa-named-query-example/)

[参考2](https://attacomsian.com/blog/spring-data-jpa-named-queries)

#### 方式1

1.创建`resources/META-INF/orm.xml`

```java
<?xml version="1.0" encoding="UTF-8"?>
<entity-mappings version="1.0" xmlns="http://java.sun.com/xml/ns/persistence/orm"
                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                 xsi:schemaLocation="http://java.sun.com/xml/ns/persistence/orm http://java.sun.com/xml/ns/persistence/orm_1_0.xsd">

    <named-query name="Demo.findByValue1">
        <query>SELECT d FROM Demo d where d.value=:value</query>
    </named-query>
</entity-mappings>
```

2.定义repository方法

```java
import com.jonny.entity.Demo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemoRepository extends JpaRepository<Demo, Long> {

    Demo findByValue1(String value);
}
```



#### 方式2

> 相比orm.xml要简单些

1.创建`resources/META-INF/jpa-named-queries.properties`

```properties
Demo.findByValue1=SELECT d FROM Demo d where d.value=:value
```

2.`repository`方法使用定义的配置

```java
package com.jonny.repository;

import com.jonny.entity.Demo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DemoRepository extends JpaRepository<Demo, Long> {

    @Query(name = "Demo.findByValue1")
    Demo findByValue1(String value);
}
```

