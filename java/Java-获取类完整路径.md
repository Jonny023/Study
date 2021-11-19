# 获取Class完整路径

```java
System.out.println(A.class.getName());
System.out.println(A.class.getCanonicalName());
System.out.println(A.class.getSimpleName());
System.out.println(A.class.getTypeName());

//运行结果
//com.geega.daas.bz.A
//com.geega.daas.bz.A
//A
//com.geega.daas.bz.A
    
    
    
//内部类
System.out.println(B.class.getName());
System.out.println(B.class.getCanonicalName());
System.out.println(B.class.getSimpleName());
System.out.println(B.class.getTypeName());

//运行结果
//com.geega.daas.bz.A$B
//com.geega.daas.bz.A.B
//B
//com.geega.daas.bz.A$B
```

