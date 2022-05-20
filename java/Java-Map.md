# Map笔记

## 根据map的key值排序

```java
/**
 *  Map按key值升序
 * @param map
 * @return
 */
public static Map<String, Object> sortAsc(Map<String, Object> map) {
    Map<String, Object> sortedMap = map.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    return sortedMap;
}

/**
 *  Map按key值降序
 * @param map
 * @return
 */
public static Map<String, Object> sortDesc(Map<String, Object> map) {
    Map<String, Object> sortedMap = map.entrySet().stream()
            .sorted(Map.Entry.<String, Object>comparingByKey().reversed())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    return sortedMap;
}
```
