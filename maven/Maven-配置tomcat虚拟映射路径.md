
## 1、配置tomcat虚拟映射路径

> 作用：文件上传到任意磁盘，用作访问映射

###  1.1 修改`pom.xml`

```xml
<build>
    <finalName>test</finalName>
    <defaultGoal>compile</defaultGoal>
    <resources>
        <!--编译之后包含xml-->
        <resource>
            <directory>src/main/java</directory>
            <includes>
                <include>**/*.xml</include>
                <include>**/*.properties</include>
            </includes>
            <excludes>
                <exclude>**/*.java</exclude>
            </excludes>
            <filtering>true</filtering>
        </resource>
        <!--解决maven项目resources目录显示为普通目录问题-->
        <resource>
            <directory>src/main/resources</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.xml</include>
                <include>**/*.json</include>
            </includes>
        </resource>
    </resources>
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
                <staticContextPath>/file/upload</staticContextPath>
                <staticContextDocbase>d:/upload/</staticContextDocbase>
                <contextReloadable>false</contextReloadable>
                <useTestClasspath>true</useTestClasspath>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### 1.2 下载并解压当前目录下的tomcat7-maven-plugin压缩文件到本地仓库覆盖掉本地仓库中的`tomcat7-maven-plugin-2.2.jar`，不然会报错

* 如我本地仓库为`D:\M2REPO\org\apache\tomcat\maven\tomcat7-maven-plugin\2.2 `,则复制项目下的`tomcat7-maven-plugin-2.2.jar`替换掉原来的`tomcat7-maven-plugin-2.2.jar`

