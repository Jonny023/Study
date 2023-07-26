> pom.xml

No.1

```xml
<dependency>
    <groupId>local</groupId>
    <artifactId>ojdbc</artifactId>
    <version>11.2.0.4.0</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/src/main/webapp/WEB-INF/lib/ojdbc6.jar</systemPath>
</dependency>
```

No.2

```xml
<dependency>
    <groupId>css2parser</groupId>
    <artifactId>ss_css2</artifactId>
    <scope>system</scope>
    <systemPath>${basedir}/src/main/resources/libs/ss_css2.jar</systemPath>
</dependency>-->
```

No.3

> 有时候可能仓库里面没有你需要的依赖jar，我们可以在其他地方将需要的jar下载下来，然后在pom.xml中添加依赖，通过命令添加jar到本地仓库

```bash
mvn install:install-file -DgroupId=com.google.code -DartifactId=kaptcha -Dversion=2.3.2 -Dfile=d:\libs\kaptcha-2.3.jar -Dpackaging=jar -DgeneratePom=true
```
### 注解：${basedir}项目的更目录


## 打包
> systemPath引入的jar默认不会打包到lib目录下，需要配置一下

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-dependency-plugin</artifactId>
    <version>2.10</version>
    <executions>
        <execution>
            <id>copy-dependencies</id>
            <phase>compile</phase>
            <goals>
                <goal>copy-dependencies</goal>
            </goals>
            <configuration>
                <outputDirectory>${project.build.directory}/${project.build.finalName}/WEB-INF/lib</outputDirectory>
                <includeScope>system</includeScope>
            </configuration>
        </execution>
    </executions>
</plugin>
```

## springboot

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <includeSystemScope>true</includeSystemScope>
            </configuration>
        </plugin>
    </plugins>
</build>
```
