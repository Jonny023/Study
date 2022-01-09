# maven远程debug

> idea配置：Remote JVM Debug

* pom.xml

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <executable>true</executable>
                <!--Xdebug 通知JVM工作在DEBUG模式下:-->
                <!--Xrunjdwp 通知JVM使用(Java debug wire protocol)运行调试环境。-->
                <!--transport 指定了调试数据的传送方式。dt_socket是指用SOCKET模式工 -->
                <!--另有dt_shmem指用共享内存方式。其中。dt_shmem只适用于Windows平台:-->
                <!--address 调试服务器的端口号，客户端用来连接服务器的瑞口号:-->
                <!--server=y/n y表示当前足调试服务端。n表示当前是调试客户端。-->
                <!--suspend表示启动时不中斯(如果启动时中断，一般用于调试启动不了的问题-->
                <jvmArguments>-Xdebug-Xrunjdwp:transport=dt_socket,address=5005,server=y,suspend=n</jvmArguments>
            </configuration>
        </plugin>
    </plugins>
</build>
```

