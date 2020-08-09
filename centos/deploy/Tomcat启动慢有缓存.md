### tomcat启动慢有缓存

> `tomcat`启动卡在webapps/`ROOT`界面，不向下执行

```bash
Aug 08, 2020 10:10:18 PM org.apache.coyote.AbstractProtocol init
INFO: Initializing ProtocolHandler ["http-nio-8088"]
Aug 08, 2020 10:10:18 PM org.apache.catalina.startup.Catalina load
INFO: Server initialization in [3137] milliseconds
Aug 08, 2020 10:10:18 PM org.apache.catalina.core.StandardService startInternal
INFO: Starting service [Catalina]
Aug 08, 2020 10:10:18 PM org.apache.catalina.core.StandardEngine startInternal
INFO: Starting Servlet engine: [Apache Tomcat/9.0.37]
Aug 08, 2020 10:10:18 PM org.apache.catalina.startup.HostConfig deployDirectory
INFO: Deploying web application directory [/usr/local/env/tomcat/apache-tomcat-9.0.37/webapps/ROOT]
```



* 修改`$JAVA_PATH/jre/lib/security/java.security`中参数`securerandom.source`为：

  ```bash
  securerandom.source=file:/dev/urandom
  ```

  > `vi`编辑器输入`:/关键字`即可搜索，`n`查找下一个

### [参考文档](https://www.jianshu.com/p/de2df2d92d92)