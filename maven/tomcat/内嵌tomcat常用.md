# 配置虚拟映射路径

* 直接配置报错，错误提示如下：
   ```bash
    Caused by: java.lang.IllegalArgumentException: addChild: Child name '/store' is not unique
   ```
   * 原因分析：`pom.xml`的配置并没有覆盖`tomcat/conf/server.xml`中的配置，导致配置中存在多个相同配置
   
# 解决方案

* 下载当前目录下的`tomcat7-maven-plugin-2.2.jar.zip`文件，解压并覆盖本地仓库中的`tomcat7-maven-plugin-2.2.jar`，
比如我的本地仓库在：`D:\M2REPO\org\apache\tomcat\maven\tomcat7-maven-plugin\2.2`下，那么我们只需要解压并此目录下的
`tomcat7-maven-plugin-2.2.jar`覆盖此文件就ok.

  

> `pom.xml`配置

```xml
<plugins>
    <!-- 指定jdk1.7编译，否则maven update 可能调整jdk -->
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>2.3.2</version>
        <configuration>
            <source>1.7</source>
            <target>1.7</target>
            <encoding>UTF-8</encoding>
        </configuration>
    </plugin>
    <!-- tomcat7插件。使用方式：tomcat7:run -->
    <plugin>
        <groupId>org.apache.tomcat.maven</groupId>
        <artifactId>tomcat7-maven-plugin</artifactId>
        <version>2.2</version>
        <configuration>
            <update>true</update>
            <port>8080</port>
            <uriEncoding>UTF-8</uriEncoding>
            <server>tomcat7</server>
            <!-- tomcat虚拟映射路径 -->
            <staticContextPath>/store</staticContextPath>
            <staticContextDocbase>d:/file/store/</staticContextDocbase>
            <contextReloadable>false</contextReloadable>
            <useTestClasspath>true</useTestClasspath>
        </configuration>
    </plugin>
</plugins>
```

# [参考地址](https://blog.csdn.net/weixin_40857593/article/details/84959938)
