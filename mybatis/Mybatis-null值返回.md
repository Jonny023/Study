> mybatis返回map默认null值不返回

* config.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD SQL MAP Config 3.1//EN"
    "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
  <settings>
    <setting name="callSettersOnNulls" value="true"/>
  </settings>
</configuration>
```

* springboot配置

```properties
mybatis.configuration.call-setters-on-nulls=true
```
