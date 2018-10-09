## 使用class变量的getResourceAsStream()方法
#### 注意：getResourceAsStream()方法的参数路径/包路径+properties文件名+.后缀（必须加/）

```
InputStream inputStream = T.class.getResourceAsStream("/b.properties");
Properties prop = new Properties();
prop.load(inputStream);
String s = prop.getProperty("b");
```

## 使用class.getClassLoader()所得到的java.lang.ClassLoader的getResourceAsStream()方法 
#### 注意：getResourceAsStream(name)方法的参数必须是包路径+文件名+.后缀(不能加/)

```
InputStream inputStream = T.class.getClassLoader().getResourceAsStream("b.properties");
//或者
//InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("file/a.properties");
Properties prop = new Properties();
prop.load(inputStream);
String s = prop.getProperty("b");
```

## 使用java.util.ResourceBundle类的getBundle()方法
#### 注意：注意：这个getBundle()方法的参数相对同目录路径，并去掉.properties后缀，否则将抛异常

```
ResourceBundle resource = ResourceBundle.getBundle("b");
String s = resource.getString("b");
System.out.println("content: " + s);
```




