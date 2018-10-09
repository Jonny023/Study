### 使用class变量的getResourceAsStream()方法
#### 注意：getResourceAsStream()方法的参数路径/包路径+properties文件名+.后缀

```
InputStream inputStream = T.class.getResourceAsStream("/b.properties");
Properties prop = new Properties();
prop.load(inputStream);
String s = prop.getProperty("b");
```

### 使用class.getClassLoader()所得到的java.lang.ClassLoader的getResourceAsStream()方法 
#### 注意：getResourceAsStream(name)方法的参数必须是包路径+文件名+.后缀

```
InputStream inputStream = T.class.getClassLoader().getResourceAsStream("b.properties");
Properties prop = new Properties();
prop.load(inputStream);
String s = prop.getProperty("b");
```



