> 问题分析

* 项目编译后没有编译properties

```xml
<build>
    <finalName>egov</finalName>
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
            </includes>
        </resource>
    </resources>
</build>
```
