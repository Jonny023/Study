SpringBoot事件监听

* 启动成功

```java
package com.example.springbootdemo.event;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 *  启动成功监听
 */
@Component
public class StartupEventLister implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("======startup success======");
    }
}
```

* 启动失败

```java

```
