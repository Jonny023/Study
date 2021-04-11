
* servlet依赖问题

```bash
2021-02-01 19:09:52.676 ERROR 20024 --- [           main] org.apache.catalina.core.ContainerBase   : A child container failed during start

java.util.concurrent.ExecutionException: org.apache.catalina.LifecycleException: Failed to start component [NonLoginAuthenticator[StandardEngine[Tomcat].StandardHost[localhost].TomcatEmbeddedContext[]]]
	at java.util.concurrent.FutureTask.report(FutureTask.java:122) [na:1.8.0_181]
	at java.util.concurrent.FutureTask.get(FutureTask.java:192) [na:1.8.0_181]
```

```xml
<exclusions>
	<exclusion>
		<artifactId>servlet-api</artifactId>
		<groupId>javax.servlet</groupId>
	</exclusion>
</exclusions>
```

* 日志依赖问题

[连接](https://blog.csdn.net/blueheart20/article/details/80363870)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-web</artifactId>
         <exclusions>
            <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-logging</artifactId>
        </exclusion>
            <exclusion>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
        </exclusion>
    </exclusions>
</dependency>


<dependency>
  <groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter</artifactId>
	<exclusions>
		<exclusion>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-logging</artifactId>
		</exclusion>
	</exclusions>
</dependency>
```