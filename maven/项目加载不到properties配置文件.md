> 问题分析

* `maven`项目在`idea`中不会自动编译`properties`文件，需手动配置
* 通过`XxxUtils.class.getResourceAsStream("/com/test/abc.properties")`加载不到`abc.properties`，但是第二次调用方法可以加载

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
            </includes>
        </resource>
    </resources>
</build>
```
