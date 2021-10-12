# orika 复制对象

```java
package com.utils;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Java拷贝工具类
 */
public class BeanCopyUtils {

    /**
     * 引入orika。线程安全，所以定义为静态的单例对象
     */
    private static final MapperFactory MAPPER_FACTORY = new DefaultMapperFactory.Builder().build();

    /**
     * 将对象属性拷贝到目标类型的同名属性字段中
     *
     * @param <T>
     * @param source
     * @param targetClazz
     * @return
     */
    public static <T> T copyProperties(Object source, Class<T> targetClazz) {

        T target;
        try {
            target = MAPPER_FACTORY.getMapperFacade().map(source, targetClazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return target;
    }

    /**
     * 将对象属性拷贝到目标类型的同名属性字段中
     *
     * @param source
     * @param target
     * @return
     */
    public static <T> T copyProperties(Object source, T target) {
        MAPPER_FACTORY.getMapperFacade().map(source, target);
        return target;
    }

    /**
     * 将对象属性拷贝给目标对象
     *
     * @param source
     * @param context
     * @param target
     * @return
     */
    public static <T> T copyProperties(Object source, ServiceCallContext context, T target) {
        MAPPER_FACTORY.getMapperFacade().map(source, target);
        if (BaseEntity.class.isAssignableFrom(target.getClass())) {
            BeanCopyUtils.copyProperties((BaseEntity) target, context);
        }

        return target;
    }

    /**
     * 拷贝通用属性
     *
     * @param dto
     * @param context
     */
    public static void copyProperties(BaseEntity dto, ServiceCallContext context) {
        if (context == null || dto == null) {
            return;
        }

        dto.setCreateTime(context.getCallTime());
        dto.setCreateUserId(context.getUser().getUserId());

        dto.setLastUpdateTime(context.getCallTime());
        dto.setLastUpdateUserId(context.getUser().getUserId());
    }

    /**
     * 将list的对象拷贝到目标类型对象中
     *
     * @param list
     * @param clazz
     * @return
     */
    public static <V, E> List<E> copy(Collection<V> list, Class<E> clazz) {
        List<E> result = new ArrayList<>(12);

        if (!CollectionUtils.isEmpty(list)) {
            for (V source : list) {
                E target = null;
                try {
                    target = (E) clazz.newInstance();
                    MAPPER_FACTORY.getMapperFacade().map(source, target);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                result.add(target);
            }
        }

        return result;
    }
}
```

