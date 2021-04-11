### Spring security oauth2问题





* `token`存储设置为`new JdbcTokenStore(dataSource)`后

>  ERROR org.springframework.security.oauth2.provider.endpoint.TokenEndpoint:169 - Handling error: NullPointerException, null java.lang.NullPointerException: null

```bash
org.springframework.security.oauth2.provider.token.store.JdbcTokenStore:208 - Failed to deserialize authentication for 4557b852-35ae-4c79-9bc3-ff7b8437e4aa java.lang.IllegalArgumentException: java.io.StreamCorruptedException: invalid stream header: 5C786163
```

* 问题分析
  * 数据库字段类型不对
  * `byte`类型在`postgresql`中为`bytea`，设置成`varchar`导致反序列化失败



* 权限注解`@PreAuthorize("#oauth2.hasScope('read')")`