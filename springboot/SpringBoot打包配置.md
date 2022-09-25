# Springboot打包配置

* 如果要把依赖的jar打包到jar中，就不要配置skip
* `<skip>true</skip>`常用在子模块无任何依赖的场景

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <mainClass>com.example.demo.DemoApplication</mainClass>
                <!-- 只打包项目代码，不包含jar -->
                <!--<skip>true</skip>-->
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```


### 跳过测试类

```xml

<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <excludes>
                    <exclude>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                    </exclude>
                </excludes>
                <includeSystemScope>true</includeSystemScope>
                <!-- 如果没有该配置，devtools不会生效 -->
                <fork>true</fork>
                <layout>JAR</layout>
                <classifier>exec</classifier>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <!-- 可以把依赖的包都打包到生成的Jar包中 -->
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <configuration>
                <skip>true</skip>
            </configuration>
        </plugin>
    </plugins>
</build>
```
