# Spring Security常见问题 （Oauth2）



### 问题1

```java
Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
2021-03-23 18:10:31.484 ERROR org.springframework.boot.diagnostics.LoggingFailureAnalysisReporter:40 - 

***************************
APPLICATION FAILED TO START
***************************

Description:

The bean 'metaDataSourceAdvisor' could not be registered. A bean with that name has already been defined and overriding is disabled.

Action:

Consider renaming one of the beans or enabling overriding by setting spring.main.allow-bean-definition-overriding=true
```

* 原因分析
  * 多个类上添加了`@EnableGlobalMethodSecurity(prePostEnabled = true)`注解，去掉即可



* `scope`权限

```java
@PreAuthorize("#oauth2.hasScope('write')")
```



### 问题2

* `token`存储设置为`new JdbcTokenStore(dataSource)`后

>  ERROR org.springframework.security.oauth2.provider.endpoint.TokenEndpoint:169 - Handling error: NullPointerException, null java.lang.NullPointerException: null

```bash
org.springframework.security.oauth2.provider.token.store.JdbcTokenStore:208 - Failed to deserialize authentication for 4557b852-35ae-4c79-9bc3-ff7b8437e4aa java.lang.IllegalArgumentException: java.io.StreamCorruptedException: invalid stream header: 5C786163
```

* 问题分析
  * 数据库字段类型不对
  * `byte`类型在`postgresql`中为`bytea`，设置成`varchar`导致反序列化失败



### 问题3

> ERROR: column "refresh_token" is of type bytea but expression is of type character varying

* 解决方法

  * 在url配置上加上`?stringtype=unspecified`

    ```
    url: jdbc:postgresql://localhost:5432/db?stringtype=unspecified
    ```

    