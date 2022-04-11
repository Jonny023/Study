#### SpringBoot配置类加载顺序

[参考地址](https://blog.csdn.net/zengjyxxz/article/details/106709457)

SpringBoot 自动配置三大注解不能作用于被启动类扫描的配置类中。

Spring Boot的自动配置均是通过spring.factories来指定的，它的优先级最低（执行时机是最晚的）；通过扫描进来的优先级是最高的。

Spring Boot的自动配置是通过@EnableAutoConfiguration注解驱动的，默认是开启状态。你也可以通过spring.boot.enableautoconfiguration = false来关闭它，回退到Spring Framework时代。


