# ip2region离线IP地址定位

[ip2region](https://github.com/lionsoul2014/ip2region)

* maven依赖

```xml
<dependency>
    <groupId>org.lionsoul</groupId>
    <artifactId>ip2region</artifactId>
    <version>1.7.2</version>
</dependency>
```

* 在application.properties中配置db文件位置

```
spring.ip2region.path=c:/ip2region.db
```

* ip2region工具类

```java
package com.common;

import com.common.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.*;
import org.springframework.core.env.Environment;

import java.io.FileNotFoundException;

@Slf4j
public class Ip2RegionUtil {

    private static DbSearcher searcher = null;

    static {
        try {
            String dbPath = SpringContextUtil.getBean(Environment.class).getProperty("spring.ip2region.path");
            DbConfig config = new DbConfig();
            searcher = new DbSearcher(config, dbPath);
        } catch (DbMakerConfigException e) {
            log.warn("ip2region config init exception: {}", e);
        } catch (FileNotFoundException e) {
            log.warn("ip2region file not found: {}", e);
        }
    }

    public static String[] getRegionInfo(String ip) {

        try {
            if (!Util.isIpAddress(ip)) {
                throw new RuntimeException("Error: Invalid ipAddress");
            }
            DataBlock dataBlock = searcher.memorySearch(ip);
            if (dataBlock != null) {
                String[] city = dataBlock.getRegion().split("\\|");
                String[] data = new String[3];
                data[0] = city[0];
                data[1] = city[2];
                data[2] = city[3];
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("ip解析错误, message: {}", e);
        }
        return new String[]{"", "", ""};
    }

}
```

* 用到的spring工具类

```java
package com.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author: Jonny
 * @description: Spring工具类, 获取Spring上下文对象
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringContextUtil.applicationContext == null) {
            SpringContextUtil.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }
}
```

