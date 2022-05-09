# Greenplum(postgresql)时区问题

[参考](https://blog.csdn.net/yeqiyugood/article/details/124437945)

> 解决方案3种，网上说的设置jdbc url参数`?TimeZone=Asia/Shanghai`或`?TimeZone=PRC`根本没有任何效果，强烈建议别测试了

1. 修改数据库配置文件`postgresql.conf`

2. 配置VM参数
3. 连接池配置

## 1.修改配置文件

> 修改配置后需要重启数据库服务

```sh
# 编辑配置
vim /opt/greenplum/gpdata/master/gpseg-1/postgres.conf

# 修改timezone为PRC或Asia/Shanghai
#timezone = 'UTC'
timezone = 'PRC'
```



## 2.配置vm option参数

```sh
-Duser.timezone=PRC
```



## 3.连接池

### hikari

> PRC和Asia/Shanghai都是东8区

#### 代码配置

```java
hikariConfig.setConnectionInitSql("set time zone 'PRC'");
```

#### yml配置

```yaml
spring:
  datasource:
    verify:
      url: jdbc:mysql://localhost:3306/test
      username: test
      password: test1234
      driver-class-name: com.mysql.jdbc.Driver
      test-while-idle: true
      validation-query: SELECT 1
      connection-init-sqls:
        - set sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
        - set names utf8mb4;
        - set time zone 'PRC'
```



### druid

```yaml
spring:
    datasource:
        druid:
            connection-init-sqls: set time zone 'Asia/Shanghai' # Asia/Shanghai和PRC都是东8区
```

