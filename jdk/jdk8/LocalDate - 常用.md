## 日期常用

### LocalDate

#### 生成本周的日期数据

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

#### 计算年龄

> 严格按天计算

```java

LocalDate birth = LocalDate.of(1993, 10, 1);
LocalDate adult = birth.plusYears(18).minusDays(1);
//获取年龄
System.out.println(Period.between(birth, adult).getYears());
System.out.println(ChronoUnit.YEARS.between(birth, adult));
System.out.println(birth.until(adult).getYears());
```
