# Maven多环境打包

## 1.配置文件

```yaml
spring:
  profiles:
    active: @environment@
```

## 2.过滤打包需要的文件

```xml
<profiles>
    <profile>
        <id>dev</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <environment>dev</environment>
        </properties>
    </profile>
    <profile>
        <id>prod</id>
        <properties>
            <environment>prod</environment>
        </properties>
    </profile>
    <profile>
        <id>test</id>
        <properties>
            <environment>test</environment>
        </properties>
    </profile>
</profiles>

<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <mainClass>app.Application</mainClass>
            </configuration>
            <executions>
                <execution>
                    <id>repackage</id>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
    <filters>
        <filter>src/main/resources/application-${environment}.yml</filter>
    </filters>
    <resources>
        <resource>
            <directory>src/main/resources</directory>
            <includes>
                <include>application.yml</include>
                <include>application-${environment}.yml</include>
                <include>mapper/*.xml</include>
            </includes>
            <filtering>true</filtering>
        </resource>
    </resources>
</build>
```

> 网上都是值配置`filters`和`<filtering>true</filtering>`，经测试如果只配置这两个配置项，打包配置不生效，需要配合include类使用

```xml
<filters>
    <filter>src/main/resources/application-${environment}.yml</filter>
</filters>
<resources>
    <resource>
        <directory>src/main/resources</directory>
        <includes>
            <include>application-${environment}.yml</include>
        </includes>
        <filtering>true</filtering>
    </resource>
</resources>
```

## 3.打包指定环境

```shell
mvn clean package -Ptest -DskipTests=true
```

## 启动报错，配置文件里面的变量未被替换

> 原因1 可能是build编译配置里面有多个配置，如src/main/java把src/main/resources覆盖了，src/main/java下如果没有如yml文件就不要配置includes

```sh
mvn clean compile -DskipTests=true resources:resources
```

> 原因2 pom是多module模式，子模块需要指定`<packaging>jar</packaging>`，只有父pom才能用`<packaging>pom</packaging>`，需要注意下
