> 使用方法,`properties`放到resources目录下

```properties
PropertiesLoaderUtils.loadProperties(new ClassPathResource("jdbc.properties"))

// 获取resources目录下的文件或者package下面的文件
InputStream inputStream = Test.class.getResourceAsStream("/application.yml");
InputStream inputStream1 = Test.class.getResourceAsStream("/com/demo/application.yml");
```
