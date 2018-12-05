> for example

```java
package test;

public class Obj<T> {

    private T obj;

    public Obj(T obj) {
        this.obj = obj;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public static void main(String[] args) {
        Obj<String> obj = new Obj<>("张三");
        obj.setObj("李四");
        System.out.println(obj.getObj());

        Obj<Integer> obj2 = new Obj<>(20);
        System.out.println(obj2.getObj());

        Obj<Character> obj3 = new Obj<>('男');
        System.out.println(obj3.getObj());

    }
}

```
