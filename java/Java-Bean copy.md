# 对象copy

* 函数式接口
```java
package com.common.util;

@FunctionalInterface
public interface BeanUtilsCallBack<S, T> {

    void callBack(S t, T s);
}

```

* util

```java
package com.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.springframework.beans.BeanUtils.copyProperties;

public class Convertor {

    public static <T> T convert(Object vo, T t) {
        if (vo == null) {
            return null;
        }
        copyProperties(vo, t);
        return t;
    }

    public static <T> T convert(Object vo, Supplier<T> target) {
        if (vo == null) {
            return null;
        }
        T t = target.get();
        copyProperties(vo, t);
        return t;
    }

    public static <T> T convert(Object vo, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        if (vo == null) {
            return null;
        }
        T object = clazz.newInstance();
        copyProperties(vo, object);
        return object;
    }

    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        return copyListProperties(sources, target, null);
    }

    /**
     * 使用场景：Entity、Bo、Vo层数据的复制，因为BeanUtils.copyProperties只能给目标对象的属性赋值，却不能在List集合下循环赋值，因此添加该方法
     * 如：List<AdminEntity> 赋值到 List<AdminVo> ，List<AdminVo>中的 AdminVo 属性都会被赋予到值
     * S: 数据源类 ，T: 目标类::new(eg: AdminVo::new)
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, BeanUtilsCallBack<S, T> callBack) {
        List<T> list = new ArrayList<>(sources.size());

        T t = null;
        for (S source : sources) {
            t = target.get();
            copyProperties(source, t);
            if (callBack != null) {
                callBack.callBack(source, t);
            }
            list.add(t);
        }
        return list;
    }

}

```
