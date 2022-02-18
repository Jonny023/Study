## logback配置

> logback-spring.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="false">

    <!--
        Logback会初始化两次。在Spring加载application*.yml之前初始化一次，在这之后获得了相关变量后又会在初始化一次。
        第一次初始化的时候，可能不能获取到${spring.application.name}。会将这个变量解析为“SPRING_APPLICATION_NAME_IS_UNDEFINED”，生成错误的日志路径。
        解决方案： 在java命令行中添加参数 -Dspring.application.name=Foo
     -->

    <springProperty scope="context" name="SPRING_APPLICATION_NAME" source="spring.application.name"/>

    <!-- TODO 找一个好看的颜色方案 -->
    <property name="CONSOLE_LOG_PATTERN"
              value="%d{yyyy-dd-MM HH:mm:ss.SSS} [%thread] %highlight(%-5level) %logger{24}:%line - %highlight(%msg) %n"/>

    <property name="FILE_LOG_PATTERN"
              value="%d{yyyy-dd-MM HH:mm:ss.SSS} [%thread] %-5level %logger{24} - %msg %n"/>

    <!-- 默认日志在项目所在的磁盘分区的根目录下 -->
    <property name="LOG_FILE"
              value=" ${LOG_FILE:-${LOG_PATH:-/var/log/test}/${SPRING_APPLICATION_NAME}/${SPRING_APPLICATION_NAME}}"/>

    <!-- 开发环境，只输出控制台-->
    <springProfile name="dev">
        <logger name="com.apache.ibatis" level="DEBUG"/>
        <logger name="java.sql.Connection" level="DEBUG"/>
        <logger name="java.sql.Statement" level="DEBUG"/>
        <logger name="java.sql.PreparedStatement" level="DEBUG"/>

        <logger name="org.hibernate.SQL" level="DEBUG"/>
        <logger name="org.hibernate.type" level="TRACE"/>
        <logger name="org.springframework.transaction" level="TRACE"/>
        <logger name="org.springframework.web" level="TRACE"/>

        <logger name="com.test" level="TRACE"/>

        <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
            <encoder>
                <pattern>${CONSOLE_LOG_PATTERN}</pattern>
                <charset>UTF-8</charset>
            </encoder>
        </appender>

        <root level="INFO">
            <appender-ref ref="CONSOLE"/>
        </root>
    </springProfile>

    <!-- 测试环境，输出文件-->
    <springProfile name="test">
        <logger name="com.apache.ibatis" level="DEBUG"/>
        <logger name="java.sql.Connection" level="DEBUG"/>
        <logger name="java.sql.Statement" level="DEBUG"/>
        <logger name="java.sql.PreparedStatement" level="DEBUG"/>

        <logger name="org.hibernate.SQL" level="DEBUG"/>
        <logger name="org.hibernate.type" level="TRACE"/>
        <logger name="org.springframework.transaction" level="TRACE"/>
        <logger name="org.springframework.web" level="TRACE"/>

        <logger name="com.test" level="TRACE"/>
        
        <appender name="FILE"
                  class="ch.qos.logback.core.rolling.RollingFileAppender">
            <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
                <pattern>${FILE_LOG_PATTERN}</pattern>
            </encoder>
            <file>${LOG_FILE}-info.log</file>
            <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
                <cleanHistoryOnStart>${LOG_FILE_CLEAN_HISTORY_ON_START:-false}</cleanHistoryOnStart>
                <fileNamePattern>${ROLLING_FILE_NAME_PATTERN:-${LOG_FILE}-info.%d{yyyy-MM-dd}.%i.gz}</fileNamePattern>
                <maxFileSize>${LOG_FILE_MAX_SIZE:-100MB}</maxFileSize>
                <maxHistory>${LOG_FILE_MAX_HISTORY:-3}</maxHistory>
                <totalSizeCap>${LOG_FILE_TOTAL_SIZE_CAP:-1GB}</totalSizeCap>
            </rollingPolicy>
        </appender>
        <root level="INFO">
            <appender-ref ref="FILE"/>
        </root>
    </springProfile>

    <!-- 生产环境，不同日志级别输出到不同文件-->
    <springProfile name="prd | prod | pro | product">
        <appender name="INFO-FILE"
                  class="ch.qos.logback.core.rolling.RollingFileAppender">
            <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
                <pattern>${FILE_LOG_PATTERN}</pattern>
            </encoder>
            <file>${LOG_FILE}-info.log</file>
            <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
                <cleanHistoryOnStart>${LOG_FILE_CLEAN_HISTORY_ON_START:-false}</cleanHistoryOnStart>
                <fileNamePattern>${ROLLING_FILE_NAME_PATTERN:-${LOG_FILE}-info.%d{yyyy-MM-dd}.%i.gz}</fileNamePattern>
                <maxFileSize>${LOG_FILE_MAX_SIZE:-100MB}</maxFileSize>
                <maxHistory>${LOG_FILE_MAX_HISTORY:-60}</maxHistory>
                <totalSizeCap>${LOG_FILE_TOTAL_SIZE_CAP:-20GB}</totalSizeCap>
            </rollingPolicy>
            <filter class="ch.qos.logback.classic.filter.LevelFilter">
                <level>INFO</level>
                <onMatch>ACCEPT</onMatch>
                <onMismatch>DENY</onMismatch>
            </filter>
        </appender>

        <appender name="WARN-FILE"
                  class="ch.qos.logback.core.rolling.RollingFileAppender">
            <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
                <pattern>${FILE_LOG_PATTERN}</pattern>
            </encoder>
            <file>${LOG_FILE}-warn.log</file>
            <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
                <cleanHistoryOnStart>${LOG_FILE_CLEAN_HISTORY_ON_START:-false}</cleanHistoryOnStart>
                <fileNamePattern>${ROLLING_FILE_NAME_PATTERN:-${LOG_FILE}-warn.%d{yyyy-MM-dd}.%i.gz}</fileNamePattern>
                <maxFileSize>${LOG_FILE_MAX_SIZE:-100MB}</maxFileSize>
                <maxHistory>${LOG_FILE_MAX_HISTORY:-60}</maxHistory>
                <totalSizeCap>${LOG_FILE_TOTAL_SIZE_CAP:-20GB}</totalSizeCap>
            </rollingPolicy>
            <filter class="ch.qos.logback.classic.filter.LevelFilter">
                <level>WARN</level>
                <onMatch>ACCEPT</onMatch>
                <onMismatch>DENY</onMismatch>
            </filter>
        </appender>


        <appender name="ERROR-FILE"
                  class="ch.qos.logback.core.rolling.RollingFileAppender">
            <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
                <pattern>${FILE_LOG_PATTERN}</pattern>
            </encoder>
            <file>${LOG_FILE}-error.log</file>
            <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
                <cleanHistoryOnStart>${LOG_FILE_CLEAN_HISTORY_ON_START:-false}</cleanHistoryOnStart>
                <fileNamePattern>${ROLLING_FILE_NAME_PATTERN:-${LOG_FILE}-error.%d{yyyy-MM-dd}.%i.gz}</fileNamePattern>
                <maxFileSize>${LOG_FILE_MAX_SIZE:-100MB}</maxFileSize>
                <maxHistory>${LOG_FILE_MAX_HISTORY:-60}</maxHistory>
                <totalSizeCap>${LOG_FILE_TOTAL_SIZE_CAP:-20GB}</totalSizeCap>
            </rollingPolicy>
            <filter class="ch.qos.logback.classic.filter.LevelFilter">
                <level>ERROR</level>
                <onMatch>ACCEPT</onMatch>
                <onMismatch>DENY</onMismatch>
            </filter>
        </appender>

        <root level="INFO">
            <appender-ref ref="INFO-FILE"/>
            <appender-ref ref="WARN-FILE"/>
            <appender-ref ref="ERROR-FILE"/>
        </root>
    </springProfile>

</configuration>
```

