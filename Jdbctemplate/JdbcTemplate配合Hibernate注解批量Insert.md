# JdbcTemplate配合Hibernate注解批量Insert

* 实体类

> `@Table`和`@Column`注解必须要有，因为要自己解析表名和字段以及对象的值

```java
package com.example.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *  用户
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_user")
public class User {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "addr")
    private String addr;

    @Column(name = "create_time")
    private Timestamp createTime;
}
```

* 解析工具类

```java
package com.example.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author: Jonny
 * @description: sql批处理构建工具
 * @date:created in 2021/4/29 15:05
 * @modificed by:
 */
public class SqlWrapperUtil {

  /**
   * 获取字段个数
   */
  public static <T> int fieldSize(Class<T> clazz)
      throws IllegalAccessException, InstantiationException {
    List<Map<String, Object>> dataList = reflect(clazz.newInstance());
    return (int) dataList.stream().filter(map -> map.containsKey("columnName")).count();
  }

  /**
   * 构建insert sql
   */
  public static <T> String buildInsert(Class<T> clazz)
      throws IllegalAccessException, InstantiationException {
    List<Map<String, Object>> dataList = reflect(clazz.newInstance());
    StringBuilder sb = new StringBuilder();
    sb.append("insert into ");
    sb.append(table(clazz));
    sb.append("(");
    sb.append(dataList.stream().filter(map -> map.containsKey("columnName"))
        .map(map -> map.get("columnName").toString())
        .collect(Collectors.joining(",")));
    sb.append(") values");
    sb.append(Stream.generate(() -> "?")
        .limit(dataList.stream().filter(map -> map.containsKey("columnName")).count())
        .collect(Collectors.joining(",", "(", ")")));
    return sb.toString();
  }

  /**
   * 获取表名
   */
  private static <T> String table(Class<T> clazz) {
    Table annotation = clazz.getAnnotation(Table.class);
    if (annotation == null) {
      throw new RuntimeException("Please add the table annotation.");
    }
    return annotation.name();
  }

  /**
   * 获取数据值集合
   */
  public static <T> List<List<Map<String, Object>>> collect(List<T> lists) {
    List<List<Map<String, Object>>> result = lists.stream().map(data -> {
      try {
        return reflect(data);
      } catch (IllegalAccessException e) {
        return null;
      }
    }).collect(Collectors.toList());
    return result;
  }

  /**
   * 通过反射读取实例对象的字段及值
   */
  private static <T> List<Map<String, Object>> reflect(T bean) throws IllegalAccessException {
    if (bean == null) {
      return new ArrayList<>();
    }
    Field f;
    boolean flag;
    List<Map<String, Object>> list = new ArrayList<>();
    Map<String, Object> map;
    Field[] fields = bean.getClass().getDeclaredFields();
    for (int i = 0; i < fields.length; i++) {
      f = fields[i];
      f.setAccessible(true);
      map = new LinkedHashMap<>();
      flag = f.isAnnotationPresent(Column.class);
      if (flag) {
        map.put("columnName", f.getAnnotation(Column.class).name());
      }
      map.put("value", f.get(bean));
      list.add(map);
    }
    return list;
  }

}
```

* 批量Insert方法

```java
  @Resource
  private JdbcTemplate jdbcTemplate;

  @GetMapping("/saveAll")
  public String saveAll() throws InstantiationException, IllegalAccessException {
    long start = System.currentTimeMillis();
    List<User> users = new ArrayList<>();
    User user = null;
    Timestamp now = new Timestamp(System.currentTimeMillis());
    for (long i = 1; i <= 100000; i++) {
      user = new User().setId(i).setUsername("admin_" + i).setAddr("China_" + i).setCreateTime(now);
      users.add(user);
    }
    int fieldSize = SqlWrapperUtil.fieldSize(User.class);
    List<List<Map<String, Object>>> dataList = SqlWrapperUtil.collect(users);
    jdbcTemplate.batchUpdate(SqlWrapperUtil.buildInsert(User.class),
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
            Stream.iterate(0, n -> n + 1).limit(fieldSize).forEach(n -> {
              try {
                preparedStatement.setObject(n + 1, dataList.get(i).get(n).get("value"));
              } catch (SQLException sqlException) {
                sqlException.printStackTrace();
              }
            });
          }

          @Override
          public int getBatchSize() {
            return users.size();
          }
        });

    System.out.println("=====完成=====");
    double total = (System.currentTimeMillis() - start) / 1000;
    return "batch save success, time: " + total + "s";
  }
```

> JdbcTemplate测试10W数据最多只需2s，Hibernate改造后批量insert也要7s左右，测试数据库是clickhouse