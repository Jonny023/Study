# 对象copy

```java
package cn.com.geely.util;

import org.springframework.beans.BeanUtils;

import java.util.function.Supplier;

public class Convertor {

    public static <T> T convert(Object vo, T t) {
        if (vo == null) {
            return null;
        }

        BeanUtils.copyProperties(vo, t);
        return t;
    }

    public static <T> T convert(Object vo, Supplier<T> target) {
        if (vo == null) {
            return null;
        }

        T t = target.get();
        BeanUtils.copyProperties(vo, t);
        return t;
    }

    public static <T> T convert(Object vo, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        if (vo == null) {
            return null;
        }

        T object = clazz.newInstance();
        BeanUtils.copyProperties(vo, object);
        return object;
    }

}

```
