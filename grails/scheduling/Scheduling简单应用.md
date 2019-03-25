# 定时任务

* 主函数类

```groovy

@EnableScheduling 
class Application extends GrailsAutoConfiguration { 
    static void main(String[] args) { 
     GrailsApp.run(Application, args) 
    } 
} 
```

* 方法

```groovy
@Scheduled(cron="00 00 * * *") 
def foo() { 
    //do something 
} 
```
