## 使用final的原因

* 给方法加锁，保证线程安全
* 提高效率

## 姿势总结

* final修饰类不可被继承，里面的方法和成员变量会被隐式指定为final
* final修饰的基本数据类型初始化后不可修改，final修饰的引用数据类型引用不可修改

## final修饰不可修改案例
  
  * 基本数据类型不可修改  final int a = 0; a = 2;(这里a=2将会报错)
  * 引用数据类型不可修改  final User u = null; u = new User();(u=new User()将会报错)
  
## static和final容易

```
public class T {

    public final double a = Math.random();
    public static double b = Math.random();

    public static void main(String[] args) {
        System.out.println(new T().a);
        System.out.println(new T().a);
        System.out.println(T.b);
        System.out.println(T.b);
        System.out.println(T.b);
    }
}

//输出结果
0.5609375369958752
0.3924568182102002
0.7219868528454848
0.7219868528454848
0.7219868528454848
```
> static作用于成员变量用来表示只保存一份副本
> final的作用是用来保证变量不可变

### 注：匿名内部类中使用的外部局部变量只能是final变量
  
