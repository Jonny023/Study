# JDBC setFetchSize

[参考](https://my.oschina.net/liuyuanyuangogo/blog/2413741)

## 代码

> 原本以为`statement`的`setFetchSize`就是sql查询的limit，结果是理解错误了，他是为了防止一次性拉取的数据量太大了导致OOM，个人理解就想分批一样，`mysql5.0+`需要配合jdbc url设置`useCursorFetch=true`

```java
import java.sql.*;

public class MysqlTest {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/test?serverTimezone=Asia/Shanghai&useCursorFetch=true";
        String username = "root";
        String password = "root";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            String sql = "select * from user";
            Statement statement = connection.createStatement();
            //Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            statement.setFetchSize(2);
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                System.out.printf("id: %d, name: %s, fetchSize: %d, \n", resultSet.getLong("id"), resultSet.getString("name"), resultSet.getFetchSize());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```

## 输出

> 不设置useCursorFetch=true，fetchSize为0

```bash
id: 1, name: 张三, fetchSize: 2, 
id: 2, name: 李四, fetchSize: 2, 
id: 3, name: 王五, fetchSize: 2, 
id: 4, name: 小明, fetchSize: 2, 
```

