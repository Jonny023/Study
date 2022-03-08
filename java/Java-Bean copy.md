# 对象copy

### 方式1【速度快】

```java
package com.example.springbootsentinel.utils;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * 对象拷贝工具类
 * 属性类型必须一致，可以通过自定义转换处理是否转换
 */
public class Convertor {

    /**
     * BeanCopier的缓存
     */
    static final ConcurrentHashMap<String, BeanCopier> CACHE = new ConcurrentHashMap<>();

    private static BeanCopier getInstance(Object source, Object target) {
        String key = genKey(source.getClass(), target.getClass());
        BeanCopier beanCopier;
        if (CACHE.containsKey(key)) {
            beanCopier = CACHE.get(key);
        } else {
            beanCopier = BeanCopier.create(source.getClass(), target.getClass(), false);
            CACHE.put(key, beanCopier);
        }
        return beanCopier;
    }

    /**
     * 对象拷贝
     *
     * @param source       源对象
     * @param destSupplier 目标对象：User::new
     * @return
     */
    private static <T1, T2> T2 copy(T1 source, Supplier<T2> destSupplier) {
        if (source == null || destSupplier == null) {
            return null;
        }
        T2 target = destSupplier.get();
        BeanCopier copier = getInstance(source, target);
        copier.copy(source, target, null);
        return target;
    }

    private static <T2, T1> List<T2> copyList(List<T1> source, Class<T2> targetClass) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }
        List<T2> resultList = new ArrayList<>();
        try {
            T2 target = targetClass.newInstance();
            for (T1 t1 : source) {
                resultList.add(copy(t1, () -> target));
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    /**
     * 生成key
     *
     * @param srcClazz 源文件的class
     * @param tgtClazz 目标文件的class
     * @return string
     */
    private static String genKey(Class<?> srcClazz, Class<?> tgtClazz) {
        return srcClazz.getName() + tgtClazz.getName();
    }
}
```

### 方式2

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
