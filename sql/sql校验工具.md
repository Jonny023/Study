## sql校验工具

* 依赖

```xml
<dependency>
    <groupId>com.github.jsqlparser</groupId>
    <artifactId>jsqlparser</artifactId>
    <version>4.7</version>
</dependency>
```

* 例子

```java
// 校验查询语句是否正确，存在错误抛出JSQLParserException异常
String sql = "select id, name from user where 1z1;>2";
// Select parse = (Select) CCJSqlParserUtil.parse(sql);

// 校验指定数据库类型语句是否正确
String sql1 = "select id, name from user where x x 2";
Validation validation = new Validation(Arrays.asList(DatabaseType.SQLSERVER, DatabaseType.MARIADB, DatabaseType.POSTGRESQL, DatabaseType.H2), sql1);
List<ValidationError> errors1 = validation.validate();
log.error("sql1存在错误: {}", !errors1.isEmpty());

// 校验DML
String sql2 = "DROP INDEX IF EXISTS idx_tab2_id;";
Validation validation2 = new Validation(Collections.singletonList(FeaturesAllowed.DML), sql2);
List<ValidationError> errors2 = validation2.validate();
// 只允许DML,使用DDL则报错
log.error("sql2存在错误: {}", !errors2.isEmpty());
```
