> 自定义一个异常类

```java
package demo;

public class MyException extends RuntimeException {

    // 异常信息
    private String errorMsg;

    private Throwable e;

    public MyException() {
        super();
    }

    public MyException(String errorMsg, Throwable e) {
        super(errorMsg,e);
        this.errorMsg = errorMsg;
        this.e = e;
    }

    public MyException(Throwable e) {
        super(e);
        this.e = e;
    }

    public MyException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Throwable getE() {
        return e;
    }

    public void setE(Throwable e) {
        this.e = e;
    }
}

```

> 如何调用？

```java
public static void main(String[] args) {
		
  String username = null;

  if(username==null) {
    throw new MyException("用户名不能为空");
  }

}
```

> `try catch`

```java
public static void main(String[] args) {

	String username = "admin";
	try {
		check(username);
	} catch (MyException e) {
		e.printStackTrace();
		System.out.println(e.getErrorMsg());
	}

}

private static boolean check(String username) throws MyException {
	if(username=="admin") {
		throw new MyException(username+"已经存在");
	}
	return true;
}
```
