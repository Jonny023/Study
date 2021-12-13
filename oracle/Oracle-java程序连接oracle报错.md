## java程序访问oracle报错

## 错误：IO Error: Connection reset

```sql
2021-12-13 11:05:07.443 ERROR 23326 --- [nio-9870-exec-7] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is java.sql.SQLRecoverableException: IO Error: Connection reset] with root cause

java.net.SocketException: Connection reset
	at java.net.SocketInputStream.read(SocketInputStream.java:210) ~[na:1.8.0_302]
	at java.net.SocketInputStream.read(SocketInputStream.java:141) ~[na:1.8.0_302]
	at oracle.net.ns.Packet.receive(Packet.java:300) ~[oracle-ojdbc6-11.2.0.3.0.jar!/:11.2.0.3.0]
	at oracle.net.ns.NSProtocol.connect(NSProtocol.java:296) ~[oracle-ojdbc6-11.2.0.3.0.jar!/:11.2.0.3.0]
	at oracle.jdbc.driver.T4CConnection.connect(T4CConnection.java:1102) ~[oracle-ojdbc6-11.2.0.3.0.jar!/:11.2.0.3.0]
	at oracle.jdbc.driver.T4CConnection.logon(T4CConnection.java:320) ~[oracle-ojdbc6-11.2.0.3.0.jar!/:11.2.0.3.0]
	at oracle.jdbc.driver.PhysicalConnection.<init>(PhysicalConnection.java:546) ~[oracle-ojdbc6-11.2.0.3.0.jar!/:11.2.0.3.0]
	at oracle.jdbc.driver.T4CConnection.<init>(T4CConnection.java:236) ~[oracle-ojdbc6-11.2.0.3.0.jar!/:11.2.0.3.0]
	at oracle.jdbc.driver.T4CDriverExtension.getConnection(T4CDriverExtension.java:32) ~[oracle-ojdbc6-11.2.0.3.0.jar!/:11.2.0.3.0]
	at oracle.jdbc.driver.OracleDriver.connect(OracleDriver.java:521) ~[oracle-ojdbc6-11.2.0.3.0.jar!/:11.2.0.3.0]
	at java.sql.DriverManager.getConnection(DriverManager.java:664) ~[na:1.8.0_302]
	at java.sql.DriverManager.getConnection(DriverManager.java:247) ~[na:1.8.0_302]
	at com.example.springbootoracle.controller.Main.index(Main.java:18) ~[classes!/:0.0.1-SNAPSHOT]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_302]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_302]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_302]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_302]
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205) ~[spring-web-5.3.13.jar!/:5.3.13]
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:150) ~[spring-web-5.3.13.jar!/:5.3.13]
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:117) ~[spring-webmvc-5.3.13.jar!/:5.3.13]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:895) ~[spring-webmvc-5.3.13.jar!/:5.3.13]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808) ~[spring-webmvc-5.3.13.jar!/:5.3.13]
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87) ~[spring-webmvc-5.3.13.jar!/:5.3.13]
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1067) ~[spring-webmvc-5.3.13.jar!/:5.3.13]
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:963) ~[spring-webmvc-5.3.13.jar!/:5.3.13]
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006) ~[spring-webmvc-5.3.13.jar!/:5.3.13]
	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898) ~[spring-webmvc-5.3.13.jar!/:5.3.13]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:655) ~[tomcat-embed-core-9.0.55.jar!/:na]
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883) ~[spring-webmvc-5.3.13.jar!/:5.3.13]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:764) ~[tomcat-embed-core-9.0.55.jar!/:na]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:227) ~[tomcat-embed-core-9.0.55.jar!/:na]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.55.jar!/:na]
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53) ~[tomcat-embed-websocket-9.0.55.jar!/:na]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189) ~[tomcat-embed-core-9.0.55.jar!/:na]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.55.jar!/:na]
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100) ~[spring-web-5.3.13.jar!/:5.3.13]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.3.13.jar!/:5.3.13]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189) ~[tomcat-embed-core-9.0.55.jar!/:na]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.55.jar!/:na]
	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93) ~[spring-web-5.3.13.jar!/:5.3.13]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.3.13.jar!/:5.3.13]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189) ~[tomcat-embed-core-9.0.55.jar!/:na]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.55.jar!/:na]
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201) ~[spring-web-5.3.13.jar!/:5.3.13]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.3.13.jar!/:5.3.13]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189) ~[tomcat-embed-core-9.0.55.jar!/:na]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.55.jar!/:na]
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:197) ~[tomcat-embed-core-9.0.55.jar!/:na]
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97) [tomcat-embed-core-9.0.55.jar!/:na]
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:540) [tomcat-embed-core-9.0.55.jar!/:na]
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:135) [tomcat-embed-core-9.0.55.jar!/:na]
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92) [tomcat-embed-core-9.0.55.jar!/:na]
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78) [tomcat-embed-core-9.0.55.jar!/:na]
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:357) [tomcat-embed-core-9.0.55.jar!/:na]
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:382) [tomcat-embed-core-9.0.55.jar!/:na]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65) [tomcat-embed-core-9.0.55.jar!/:na]
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:895) [tomcat-embed-core-9.0.55.jar!/:na]
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1722) [tomcat-embed-core-9.0.55.jar!/:na]
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49) [tomcat-embed-core-9.0.55.jar!/:na]
	at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191) [tomcat-embed-core-9.0.55.jar!/:na]
	at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659) [tomcat-embed-core-9.0.55.jar!/:na]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) [tomcat-embed-core-9.0.55.jar!/:na]
	at java.lang.Thread.run(Thread.java:748) [na:1.8.0_302]
```

## 排查

> 1.首先想到了网络是不是相通，可以通过`telnet ip:port`测试，但是我的服务器上没有`telnet命令`
>
> 2.通过`curl`命令提示，`curl localhost:1521`

```shell
# 正常【可访问外网】
[root@vm01 test]# curl 192.168.1.101:1521
curl: (52) Empty reply from server

# 异常【无法访问外网】
[root@vm01 test]# curl baidu.com
<h2>move</h2>
```

