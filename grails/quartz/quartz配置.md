build.gradle依赖：
```
//定时器
compile 'org.grails.plugins:quartz:2.0.0.M4'
```

创建一个job，执行命令：
```
create-job com.test.Test
```
修改TestJob：
```groovy
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
## 控制器中动态调用：
```groovy
def personPush() {
    println "xxxxx"
    TestJob.schedule(new Date(),[tip: "执行了定时器。。。"])
}
```

## 暂停定时任务

```groovy 
def jobManagerService

def index() {
    // 停止所有定时任务
    jobManagerService.pauseAll()
    render "Stop all Job."
}
```

## 恢复定时任务

```groovy
jobManagerService.resumeAll()
```

## `JobManagerService`服务中常用方法

```groovy
Map <String , List<JobDescriptor>> getAllJobs() {}

List<JobDescriptor> getJobs(String group) {}

def getRunningJobs() {}

def pauseJob(String group, String name) {}

def resumeJob(String group, String name) {}

def pauseTrigger(String group, String name) {}

def resumeTrigger(String group, String name) {}

def pauseTriggerGroup(String group) {}

def resumeTriggerGroup(String group) {}

def pauseJobGroup(String group) {}

def resumeJobGroup(String group) {}

def pauseAll() {}

def resumeAll() {}

def removeJob(String group, String name) {}

def unscheduleJob(String group, String name) {}

def interruptJob(String group, String name) {}

```
* 可自行查看`grails.plugins.quartz.JobManagerService`类


方法：
```groovy
MyJob.schedule(String cronExpression, Map params) // 创建cron触发器
MyJob.schedule(Long repeatInterval, Integer repeatCount, Map params) // 创建简单的触发器：以repeatInterval毫秒的延迟重复作业repeatCount + 1次
MyJob.schedule(Date scheduleDate, Map params) // 将一个作业执行安排到特定日期
MyJob.schedule(Trigger trigger) // 使用自定义触发器来安排作业的执行
MyJob.triggerNow(Map params) // 强制立即执行工作
```
