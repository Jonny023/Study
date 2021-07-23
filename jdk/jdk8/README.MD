# 1.传统排序

> 数值型字符排序(传统方式)

```java
public static void main(String[] args) {
    List list = Arrays.asList("2", "1", "3","200","100","-100","0");

    Comparator<String> comparator = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return Integer.valueOf(o1) - Integer.valueOf(o2);
        }
    };

    list.sort(comparator);
    System.out.println(list);
    // 输出[-100, 0, 1, 2, 3, 100, 200]
}
```

# 2.lambda排序

> Lambda表达式

```java
public static void main(String[] args) {
    List list = Arrays.asList("2", "1", "3","20","10000","0");

    // 升序
    // list.sort((o1, o2) -> Integer.valueOf(String.valueOf(o1)).compareTo(Integer.valueOf(String.valueOf(o2))));
    // list.sort((o1, o2) -> Integer.valueOf(String.valueOf(o1)) - Integer.valueOf(String.valueOf(o2)));

    // 不能对负数排序
    list.sort(Comparator.comparing(String::hashCode));

    // 降序
    // list.sort((o1, o2) -> Integer.valueOf(String.valueOf(o2)).compareTo(Integer.valueOf(String.valueOf(o1))));
    
    // 可以包含负整数
    List list2 = Arrays.asList("2", "1", "3","20","10000","500","-1","800");
    list2.sort(Comparator.<String,Integer>comparing(Integer::valueOf).reversed());
    System.out.println(list2);

    System.out.println(list);
}
```

# 3.join用法

> join前后加字符

```java
String[] strs = new String[]{ "a", "b", "c"};
Stream<String> strs1 = Stream.of(strs);
// 双引号，结果："a","b","c"
String collect = strs1.collect(Collectors.joining("\",\"", "\"", "\""));
System.out.println(collect);

// 单引号，结果：'a','b','c'
String collect1 = Stream.of(strs).collect(Collectors.joining("\','", "'", "'"));
```

# 4.JSONArray数据筛选求和

> 求时长的和

```java
@Test
public void count() {
    String json = "[\n" +
            "  [\n" +
            "    {\n" +
            "      \"label\": \"时长(天)\",\n" +
            "      \"required\": true,\n" +
            "      \"name\": \"DURATION\",\n" +
            "      \"type\": \"number\",\n" +
            "      \"value\": \"0.5\",\n" +
            "      \"dateFormatType\": 0,\n" +
            "      \"editable\": false\n" +
            "    },\n" +
            "    {\n" +
            "      \"label\": \"同行人员\",\n" +
            "      \"required\": false,\n" +
            "      \"name\": \"COLLEAGUES\",\n" +
            "      \"type\": \"text\",\n" +
            "      \"editable\": false\n" +
            "    }\n" +
            "  ],\n" +
            "  [\n" +
            "    {\n" +
            "      \"label\": \"时长(天)\",\n" +
            "      \"required\": true,\n" +
            "      \"name\": \"DURATION\",\n" +
            "      \"type\": \"number\",\n" +
            "      \"value\": \"0.9\",\n" +
            "      \"dateFormatType\": 0,\n" +
            "      \"editable\": false\n" +
            "    },\n" +
            "    {\n" +
            "      \"label\": \"同行人员\",\n" +
            "      \"required\": false,\n" +
            "      \"name\": \"COLLEAGUES\",\n" +
            "      \"type\": \"text\",\n" +
            "      \"editable\": false\n" +
            "    }\n" +
            "  ]\n" +
            "]\n" +
            "\n";

    JSONArray jsonArray = JSON.parseArray(json);
    Double sum = jsonArray.stream().map(obj -> ((JSONArray) obj).stream().filter(arr -> {
        return "DURATION".equals(((JSONObject) arr).getString("name"));
    }).mapToDouble(arr -> Double.parseDouble(((JSONObject) arr).getString("value")))).collect(Collectors.toList()).stream().flatMapToDouble(d -> d).sum();
    System.out.println(sum);
}
```

# 5.String转Set

```java
List<Map<String, Object>> list= new ArrayList<>();

Map<String, Object> map1 = new HashMap<>();
map1.put("id", 1);
map1.put("managerName", "1,1,2");

Map map2 = new HashMap<>();
map2.put("id", 2);
map2.put("managerName", "2,2,2,3");

list.add(map1);
list.add(map2);

Set<String> managerName = list.stream().map(map -> (String) map.get("managerName")).map(s -> s.split(",")).flatMap(arr -> Arrays.stream(arr)).collect(Collectors.toSet());
System.out.println(managerName);
```

# 6.集合超过指定长度进行分组【切割】

```java
//方式1
package com.whalebest.common.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Jonny
 * @description 集合分批（分割）
 * @date 2020/8/28 9:42
 */
public class ListBatchUtil {

    /**
     * 计算切分次数
     * @param size 集合大小
     * @param max 每max个分为一组
     * @return
     */
    private static Integer countStep(int size, int max) {
        return (size + max - 1) / max;
    }

    /**
     *  集合超过max个，每max个分为一组，如：超过2个，每2个分为一组
     *  list:[1,2,3,4,5] -> [[1,2], [3,4], [5]]
     * @param list
     * @param max 每max个分为一组
     * @return
     */
    public static <T> List<List<T>> group(List<T> list, int max) {
        int limit = countStep(list.size(), max);
        List<List<T>> splitList = Stream.iterate(0, n -> n + 1).limit(limit).parallel().map(a -> list.stream().skip(a * max).limit(max).parallel().collect(Collectors.toList())).collect(Collectors.toList());
        return splitList;
    }
}


//方式2 借助guava工具
List<List<User>> parts = Lists.partition(users, 50);
```

# 7.List排序

## 7.1 按日期排序

```java
List<DailyDataChartVo> list = list.stream()
        .sorted(Comparator.comparing(DailyDataChartVo::getDate))
        .collect(Collectors.toList());
```

## 7.2 按日期排序后，逆序

```java
List<DailyDataChartVo> list = list.stream()
        .sorted(Comparator.comparing(DailyDataChartVo::getDate).reversed())
        .collect(Collectors.toList());
```

## 7.3 按日期排序后，再按金额排序

```java
List<DailyDataChartVo> list = list.stream()
        .sorted(Comparator.comparing(DailyDataChartVo::getDate)
                .thenComparing(DailyDataChartVo::getRevenue))
        .collect(Collectors.toList());
```

## 7.4 按金额排序，排序时过滤Null值（如果排序的字段为null，NPE）

```java
List<DailyDataChartVo> list = list.stream()
        .filter(c -> Objects.nonNull(c.getRevenue()))
        .sorted(Comparator.comparing(DailyDataChartVo::getRevenue))
        .collect(Collectors.toList());
```

## 7.5 按金额排序，Null值排在最前面

java

```java
List<DailyDataChartVo> list = list.stream()
        .sorted(Comparator.comparing(DailyDataChartVo::getRevenue,
                Comparator.nullsFirst(BigDecimal::compareTo)))
        .collect(Collectors.toList());
//注意Comparator.nullsFirst的方法引用中，比较的字段是BigDecimal类型的，如果前后类型不一致，会报错：Non-static method cannot be referenced from a static context
```

## 7.6 按金额排序，Null值排在最后面

java

```java
List<DailyDataChartVo> list = list.stream()
        .sorted(Comparator.comparing(DailyDataChartVo::getRevenue,
                Comparator.nullsLast(BigDecimal::compareTo)))
        .collect(Collectors.toList());
```

# 8.Map排序

## 8.1 按key排序

```java
Map<LocalDate, BigDecimal> map = map.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByKey())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (c1, c2) -> c1, LinkedHashMap::new));
```

> 将map转换成流，在流中对元素进行排序，排序后，再用LinkedHashMap收集来保留顺序

```java
public static <K extends Comparable<? super K>, V> Comparator<Map.Entry<K,V>> comparingByKey() {
    return (Comparator<Map.Entry<K, V>> & Serializable)
        (c1, c2) -> c1.getKey().compareTo(c2.getKey());
}
```

> Map.Entry.comparingByKey()：对任意的c1, c2进行比较，然后将结果强制转换成一个可序列化的Comparator<Map.Entry<K, V>>
> [Collectors.toMap()基础](https://www.cnblogs.com/milete/p/14070775.html)

## 8.2 按key排序后，逆序

```java
Map<LocalDate, BigDecimal> map = map.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (c1, c2) -> c1, LinkedHashMap::new));
```

## 8.3 按value排序

```java
Map<LocalDate, BigDecimal> map = map.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (c1, c2) -> c1, LinkedHashMap::new));
```