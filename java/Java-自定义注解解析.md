# Java自定义注解解析

```java
package org.example;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Main {
    
    public static void main(String[] args) throws IllegalAccessException {

        //获取属性上的注解
        User user = new User(20L, "zhangsan");
        Field[] fields = user.getClass().getDeclaredFields();
        Valid valid = null;
        for (Field field : fields) {
            field.setAccessible(true);
            System.out.printf("属性名：" + field.getName() + ", 属性值：" + field.get(user) + "\n");

            //标注有Valid注解的属性
            if (field.isAnnotationPresent(Valid.class)) {
                valid = field.getAnnotation(Valid.class);
                System.out.println(valid.required());
                System.out.println(valid.max());
            }
        }

        //获取类上的注解
        Valid annotation = User.class.getAnnotation(Valid.class);
        System.out.println(annotation.max());

        //获取方法上的注解
        Method[] methods = User.class.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Valid.class)) {
                valid = method.getAnnotation(Valid.class);
                System.out.printf("方法名：" + method.getName() + ", required：" + valid.required() + ", max:" + valid.max() + "\n");
            }
        }
    }

}
```
