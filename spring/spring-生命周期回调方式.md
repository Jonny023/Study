# Spring生命周期回调的几种方式

* `xml`注入`<bean id="userService" clas="com.service.UserServiceImpl" initMethod="init"/>`

* 实现`InitializingBean`接口

* 类中添加`@PostConstruct`注解方法

