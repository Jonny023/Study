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

# 日期算术运算

> 有两种方法可以对日期对象执行算术运算，使用`plus`和`minus`方法以及使用`+`和`-`运算符。
> 比如说，日期后`10`天的日期对象。使用`plus`方法和`+`运算符将日期增加`10`天：

```groovy
def nowString = "2017-Oct-26 11:45:23 PM"
def nowDate = Date.parse("yyyy-MMM-dd hh:mm:ss a", nowString)
def addDate = nowDate.plus(10)  // 使用plus方法减去指定天数
def addDate2 = nowDate + 10     // 使用加号
```

> 日期前`30`天找出日期对象，我们可以使用`minus`方法或-运算符来减少日期：

```groovy
def subDate = nowDate.minus(30)  // 使用minus方法减去指定天数
def subDate2 = nowDate - 30      // 使用减号
```
