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
