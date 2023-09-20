# BigDecimal转换

> 获取精度，小数位数

* 精度为整数部分和小数部分总长度（200.12精度为5）

```java
BigDecimal bigDecimal = BigDecimal.valueOf(200.12);

//5
System.out.println("精度：" + bigDecimal.precision());

//2
System.out.println("小数位数：" + bigDecimal.scale());
```

> 保留指定位数

```java
package com.util;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * 计算工具类
 */
public class CalcUtil {

    /**
     * @param value 值
     * @param scale 保留小数位数
     * @return
     */
    public static BigDecimal calc(Object value, int scale) {
        BigDecimal result = parse(value).setScale(scale, BigDecimal.ROUND_HALF_UP);
        return result;
    }

    /**
     *  保留指定位数小数，去除小数后面的0
     * @param value 值
     * @param scale 保留小数位数
     * @return
     */
    public static BigDecimal calcDisplayZero(Object value, int scale) {
        BigDecimal result = parse(value).setScale(scale, BigDecimal.ROUND_HALF_UP);;
        return result.stripTrailingZeros();
    }

    /**
     *  转换为BigDecimal
     * @param value
     * @param <T>
     * @return
     */
    private static <T> BigDecimal parse(T value) {
        BigDecimal result;
        if (value instanceof String) {
            if (!StringUtils.hasText((String) value)) {
                return BigDecimal.ZERO;
            }
            result = new BigDecimal((String) value);
        } else if (value instanceof Integer) {
            result = BigDecimal.valueOf((Integer) value);
        } else if (value instanceof Double) {
            result = BigDecimal.valueOf((Double) value);
        } else if (value instanceof Float) {
            result = BigDecimal.valueOf((Float) value);
        } else if (value instanceof Long) {
            result = BigDecimal.valueOf((Long) value);
        } else {
            result = BigDecimal.ZERO;
        }
        return result;
    }
}
```

## 去除小数末尾0，可能导致科学计数法，用toPlainString()解决

> stripTrailingZeros()去除0
> toPlainString() 输出为字符，防止科学计数

```java
stripTrailingZeros().toPlainString()
```

## 判断BigDecimal是否为整数

```java
/**
 * 判断BigDecimal是否为整数
 *
 * @param number 需判断的数
 * @return true 整数，false 非整数
 */
public static boolean isInteger(BigDecimal number) {
    return number.stripTrailingZeros().scale() <= 0 || number.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0;
}
```

## 获取BigDecimal小数位数（去掉末尾的0,为0不算小数位数）

```java
/**
 * 获取decimal小数位数
 *
 * @param number 需要获取的值
 * @return 返回0和大于0
 */
public static int scale(BigDecimal number) {
    return Math.max(number.stripTrailingZeros().scale(), 0);
}
```

## float转BigDecimal精度丢失问题

> 将float的7.45f向上取整保留1位小数导致精度问题，float需要先转换为String，再通过BigDecimal的构造器创建

```java
float f = 7.45f;
System.out.println(BigDecimal.valueOf(f).setScale(1, RoundingMode.HALF_UP));// 7.4
BigDecimal result = new BigDecimal(Float.toString(f));
System.out.println(result); // 7.45
System.out.println(result.setScale(1, RoundingMode.HALF_UP)); // 7.5
```
