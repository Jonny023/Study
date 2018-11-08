## `SpringBoot`底层使用的是`slf4j+logback`来进行日志记录

* 把其他`common-logging`、`log4j`、`java.util.logging`转换为`slf4j`

> 底层依赖关系

![](https://user-gold-cdn.xitu.io/2018/11/9/166f434f9b8961b3?w=958&h=388&f=jpeg&s=39667)

> 关系如何转化

![](https://user-gold-cdn.xitu.io/2018/11/9/166f434f9b7780e6?w=782&h=469&f=png&s=180692)

> 底层通过偷梁换柱的方法，用jcl、jul、log4j中间转换包进行转化

![](https://user-gold-cdn.xitu.io/2018/11/9/166f434f9c037776?w=1171&h=295&f=png&s=230792)



#### 如果要引入其他框架，必须将其中默认日志依赖剔除

> `SpringBoot`从maven依赖中剔除`springframework:spring-core`中的`common-logging`

```xml
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-core</artifactId>
  <version>4.3.20.RELEASE</version>
  <exclusions>
    <exclusion>
      <artifactId>commons-logging</artifactId>
      <groupId>commons-logging</groupId>
    </exclusion>
  </exclusions>
</dependency>
```



+++



### `SpringBoot`默认日志级别为`INFO`级别

* 日志优先级从小到大顺序为：
  + `trace<debug<info<warn<error`

```java
package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    Logger log = LoggerFactory.getLogger(getClass());

    @Test
    public void contextLoads() {
        log.trace("trace日志");
        log.debug("debug日志");
        log.info("info日志");
        log.warn("warn日志");
        log.error("error日志");
    }

}
```

* 启动运行，控制台打印只打印了`info`及以上级别

```bash
2018-11-09 00:13:36.899  INFO 8156 --- [main] com.example.demo.DemoApplicationTests    : info日志
2018-11-09 00:13:36.900  WARN 8156 --- [main] com.example.demo.DemoApplicationTests    : warn日志
2018-11-09 00:13:36.900 ERROR 8156 --- [main] com.example.demo.DemoApplicationTests    : error日志
```

> 日志基础配置

```properties
# 指定日志输入级别
logging.level.com.example.demo=trace 

# 指定日志输出位置和日志文件名
logging.file=./log/log.txt

# 指定日志输出路径,若file和path同时配置,则file生效
# 此配置默认生成文件为spring.log
#logging.path=./log

# 控制台日志输出格式
# -5表示从左显示5个字符宽度
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss.SSS} %highlight(%-5level) %boldYellow(%thread) | %boldGreen(%logger) | %msg%n

# 文件中输出的格式
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss.SSS} = [%thread] = %-5level = %logger{50} - %msg%n
```

