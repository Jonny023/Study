# 占位字符替换工具
### String.format()
  * %s 字符型
  * %d 数字型
  * %f 浮点型
  
```java
String str = "%s或%s不能为空";
System.out.println(String.format(str,"用户名","密码")); 
// username或password不能为空

String num = "%d大于%d";
System.out.println(String.format(num,10,8));
// 10大于8

String f = "%f小于%f";
System.out.println(String.format(f,0f,0.1f));
// 0.000000小于0.100000
```

### MessageFormat.format()
  * {0}xxx{1}xxx  {0}占位符号

```java
String message = "{0}或{1}不能为空";
System.out.println(MessageFormat.format(message,"username","password"));
// 用户名或密码不能为空
```
