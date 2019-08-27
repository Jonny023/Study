# 开发注意事项

* `domain`包名尽量小写
  * 一次包名引发的事件（同事包名用了大小写组合`package`，`idea`中运行正常，打包部署到`tomcat`报错，找不到`domain`类）

* `Domain.withTransaction`和`@Transactional`的区别
  * `withTransaction`将创建会话，`@Transactional`不会

# 打包命令

* 针对不同环境打包
  * `grails war` - 普通打包
  * `grails test war` - 指定测试环境打包
  * `grails -Dgrails.env=staging war` - 指定自定义staging环境打包
  * 若需打包为`jar`，则需注释`build.gradle`中的如下代码：
```groovy
apply plugin: "eclipse"
apply plugin: "idea"
//apply plugin: "war"
apply plugin: "org.grails.grails-web"
apply plugin: "org.grails.grails-gsp"
apply plugin: "asset-pipeline"
```

# 获取`domain`所有字段

```groovy
User.gormPersistentEntity.persistentProperties.collect {
   it.capitilizedName
}

# 
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler

def grailsApplication = ApplicationHolder.application
def domainDescriptor = grailsApplication.getArtefact(DomainClassArtefactHandler.TYPE, "PhysicalSiteAssessment")

def property = domainDescriptor.getPropertyByName("site")
def type = property.getType()
assert type instanceof Class
```

# 使用`gorm-logical-delete`插件报错

```bash
Type is null. Most probably you let a transform reuse existing ClassNodes with generics information, that is now used in a wrong context
```
* 解决方案：泛型类需要带上具体类型，如：

```groovy
class A<T> {

} 
class B extends A<B> {

}
```

# 参数获取

* 普通参数
  * `params.name`
  * `params["name"]`
  * `params.get("name")`
 
* 数组/集合
  * `params["ids[]"]`
  * `params.get("ids[]")`
  * 转换为集合/数组
    * `Long[] ids = params["ids[]"].collect {it as Long}`
    
* json数据
  * `request.JSON`，对应`application/json`的`raw`


### 日志提示

```
19:47:23.983 [main] WARN  grails.dev.Support 63 - Reloading is disabled. Development time reloading requires disabling the Java verifier. Please pass the argument '-Xverify:none' to the JVM
```

* 设置开关

```
grails {
    agent {
        enabled = false
    }
}
```

* tomcat启动报错

```
8-May-2019 01:21:57.987 SEVERE [localhost-startStop-1] org.apache.catalina.core.ContainerBase.addChildInternal ContainerBase.addChild: start: 
 org.apache.catalina.LifecycleException: Failed to start component [StandardEngine[Catalina].StandardHost[localhost].StandardContext[/Scddxt]]
	at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:153)
	at org.apache.catalina.core.ContainerBase.addChildInternal(ContainerBase.java:725)
	at org.apache.catalina.core.ContainerBase.addChild(ContainerBase.java:701)
	at org.apache.catalina.core.StandardHost.addChild(StandardHost.java:717)
	at org.apache.catalina.startup.HostConfig.deployDirectory(HostConfig.java:1092)
	at org.apache.catalina.startup.HostConfig$DeployDirectory.run(HostConfig.java:1834)
	at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)
Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'methodValidationPostProcessor' defined in class path resource [org/springframework/boot/autoconfigure/validation/ValidationAutoConfiguration.class]: Unsatisfied dependency expressed through method 'methodValidationPostProcessor' parameter 0; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'hibernateDatastoreServiceRegistry': Cannot resolve reference to bean 'hibernateDatastore' while setting constructor argument; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'hibernateDatastore': Bean instantiation via constructor failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.grails.orm.hibernate.HibernateDatastore]: Constructor threw exception; nested exception is org.hibernate.service.spi.ServiceException: Unable to create requested service [org.hibernate.engine.jdbc.env.spi.JdbcEnvironment]
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:749)
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:467)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1173)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1067)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:513)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:483)
	at org.springframework.beans.factory.support.AbstractBeanFactory$1.getObject(AbstractBeanFactory.java:306)
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:230)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:302)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:202)
	at org.springframework.context.support.PostProcessorRegistrationDelegate.registerBeanPostProcessors(PostProcessorRegistrationDelegate.java:225)
	at org.springframework.context.support.AbstractApplicationContext.registerBeanPostProcessors(AbstractApplicationContext.java:703)
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:528)
	at org.springframework.boot.context.embedded.EmbeddedWebApplicationContext.refresh(EmbeddedWebApplicationContext.java:122)
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:693)
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:360)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:303)
	at grails.boot.GrailsApp.run(GrailsApp.groovy:83)
	at org.springframework.boot.web.support.SpringBootServletInitializer.run(SpringBootServletInitializer.java:154)
	at org.grails.boot.context.web.GrailsAppServletInitializer.createRootApplicationContext(GrailsAppServletInitializer.groovy:57)
	at org.springframework.boot.web.support.SpringBootServletInitializer.onStartup(SpringBootServletInitializer.java:87)
	at org.springframework.web.SpringServletContainerInitializer.onStartup(SpringServletContainerInitializer.java:169)
	at org.apache.catalina.core.StandardContext.startInternal(StandardContext.java:5292)
	at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:147)
	... 10 more
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'hibernateDatastoreServiceRegistry': Cannot resolve reference to bean 'hibernateDatastore' while setting constructor argument; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'hibernateDatastore': Bean instantiation via constructor failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.grails.orm.hibernate.HibernateDatastore]: Constructor threw exception; nested exception is org.hibernate.service.spi.ServiceException: Unable to create requested service [org.hibernate.engine.jdbc.env.spi.JdbcEnvironment]
	at org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveReference(BeanDefinitionValueResolver.java:359)
	at org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveValueIfNecessary(BeanDefinitionValueResolver.java:108)
	at org.springframework.beans.factory.support.ConstructorResolver.resolveConstructorArguments(ConstructorResolver.java:648)
	at org.springframework.beans.factory.support.ConstructorResolver.autowireConstructor(ConstructorResolver.java:145)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireConstructor(AbstractAutowireCapableBeanFactory.java:1193)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1095)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.getSingletonFactoryBeanForTypeCheck(AbstractAutowireCapableBeanFactory.java:923)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.getTypeForFactoryBean(AbstractAutowireCapableBeanFactory.java:804)
	at org.springframework.beans.factory.support.AbstractBeanFactory.isTypeMatch(AbstractBeanFactory.java:558)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doGetBeanNamesForType(DefaultListableBeanFactory.java:432)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanNamesForType(DefaultListableBeanFactory.java:395)
	at org.springframework.beans.factory.BeanFactoryUtils.beanNamesForTypeIncludingAncestors(BeanFactoryUtils.java:220)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.findAutowireCandidates(DefaultListableBeanFactory.java:1267)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1101)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1066)
	at org.springframework.beans.factory.support.ConstructorResolver.resolveAutowiredArgument(ConstructorResolver.java:835)
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:741)
	... 33 more
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'hibernateDatastore': Bean instantiation via constructor failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.grails.orm.hibernate.HibernateDatastore]: Constructor threw exception; nested exception is org.hibernate.service.spi.ServiceException: Unable to create requested service [org.hibernate.engine.jdbc.env.spi.JdbcEnvironment]
	at org.springframework.beans.factory.support.ConstructorResolver.autowireConstructor(ConstructorResolver.java:279)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireConstructor(AbstractAutowireCapableBeanFactory.java:1193)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1095)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:513)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:483)
	at org.springframework.beans.factory.support.AbstractBeanFactory$1.getObject(AbstractBeanFactory.java:306)
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:230)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:302)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:197)
	at org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveReference(BeanDefinitionValueResolver.java:351)
	... 49 more
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.grails.orm.hibernate.HibernateDatastore]: Constructor threw exception; nested exception is org.hibernate.service.spi.ServiceException: Unable to create requested service [org.hibernate.engine.jdbc.env.spi.JdbcEnvironment]
	at org.springframework.beans.BeanUtils.instantiateClass(BeanUtils.java:154)
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:122)
	at org.springframework.beans.factory.support.ConstructorResolver.autowireConstructor(ConstructorResolver.java:271)
	... 58 more
Caused by: org.hibernate.service.spi.ServiceException: Unable to create requested service [org.hibernate.engine.jdbc.env.spi.JdbcEnvironment]
	at org.hibernate.service.internal.AbstractServiceRegistryImpl.createService(AbstractServiceRegistryImpl.java:264)
	at org.hibernate.service.internal.AbstractServiceRegistryImpl.initializeService(AbstractServiceRegistryImpl.java:228)
	at org.hibernate.service.internal.AbstractServiceRegistryImpl.getService(AbstractServiceRegistryImpl.java:207)
	at org.hibernate.engine.jdbc.internal.JdbcServicesImpl.configure(JdbcServicesImpl.java:51)
	at org.hibernate.boot.registry.internal.StandardServiceRegistryImpl.configureService(StandardServiceRegistryImpl.java:94)
	at org.hibernate.service.internal.AbstractServiceRegistryImpl.initializeService(AbstractServiceRegistryImpl.java:237)
	at org.hibernate.service.internal.AbstractServiceRegistryImpl.getService(AbstractServiceRegistryImpl.java:207)
	at org.hibernate.boot.model.process.spi.MetadataBuildingProcess.handleTypes(MetadataBuildingProcess.java:352)
	at org.hibernate.boot.model.process.spi.MetadataBuildingProcess.complete(MetadataBuildingProcess.java:111)
	at org.hibernate.boot.model.process.spi.MetadataBuildingProcess.build(MetadataBuildingProcess.java:83)
	at org.hibernate.boot.internal.MetadataBuilderImpl.build(MetadataBuilderImpl.java:418)
	at org.hibernate.boot.internal.MetadataBuilderImpl.build(MetadataBuilderImpl.java:87)
	at org.hibernate.cfg.Configuration.buildSessionFactory(Configuration.java:692)
	at org.grails.orm.hibernate.cfg.HibernateMappingContextConfiguration.buildSessionFactory(HibernateMappingContextConfiguration.java:274)
	at org.grails.orm.hibernate.connections.HibernateConnectionSourceFactory.create(HibernateConnectionSourceFactory.java:86)
	at org.grails.orm.hibernate.connections.AbstractHibernateConnectionSourceFactory.create(AbstractHibernateConnectionSourceFactory.java:39)
	at org.grails.orm.hibernate.connections.AbstractHibernateConnectionSourceFactory.create(AbstractHibernateConnectionSourceFactory.java:23)
	at org.grails.datastore.mapping.core.connections.AbstractConnectionSourceFactory.create(AbstractConnectionSourceFactory.java:64)
	at org.grails.datastore.mapping.core.connections.AbstractConnectionSourceFactory.create(AbstractConnectionSourceFactory.java:52)
	at org.grails.datastore.mapping.core.connections.ConnectionSourcesInitializer.create(ConnectionSourcesInitializer.groovy:24)
	at org.grails.orm.hibernate.HibernateDatastore.<init>(HibernateDatastore.java:196)
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
	at java.lang.reflect.Constructor.newInstance(Constructor.java:423)
	at org.springframework.beans.BeanUtils.instantiateClass(BeanUtils.java:142)
	... 60 more
Caused by: org.hibernate.HibernateException: Access to DialectResolutionInfo cannot be null when 'hibernate.dialect' not set
	at org.hibernate.engine.jdbc.dialect.internal.DialectFactoryImpl.determineDialect(DialectFactoryImpl.java:100)
	at org.hibernate.engine.jdbc.dialect.internal.DialectFactoryImpl.buildDialect(DialectFactoryImpl.java:54)
	at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.initiateService(JdbcEnvironmentInitiator.java:137)
	at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.initiateService(JdbcEnvironmentInitiator.java:35)
	at org.hibernate.boot.registry.internal.StandardServiceRegistryImpl.initiateService(StandardServiceRegistryImpl.java:88)
	at org.hibernate.service.internal.AbstractServiceRegistryImpl.createService(AbstractServiceRegistryImpl.java:254)
	... 85 more
```

## 解决方法

* 配置方言

```yml
dataSource:
    pooled: true
    jmxExport: true
    driverClassName: org.h2.Driver
    username: sa
    password:

dataSources:
    lookup:
        dialect: org.hibernate.dialect.MySQLInnoDBDialect
        driverClassName: com.mysql.jdbc.Driver
        username: lookup
        password: secret
        url: jdbc:mysql://localhost/lookup
        dbCreate: update

```

* idea项目卡的一批，设置jvm

```
-XX:MaxPermSize=1g
```
