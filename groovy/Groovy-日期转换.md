# 日期转换工具

* 获取指定日期0-23点

```groovy
/**
 *  获取指定日期开始时间和结束时间
 * @param date 指定日期
 * @return
 */
static def dayOfRange(Date date) {
    Calendar calendar = new GregorianCalendar()
    calendar.setTime(date)
    calendar.add(Calendar.DAY_OF_MONTH,0)

    //一天的开始时间 yyyy:MM:dd 00:00:00
    calendar.set(Calendar.HOUR_OF_DAY,0)
    calendar.set(Calendar.MINUTE,0)
    calendar.set(Calendar.SECOND,0)
    calendar.set(Calendar.MILLISECOND,0)
    Date dayStart = DateUtils.round(calendar.getTime(), Calendar.SECOND)

    //一天的结束时间 yyyy:MM:dd 23:59:59
    calendar.set(Calendar.HOUR_OF_DAY,23)
    calendar.set(Calendar.MINUTE,59)
    calendar.set(Calendar.SECOND,59)
    calendar.set(Calendar.MILLISECOND,999)
    Date dayEnd = DateUtils.round(calendar.getTime(), Calendar.SECOND)

    return [start: dayStart, end: dayEnd]
}

/**
 *  获取指定时间起始和结束
 * @param start 开始时间
 * @param end 结束时间
 * @return
 */
static def beginAndEnd(Date start, Date end) {
    Calendar startCalendar = new GregorianCalendar()
    startCalendar.setTime(start)
    startCalendar.add(Calendar.DAY_OF_MONTH,0)

    //一天的开始时间 yyyy:MM:dd 00:00:00
    startCalendar.set(Calendar.HOUR_OF_DAY,0)
    startCalendar.set(Calendar.MINUTE,0)
    startCalendar.set(Calendar.SECOND,0)
    startCalendar.set(Calendar.MILLISECOND,0)
    Date dayStart = DateUtils.round(startCalendar.getTime(), Calendar.SECOND)

    //一天的结束时间 yyyy:MM:dd 23:59:59
    startCalendar.setTime(end)
    startCalendar.add(Calendar.DAY_OF_MONTH,0)
    startCalendar.set(Calendar.HOUR_OF_DAY,23)
    startCalendar.set(Calendar.MINUTE,59)
    startCalendar.set(Calendar.SECOND,58)
    startCalendar.set(Calendar.MILLISECOND,999)
    // 返回用DateUtils.round，不然会多一秒
    Date dayEnd = DateUtils.round(startCalendar.getTime(), Calendar.SECOND)

    return [start: dayStart, end: dayEnd]
}

/**
 *  获取指定日期月份天数
 * @param date
 * @return
 */
static int getDaysOfMonth(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
}
```
