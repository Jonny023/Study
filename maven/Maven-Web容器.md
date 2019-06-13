# Maven项目使用内嵌容器

* tomcat7

> 运行命令：tomcat:run

```xml
<build>
    <finalName>demo</finalName>
    <pluginManagement>
      <plugins>
        <plugin>
          <groupId>org.apache.tomcat.maven</groupId>
          <artifactId>tomcat7-maven-plugin</artifactId>
          <version>2.1</version>
          <configuration>
            <port>8080</port>
            <path>/demo</path>
            <uriEncoding>UTF-8</uriEncoding>
            <finalName>web</finalName>
            <server>tomcat7</server>
          </configuration>
        </plugin>
      </plugins>
    </pluginManagement>
</build>
```

* tomcat6

> 注意`tomcat6`使用jdk范围为`jdk1.6-jdk1.7`，若为`jdk1.8+`则会报如下错误

```bash
严重: Compilation error
org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException
	at org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader.<init>(ClassFileReader.java:342)
	at org.apache.jasper.compiler.JDTCompiler$1.findType(JDTCompiler.java:206)
	at org.apache.jasper.compiler.JDTCompiler$1.findType(JDTCompiler.java:163)
	at org.eclipse.jdt.internal.compiler.lookup.LookupEnvironment.askForType(LookupEnvironment.java:96)
	at org.eclipse.jdt.internal.compiler.lookup.UnresolvedReferenceBinding.resolve(UnresolvedReferenceBinding.java:49)
	at org.eclipse.jdt.internal.compiler.lookup.BinaryTypeBinding.resolveType(BinaryTypeBinding.java:97)
	at org.eclipse.jdt.internal.compiler.lookup.PackageBinding.getTypeOrPackage(PackageBinding.java:167)
	at org.eclipse.jdt.internal.compiler.lookup.Scope.getType(Scope.java:2187)
	at org.eclipse.jdt.internal.compiler.ast.TypeDeclaration.resolve(TypeDeclaration.java:974)
	at org.eclipse.jdt.internal.compiler.ast.TypeDeclaration.resolve(TypeDeclaration.java:1164)
	at org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration.resolve(CompilationUnitDeclaration.java:366)
	at org.eclipse.jdt.internal.compiler.Compiler.process(Compiler.java:623)
	at org.eclipse.jdt.internal.compiler.Compiler.compile(Compiler.java:392)
	at org.apache.jasper.compiler.JDTCompiler.generateClass(JDTCompiler.java:429)
	at org.apache.jasper.compiler.Compiler.compile(Compiler.java:349)
	at org.apache.jasper.compiler.Compiler.compile(Compiler.java:327)
	at org.apache.jasper.compiler.Compiler.compile(Compiler.java:314)
	at org.apache.jasper.JspCompilationContext.compile(JspCompilationContext.java:592)
	at org.apache.jasper.servlet.JspServletWrapper.service(JspServletWrapper.java:317)
	at org.apache.jasper.servlet.JspServlet.serviceJspFile(JspServlet.java:313)
	at org.apache.jasper.servlet.JspServlet.service(JspServlet.java:260)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:717)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:290)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:206)
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:233)
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:191)
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:127)
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:102)
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:109)
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:298)
	at org.apache.coyote.http11.Http11Processor.process(Http11Processor.java:857)
	at org.apache.coyote.http11.Http11Protocol$Http11ConnectionHandler.process(Http11Protocol.java:588)
	at org.apache.tomcat.util.net.JIoEndpoint$Worker.run(JIoEndpoint.java:489)
	at java.lang.Thread.run(Thread.java:748)

六月 13, 2019 8:54:57 下午 org.apache.catalina.core.StandardWrapperValve invoke
严重: Servlet.service() for servlet jsp threw exception
org.apache.jasper.JasperException: Unable to compile class for JSP: 

An error occurred at line: 1 in the generated java file
The type java.io.ObjectInputStream cannot be resolved. It is indirectly referenced from required .class files

Stacktrace:
	at org.apache.jasper.compiler.DefaultErrorHandler.javacError(DefaultErrorHandler.java:92)
	at org.apache.jasper.compiler.ErrorDispatcher.javacError(ErrorDispatcher.java:330)
	at org.apache.jasper.compiler.JDTCompiler.generateClass(JDTCompiler.java:439)
	at org.apache.jasper.compiler.Compiler.compile(Compiler.java:349)
	at org.apache.jasper.compiler.Compiler.compile(Compiler.java:327)
	at org.apache.jasper.compiler.Compiler.compile(Compiler.java:314)
	at org.apache.jasper.JspCompilationContext.compile(JspCompilationContext.java:592)
	at org.apache.jasper.servlet.JspServletWrapper.service(JspServletWrapper.java:317)
	at org.apache.jasper.servlet.JspServlet.serviceJspFile(JspServlet.java:313)
	at org.apache.jasper.servlet.JspServlet.service(JspServlet.java:260)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:717)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:290)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:206)
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:233)
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:191)
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:127)
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:102)
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:109)
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:298)
	at org.apache.coyote.http11.Http11Processor.process(Http11Processor.java:857)
	at org.apache.coyote.http11.Http11Protocol$Http11ConnectionHandler.process(Http11Protocol.java:588)
	at org.apache.tomcat.util.net.JIoEndpoint$Worker.run(JIoEndpoint.java:489)
	at java.lang.Thread.run(Thread.java:748)
```

* `tomcat6`配置

```xml
<build>
    <finalName>demo</finalName>
    <pluginManagement>
      <plugins>
        <plugin>
          <groupId>org.apache.tomcat.maven</groupId>
          <artifactId>tomcat6-maven-plugin</artifactId>
          <version>2.0</version>
          <configuration>
            <port>80</port>
            <path>/demo</path>
            <uriEncoding>UTF-8</uriEncoding>
            <finalName>web</finalName>
            <server>tomcat</server>
          </configuration>
        </plugin>
      </plugins>
    </pluginManagement>
</build>
```
