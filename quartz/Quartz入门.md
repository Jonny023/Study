# Quartz入门

[视频地址](https://www.bilibili.com/video/BV19t41127de?p=11&spm_id_from=pageDriver)

# 一、核心概念

* 任务`Job`
  
* 每个`Job`必须实现`org.quartz.Job`接口，且只需实现接口定义的`execute`方法
  
* 触发器`Trigger`
  * `SimpleTrigger`
  * `CronTrigger`

* 调度器`Scheduler`
  
  * 将任务`Job`和触发器`Trigger`整合起来，基于`Trigger`设定的时间执行`Job`
  
  

# 二、结构



![](img/结构.png)

