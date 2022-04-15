## 常用数据库jdbc url

> 凡是手动创建的示例，而不是交给spring类似框架管理的，都需要手动关闭连接，如手动创建DruidDataSource，直接调用，需要手动调用关闭连接

### postgresql

* 驱动`org.postgresql.Driver`

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.3.1</version>
</dependency>
```

```java
String url = "jdbc:postgresql://localhost:5432/dev?ssl=false&currentSchema=test";
String username = "gpadmin";
String password = "gpadmin";
Connection connection = DriverManager.getConnection(url, username, password);
Statement statement = connection.createStatement();
String sql = "SELECT id, (total::float8 * 3) as total FROM test.kpi";
ResultSet resultSet = statement.executeQuery(sql);
while (resultSet.next()) {
    //Long id = resultSet.getLong(1);
    //double v = resultSet.getDouble(2);

    Long id = resultSet.getLong("id");
    //double v = resultSet.getDouble("total");
    Float v = resultSet.getFloat("total");
    System.out.println("id:" + id + ", value: " + v);
}
connection.close();
resultSet.close();
statement.close();
```

### mysql

* 驱动
  * 5.5+ `com.mysql.jdbc.Driver`
  * 8.0+ `com.mysql.cj.jdbc.Driver`

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.49</version>
</dependency>
```

```java
String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&useSSL=false&allowMultiQueries=true";

String username = "root";
String password = "123456";
Connection connection = DriverManager.getConnection(url, username, password);
Statement statement = connection.createStatement();
String sql = "SELECT * FROM test";
ResultSet resultSet = statement.executeQuery(sql);
while (resultSet.next()) {

    int id = resultSet.getInt("id");
    String v = resultSet.getString("name");
    String type = resultSet.getString("type");
    System.out.println("id:" + id + ", value: " + v + ", type: " + type);
}
connection.close();
resultSet.close();
statement.close();
```

### oracle

* 驱动`oracle.jdbc.driver.OracleDriver`

* 切换schema: `alter session set current_schema=test`
* 获取当前模式：`SELECT sys_context('USERENV', 'CURRENT_SCHEMA') FROM dual;`

```xml
 <dependency>
     <groupId>cn.guoyukun.jdbc</groupId>
     <artifactId>oracle-ojdbc6</artifactId>
     <version>11.2.0.3.0</version>
</dependency>
```

```java
String url = "jdbc:oracle:thin:@//localhost:1521/helowin";
String username = "system";
String password = "system";
Connection connection = DriverManager.getConnection(url, username, password);
Statement statement = connection.createStatement();
String sql = "SELECT * FROM XX.DEMO";
ResultSet resultSet = statement.executeQuery(sql);
while (resultSet.next()) {

    int id = resultSet.getInt("ID");
    String name = resultSet.getString("NAME");
    String age = resultSet.getString("AGE");
    System.out.println("id:" + id + ", value: " + name + ", type: " + age);
}
connection.close();
resultSet.close();
statement.close();
```

### sqlserver(mssql)

* 驱动`com.microsoft.sqlserver.jdbc.SQLServerDriver`

```xml
<dependency>
    <groupId>com.microsoft.sqlserver</groupId>
    <artifactId>sqljdbc4</artifactId>
    <version>4.0</version>
</dependency>
```

```java
//sqlserver
String url = "jdbc:sqlserver://localhost:1433;databaseName=test";
String username = "sa";
String password = "123456";
Connection connection = DriverManager.getConnection(url, username, password);
Statement statement = connection.createStatement();
String sql = "SELECT * FROM student";
ResultSet resultSet = statement.executeQuery(sql);
while (resultSet.next()) {

    int id = resultSet.getInt("id");
    String v = resultSet.getString("stu_name");
    int age = resultSet.getInt("age");
    System.out.println("id:" + id + ", value: " + v + ", type: " + age);
}
```

## oracle数据库问题

> 通过jdbc连接oracle，数值类型不能用rs.getString()，可能造成精度丢失，
> 解决方法：通过rs.getObject()获取数据，判断如果为NUMBER并且值为null字符则单独处理为0

```java
package com.example.springbootoracle;

import net.minidev.json.JSONUtil;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Test {

    static {
        System.out.println("加载Test.class类");
    }

    public static void main(String[] args) throws SQLException {
        List<List<String>> allData = new ArrayList<>();
        ResultSet rs = null;
        Statement statement = null;
        Connection connection = null;
        try {
            JSONObject dataInfoJson = JSONUtil.parseObj(dataInfo);
            connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.1:1521:helowin", "system", "system");
            statement = connection.createStatement();
            statement.setFetchSize(10000);
            rs = statement.executeQuery(dataInfoJson.getStr("sql"));

            //表头
            ResultSetMetaData metaData = rs.getMetaData();
            List<String> rowData = null;
            String value;
            //抽取数据
            while (rs.next()) {
                rowData = new ArrayList<String>();
                int headIndex = 1;
                for (StructuresVO column : structures) {
                    value = String.valueOf(rs.getObject(column.getColumnName()));
                    //解决oracle小数点问题0.01 => .01 BigDecimal为空无法转换为String
                    if (Objects.equals("NUMBER", metaData.getColumnTypeName(headIndex))) {
                        if (Objects.equals(value, "null")) {
                            rowData.add("0");
                        } else {
                            rowData.add(value);
                        }
                    } else {
                        rowData.add(value);
                    }
                    headIndex++;
                }
                allData.add(rowData);
            }

        } finally {
            try {
                if (rs != null) rs.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


```
