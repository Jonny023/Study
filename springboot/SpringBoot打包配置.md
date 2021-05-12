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

