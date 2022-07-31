# SpringSecurity 加密

```java
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
String encode = encoder.encode("123456");
System.out.println(encode);
//判断密码是否正确
boolean matches = encoder.matches("123456", encode);
System.out.println(matches);
```

