## Kettle常见问题

### postgreSQL

* `kettle`连接`postgreSQL`报时区错误
> 'FATAL: invalid value for parameter "TimeZone": "Asia/Shanghai"

* 在`Spoon.bat`文件中的`OPT`后面添加`"-Duser.timezone=UTC"`
```bash
set OPT=%OPT% %PENTAHO_DI_JAVA_OPTIONS% "-Dhttps.protocols=TLSv1,TLSv1.1,TLSv1.2" "-Djava.library.path=%LIBSPATH%" "-DKETTLE_HOME=%KETTLE_HOME%" "-DKETTLE_REPOSITORY=%KETTLE_REPOSITORY%" "-DKETTLE_USER=%KETTLE_USER%" "-DKETTLE_PASSWORD=%KETTLE_PASSWORD%" "-DKETTLE_PLUGIN_PACKAGES=%KETTLE_PLUGIN_PACKAGES%" "-DKETTLE_LOG_SIZE_LIMIT=%KETTLE_LOG_SIZE_LIMIT%" "-DKETTLE_JNDI_ROOT=%KETTLE_JNDI_ROOT%" "-Duser.timezone=UTC"
```
[参考地址](https://github.com/pentaho/pentaho-kettle/blob/master/pom.xml)


### MySQL

* `kettle`连接`mysql`提示`Driver class org.gjt.mm.mysql.Driver could not be found`的问题

```bash
将mysql版本降到5.+或者不改变原来的驱动版本（多版本共存）
```
