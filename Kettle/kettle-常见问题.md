## Windows Kettle常见问题【Kettle 8.3】

### postgreSQL

* `kettle`连接`postgreSQL`报时区错误
> 'FATAL: invalid value for parameter "TimeZone": "Asia/Shanghai"

* 在`Spoon.bat`文件中的`OPT`后面添加`"-Duser.timezone=UTC"`
```bash
set OPT=%OPT% %PENTAHO_DI_JAVA_OPTIONS% "-Dhttps.protocols=TLSv1,TLSv1.1,TLSv1.2" "-Djava.library.path=%LIBSPATH%" "-DKETTLE_HOME=%KETTLE_HOME%" "-DKETTLE_REPOSITORY=%KETTLE_REPOSITORY%" "-DKETTLE_USER=%KETTLE_USER%" "-DKETTLE_PASSWORD=%KETTLE_PASSWORD%" "-DKETTLE_PLUGIN_PACKAGES=%KETTLE_PLUGIN_PACKAGES%" "-DKETTLE_LOG_SIZE_LIMIT=%KETTLE_LOG_SIZE_LIMIT%" "-DKETTLE_JNDI_ROOT=%KETTLE_JNDI_ROOT%" "-Duser.timezone=UTC"
```
[参考地址](https://github.com/pentaho/pentaho-kettle/blob/master/pom.xml)

* 进行数据抽取的时候类型必须完全匹配，比如：postgreSQL表字段类型为字符型，从mysql查询的结果集也必须为字符，如下方式转换

```sql
select concat(count(1), '') from dual
```

### MySQL

* `kettle`连接`mysql`提示`Driver class org.gjt.mm.mysql.Driver could not be found`的问题

> 下载5.+的驱动放到kettle/lib目录下，重启kettle客户端

```bash
将mysql版本降到5.+或者不改变原来的驱动版本（多版本共存）
```
