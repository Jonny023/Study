### Grails3在utils中注入bean

> 在`grails-app/conf/application.yml`中配置用户基本信息

```yml
user:
  pserson-name: 张三
  age: 20
  sex: 男
```

> 在`grails-app/utils`下面新建一个`JavaBean`类

### 注意

* 这里的包名最好跟`init/Application.groovy`下面的保持一致，比如：`Application.groovy`是在`com.demo`下面，那么这个`JavaBean`也必须是在`com.demo`下面，例：`com.demo.config`

```java
package com.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "user")
public class UserConfig {

    private String psersonName;

    private Integer age;

    private char sex;

    public String getPsersonName() {
        return psersonName;
    }

    public void setPsersonName(String psersonName) {
        this.psersonName = psersonName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "UserConfig{" +
                "psersonName='" + psersonName + '\'' +
                ", age='" + age + '\'' +
                ", sex=" + sex +
                '}';
    }
}
```

> 新建`UserUtils`

```java
package com.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

    @Autowired
    private UserConfig config;

    public String get() {
        System.out.println(config);
        return "用户姓名为："+config.getPsersonName();
    }

}
```

> 别忘了在主配置类`Application`上面加上`@ComponetScan`启用组件扫描

```java
@ComponentScan
class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }
}
```

> 通过`@Integration`启用集成测试

```groovy
package com.system

import grails.test.mixin.TestFor
import grails.test.mixin.integration.Integration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.IntegrationTest
import spock.lang.Specification
import websocket.UserUtils

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@Integration
class TestControllerSpec extends Specification {

    @Autowired
    private UserUtils util

    def setup() {
    }

    def cleanup() {
    }

    void "user test" () {
        expect: 'execlute'
            util.get()
    }

    void "test something"() {
        expect:"fix me"
            true == false
    }
}
```

### 控制台打印

```bash
Grails application running at http://localhost:8080 in environment: development
UserConfig{psersonName='张三', age='20', sex=男}
```

