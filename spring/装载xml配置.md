* 加载xml配置

```
//装载单个配置文件实例化ApplicationContext容器
ApplicationContext cxt = new ClassPathXmlApplicationContext("applicationContext.xml");

//装载多个配置文件实例化ApplicationContext容器
String[] configs = {"bean1.xml","bean2.xml","bean3.xml"};
ApplicationContext cxt = new ClassPathXmlApplicationContext(configs);
```
