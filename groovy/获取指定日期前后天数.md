> 用法

```groovy
package com.atgenee

use (groovy.time.TimeCategory) {
    //直接用数字的写法
    println 1.minute.from.now //一分钟以后
    println 30.days.ago.format("yyyy-MM-dd HH:mm:ss")   //30天前的时间

    // 还可以与日期型的混用
    def someDate = new Date()
    println ((someDate - 3.months).format("yyyy-MM-dd HH:mm:ss")) //三个月前的时间
    println ((3.months.from.now).format("yyyy-MM-dd HH:mm:ss")) //三个月后的时间
}

println "=========万恶分割线=========="

// java获取3个月前
Calendar calendar = Calendar.getInstance()
calendar.add(Calendar.MONTH, -3)
System.out.println(calendar.getTime().format("yyyy-MM-dd HH:mm:ss"))

// 3个月后
Calendar calendar1 = Calendar.getInstance()
calendar1.add(Calendar.MONTH,3)
System.out.println(calendar1.getTime().format("yyyy-MM-dd HH:mm:ss"))

```
