# SpringBoot-aop导致service注入为null

#### Controller层的RequestMapping方法不能设置private(私有的)，这样会导致在动态代理过程中无法注入private对象。这里跟用@Autowire和@Resouce没有关系。就是被设置为私有方法导致的。

