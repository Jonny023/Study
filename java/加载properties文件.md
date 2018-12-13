## 1、使用`class`变量的`getResourceAsStream()`方法
#### 注意：`getResourceAsStream()`方法的参数路径/包路径+properties文件名+.后缀（必须加/）

```java
InputStream inputStream = T.class.getResourceAsStream("/b.properties");
Properties prop = new Properties();
prop.load(inputStream);
String s = prop.getProperty("b");
```

## 2、使用`class.getClassLoader()`所得到的`java.lang.ClassLoader`的`getResourceAsStream()`方法 
#### 注意：`getResourceAsStream(name)`方法的参数必须是包路径+文件名+.后缀(不能加/)

```java
InputStream inputStream = T.class.getClassLoader().getResourceAsStream("b.properties");
//或者
//InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("file/a.properties");
Properties prop = new Properties();
prop.load(inputStream);
String s = prop.getProperty("b");
```

## 3、使用`java.util.ResourceBundle`类的`getBundle()`方法
#### 注意：注意：这个`getBundle()`方法的参数相对同目录路径，并去掉`.properties`后缀，否则将抛异常

```java
ResourceBundle resource = ResourceBundle.getBundle("b");
String s = resource.getString("b");
System.out.println("content: " + s);
```




