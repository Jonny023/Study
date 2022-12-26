## 日期常用

### LocalDate

```java
public static Map<String, String> allDaysOfWeek(LocalDate localDate){
    Map<String, String> map = new HashMap<>();
    map.put("1", localDate.with(DayOfWeek.MONDAY).toString());
    map.put("2", localDate.with(DayOfWeek.TUESDAY).toString());
    map.put("3", localDate.with(DayOfWeek.WEDNESDAY).toString());
    map.put("4", localDate.with(DayOfWeek.THURSDAY).toString());
    map.put("5", localDate.with(DayOfWeek.FRIDAY).toString());
    map.put("6", localDate.with(DayOfWeek.SATURDAY).toString());
    map.put("7", localDate.with(DayOfWeek.SUNDAY).toString());
    return map;
}
```
