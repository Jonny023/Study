## SpringBoot Kafka依赖冲突

[kafka和springboot依赖关系](https://spring.io/projects/spring-kafka/)

> 在项目中集成`actuator`优雅停机启动报错，内容如下

```java
Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'kafkaTemplate' defined in class path resource [org/springframework/boot/autoconfigure/kafka/KafkaAutoConfiguration.class]: Unsatisfied dependency expressed through method 'kafkaTemplate' parameter 0; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'kafkaProducerFactory' defined in class path resource [org/springframework/boot/autoconfigure/kafka/KafkaAutoConfiguration.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.springframework.kafka.core.ProducerFactory]: Factory method 'kafkaProducerFactory' threw exception; nested exception is java.lang.NoClassDefFoundError: org/springframework/kafka/core/MicrometerProducerListener
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:800)
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:541)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1336)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1179)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:571)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:531)
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:335)
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:333)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:213)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.resolveBeanByName(AbstractAutowireCapableBeanFactory.java:468)
	at org.springframework.context.annotation.CommonAnnotationBeanPostProcessor.autowireResource(CommonAnnotationBeanPostProcessor.java:527)
	at org.springframework.context.annotation.CommonAnnotationBeanPostProcessor.getResource(CommonAnnotationBeanPostProcessor.java:497)
	at org.springframework.context.annotation.CommonAnnotationBeanPostProcessor$ResourceElement.getResourceToInject(CommonAnnotationBeanPostProcessor.java:650)
	at org.springframework.beans.factory.annotation.InjectionMetadata$InjectedElement.inject(InjectionMetadata.java:228)
	at org.springframework.beans.factory.annotation.InjectionMetadata.inject(InjectionMetadata.java:119)
	at org.springframework.context.annotation.CommonAnnotationBeanPostProcessor.postProcessProperties(CommonAnnotationBeanPostProcessor.java:318)
	... 62 common frames omitted
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'kafkaProducerFactory' defined in class path resource [org/springframework/boot/autoconfigure/kafka/KafkaAutoConfiguration.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.springframework.kafka.core.ProducerFactory]: Factory method 'kafkaProducerFactory' threw exception; nested exception is java.lang.NoClassDefFoundError: org/springframework/kafka/core/MicrometerProducerListener
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:658)
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:638)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1336)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1179)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:571)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:531)
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:335)
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:333)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:208)
	at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:276)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1380)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1300)
	at org.springframework.beans.factory.support.ConstructorResolver.resolveAutowiredArgument(ConstructorResolver.java:887)
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:791)
	... 78 common frames omitted
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.springframework.kafka.core.ProducerFactory]: Factory method 'kafkaProducerFactory' threw exception; nested exception is java.lang.NoClassDefFoundError: org/springframework/kafka/core/MicrometerProducerListener
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:185)
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:653)
	... 92 common frames omitted
Caused by: java.lang.NoClassDefFoundError: org/springframework/kafka/core/MicrometerProducerListener
	at org.springframework.boot.actuate.autoconfigure.metrics.KafkaMetricsAutoConfiguration.addListener(KafkaMetricsAutoConfiguration.java:71)
	at org.springframework.boot.actuate.autoconfigure.metrics.KafkaMetricsAutoConfiguration.lambda$kafkaProducerMetrics$0(KafkaMetricsAutoConfiguration.java:58)
	at org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration.lambda$kafkaProducerFactory$1(KafkaAutoConfiguration.java:102)
	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:184)
	at java.util.ArrayList.forEach(ArrayList.java:1257)
	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:390)
	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:482)
	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:471)
	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:151)
	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:174)
	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:418)
	at org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration.kafkaProducerFactory(KafkaAutoConfiguration.java:102)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:154)
	... 93 common frames omitted
```

> 官网版本对照

| Spring for Apache Kafka Version | Spring Integration for Apache Kafka Version | `kafka-clients`         | Spring Boot             |
| ------------------------------- | ------------------------------------------- | ----------------------- | ----------------------- |
| 2.7.0                           | 5.4.x                                       | 2.7.0 or 2.8.0          | 2.4.x or 2.5.x          |
| 2.6.x                           | 5.3.x or 5.4.x                              | 2.6.0                   | 2.3.x or 2.4.x          |
| 2.5.x                           | 3.3.x                                       | 2.5.1                   | 2.3.x                   |
| 2.4.x                           | 3.2.x                                       | 2.4.1                   | 2.2.x                   |
| 2.3.x                           | 3.2.x                                       | 2.3.1                   | 2.2.x                   |
| ~~2.2.x~~                       | ~~3.1.x~~                                   | ~~2.0.1, 2.1.x, 2.2.x~~ | ~~2.1.x (End of Life)~~ |
| ~~2.1.x~~                       | ~~3.0.x~~                                   | ~~1.0.2~~               | ~~2.0.x (End of Life)~~ |
| ~~1.3.x~~                       | ~~2.3.x~~                                   | ~~0.11.0.x, 1.0.x~~     | ~~1.5.x (End of Life)~~ |

#### 由于`springboot`用的版本是`2.4.2`，所以更换spring-kafka为`2.7.0`