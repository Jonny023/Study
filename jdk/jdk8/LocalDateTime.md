# 日期转换

```java
// 年月日时分秒毫秒转LocalDateTime
DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
    // 解析date+time
    .appendPattern("yyyyMMddHHmmss")
    // 解析毫秒数
    .appendValue(ChronoField.MILLI_OF_SECOND, 3)
    .toFormatter();
LocalDateTime localDateTime = LocalDateTime.parse("20210816094419214", DATE_TIME_FORMATTER);
System.out.println(localDateTime);


//LocalDateTime转Date
ZoneId zoneId = ZoneId.systemDefault();
ZonedDateTime zdt = localDateTime.atZone(zoneId);
Date date = Date.from(zdt.toInstant());
System.out.println(date);

//localDateTime的0点和23点
localDateTime.with(LocalTime.MIN);
localDateTime.with(LocalTime.MAX);

```

