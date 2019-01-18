build.gradle依赖：
```
//定时器
compile 'org.grails.plugins:quartz:2.0.0.M4'
```

创建一个job，执行命令：
```
create-job com.test.Test
```groovy
修改TestJob：
```
package com.test

class TestJob {

static triggers = {}

    def execute(context) {
        // execute job
        println "执行..."
        println context.mergedJobDataMap.get("tip")
    }
}
```
控制器中动态调用：
```groovy
def personPush() {
    println "xxxxx"
    TestJob.schedule(new Date(),[tip: "执行了定时器。。。"])
}
```

方法：
```groovy
MyJob.schedule(String cronExpression, Map params) // 创建cron触发器
MyJob.schedule(Long repeatInterval, Integer repeatCount, Map params) // 创建简单的触发器：以repeatInterval毫秒的延迟重复作业repeatCount + 1次
MyJob.schedule(Date scheduleDate, Map params) // 将一个作业执行安排到特定日期
MyJob.schedule(Trigger trigger) // 使用自定义触发器来安排作业的执行
MyJob.triggerNow(Map params) // 强制立即执行工作
```
