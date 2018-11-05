> 静态代码块>构造代码块>构造函数>普通代码块　
### 静态代码块和构造代码块

```java
public class CodeBlock {
    static{
        System.out.println("静态代码块");
    }
    {
        System.out.println("构造代码块");
    }
}
```

### 静态代码块
  * 静态代码块中不能调用普通成员变量

### 构造代码块
  * 创建对象时执行，也就是new 
  * 每个构造函数之前执行

> 编译后代码

```java
public class CodeBlock {
         
    public CodeBlock(){
        System.out.println("构造代码块");
        System.out.println("无参构造函数");
    }
    public CodeBlock(String str){
        System.out.println("构造代码块");
        System.out.println("有参构造函数");
    }
}
```

### 普通代码块和构造代码块的区别
  * 构造代码块定义在类中
  * 普通代码块定义在方法体中
  * 普通代码块的执行顺序和书写顺序一致
  
  
### 注意
    * 构造代码块只有在执行构造函数时才会执行
