# spring依赖注入的三种方式

* 构造器注入
* 接口注入
* set方法注入

> 构造器注入

```java
public class Test1 {  
    private MessageInterface message;  
    @Autowired //构造器注入  
    private Test1(MessageInterface message) {  
        this.message = message;  
        //省略getter和setter  
    } 
  }
```

> 接口注入

```java
public class Test2 {  
    @Autowired //接口注入  
    private MessageInterface messageInterface;  
    //省略getter和setter  
}
```

> set方法注入

```java
public class Test3 {  
    private MessageInterface message;  
 
    @Autowired //setter方法注入  
    public void setMessage(MessageInterface message) {  
        this.message = message;  
    }  
    public String getMessage() {  
        return message;  
    }  
}
```
