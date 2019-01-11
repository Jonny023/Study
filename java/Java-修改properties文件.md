> maven依赖

```xml
<dependency>
    <groupId>commons-configuration</groupId>
    <artifactId>commons-configuration</artifactId>
    <version>1.10</version>
</dependency>
```

> 核心代码

```java
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
 
public class UpdateConfig {
 
	public static void main(String[] args) throws ConfigurationException {
 
	
		PropertiesConfiguration config = new PropertiesConfiguration("/Documents/userConfig.properties");
		config.setProperty("userName", "admin");
		config.setProperty("password", "12345");
		
		config.save();
 
		System.out.println("config update.");
	}
}
```
